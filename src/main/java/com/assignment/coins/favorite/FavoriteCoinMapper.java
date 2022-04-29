package com.assignment.coins.favorite;

import com.assignment.coins.Coin;
import com.assignment.coins.CoinsResponse;
import com.assignment.users.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class FavoriteCoinMapper {

  public FavoriteCoin map(User user, Coin coin) {
    return FavoriteCoin.create(user, coin);
  }

  public CoinsResponse map(FavoriteCoin favoriteCoin) {
    var coin = favoriteCoin.getCoin();
    return new CoinsResponse(coin.getCoinId(), coin.getRank(), coin.getSymbol(), coin.getName(), coin.getSupply(), coin.getMaxSupply());
  }
}
