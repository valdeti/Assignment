package com.assignment.coins;

import static com.assignment.security.SecurityUtil.currentLoggedInUserId;

import com.assignment.coins.favorite.FavoriteCoinRepository;
import com.assignment.coins.favorite.FavoriteCoinMapper;
import com.assignment.exceptions.CoinException;
import com.assignment.exceptions.FavoriteCoinException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@Tag(name = "Coins")
@RequiredArgsConstructor
@RequestMapping("/api/coins")
public class CoinController {

  private final FavoriteCoinRepository favoriteCoinRepository;
  private final FavoriteCoinMapper favoriteCoinMapper;
  private final CoinService coinService;
  private final RestTemplate restTemplate;

  @GetMapping
  public ResponseEntity<List<CoinsResponse>> listAllCoins() {
    log.debug("REST request to retrieve all coins");
    var url = "https://api.coincap.io/v2/assets";
    var coins = this.restTemplate.getForObject(url, CoinsTemplate.class);
    nullResponseCheck(coins);
    List<CoinsResponse> mappedClassesResponseList = map(coins);
    return ResponseEntity.ok(mappedClassesResponseList);
  }

  private List<CoinsResponse> map(CoinsTemplate coins) {
    return coins.getData().stream()
        .map(mappedClassTwo -> new CoinsResponse(mappedClassTwo.getId(), mappedClassTwo.getRank(), mappedClassTwo.getSymbol(),
            mappedClassTwo.getName(), mappedClassTwo.getSupply(), mappedClassTwo.getMaxSupply())).toList();
  }

  private void nullResponseCheck(CoinsTemplate coins) {
    if (coins == null) {
      throw CoinException.notAvailable("");
    }
  }

  @PostMapping("/favorite/{coinId}")
  public ResponseEntity<Void> addFavoriteCoin(@PathVariable String coinId) {
    log.debug("REST request to add as favorite coin with id: {}", coinId);
    var url = "https://api.coincap.io/v2/assets";
    var coins = this.restTemplate.getForObject(url, CoinsTemplate.class);
    nullResponseCheck(coins);
    this.coinService.addCoinToFavorites(coins, coinId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/favorite/{coinId}/remove")
  public ResponseEntity<Void> removeFavoriteCoin(@PathVariable String coinId) {
    log.debug("REST request to remove coin from favorites with id: {}", coinId);
    var favoriteCoin = this.favoriteCoinRepository.findByUserIdAndCoinId(currentLoggedInUserId(), coinId)
        .orElseThrow(()-> FavoriteCoinException.favoriteCoinNotFound(coinId));
    this.coinService.removeCoinFromFavorites(favoriteCoin);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/favorite/all")
  public ResponseEntity<List<CoinsResponse>> listAll() {
    log.debug("REST request to list all favorite coins");
    var favoriteCoin = this.favoriteCoinRepository.findAllByUserId(currentLoggedInUserId());
    return ResponseEntity.ok(favoriteCoin.stream().map(favoriteCoinMapper::map).toList());
  }
}
