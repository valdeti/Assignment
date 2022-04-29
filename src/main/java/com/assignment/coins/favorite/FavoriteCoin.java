package com.assignment.coins.favorite;

import com.assignment.coins.Coin;
import com.assignment.users.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "favorite_coins")
@IdClass(FavoriteCoinPkId.class)
public class FavoriteCoin {

  @Id
  @JoinColumn(name = "user_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @Id
  @JoinColumn(name = "coin_id")
  @ManyToOne(fetch = FetchType.LAZY)
  private Coin coin;

  public static FavoriteCoin create(User user, Coin coin) {
    var favoriteCoin = new FavoriteCoin();
    favoriteCoin.setUser(user);
    favoriteCoin.setCoin(coin);
    return favoriteCoin;
  }
}
