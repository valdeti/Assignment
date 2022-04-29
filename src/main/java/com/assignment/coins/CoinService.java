package com.assignment.coins;

import com.assignment.coins.favorite.FavoriteCoin;
import com.assignment.coins.favorite.FavoriteCoinRepository;
import com.assignment.coins.favorite.FavoriteCoinService;
import com.assignment.exceptions.CoinException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoinService {

  private static final int THIRTY_SECONDS = 30000;

  private final CoinRepository coinRepository;
  private final FavoriteCoinService favoriteCoinService;
  private final FavoriteCoinRepository favoriteCoinRepository;

  @Transactional
  public void addCoinToFavorites(CoinsTemplate coins, String coinId) {
    var mappedClassTwoStream = coins.getData().stream().filter(mappedClassTwo -> mappedClassTwo.getId().equals(coinId)).findFirst()
        .orElseThrow(() -> CoinException.notFound(coinId));
    var coin = new Coin(mappedClassTwoStream.getId(), mappedClassTwoStream.getRank(), mappedClassTwoStream.getSymbol(), mappedClassTwoStream.getName(),
        mappedClassTwoStream.getSupply(), mappedClassTwoStream.getMaxSupply());
    this.coinRepository.save(coin);
    this.favoriteCoinService.add(coin);
  }

  public void removeCoinFromFavorites(FavoriteCoin favoriteCoin) {
    this.favoriteCoinService.remove(favoriteCoin.getUser().getId(), favoriteCoin.getCoin().getCoinId());
  }

  @Scheduled(fixedRate = THIRTY_SECONDS)
  public void unusedCoinRemoval() {
    var coins = this.coinRepository.findAll().stream().map(Coin::getCoinId).toList();
    var favoriteCoins = this.favoriteCoinRepository.findAll().stream().map(FavoriteCoin::getCoin).map(Coin::getCoinId).toList();
    for (String coinId : coins) {
      if (!favoriteCoins.contains(coinId)) {
        this.coinRepository.deleteById(coinId);
        log.info("Coin with id: %s has been removed".formatted(coinId));
      }
    }
  }
}
