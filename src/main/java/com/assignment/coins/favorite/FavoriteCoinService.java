package com.assignment.coins.favorite;

import static com.assignment.security.SecurityUtil.currentLoggedInUserId;

import com.assignment.coins.Coin;
import com.assignment.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FavoriteCoinService {

  private final UserRepository userRepository;
  private final FavoriteCoinRepository favoriteCoinRepository;
  private final FavoriteCoinMapper favoriteCoinMapper;

  @Transactional
  public void add(Coin coin) {
    var user = this.userRepository.findById(currentLoggedInUserId()).orElseThrow();
    var favoriteProduct = this.favoriteCoinMapper.map(user, coin);
    this.favoriteCoinRepository.save(favoriteProduct);
  }

  public void remove(String userId, String coinId) {
    this.favoriteCoinRepository.deleteById(new FavoriteCoinPkId(userId,coinId));
  }
}
