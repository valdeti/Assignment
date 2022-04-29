package com.assignment.coins;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verifyNoInteractions;

import com.assignment.coins.favorite.FavoriteCoinRepository;
import com.assignment.coins.favorite.FavoriteCoinService;
import com.assignment.exceptions.CoinException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CoinServiceTests {

  @Mock
  private CoinRepository coinRepository;

  @Mock
  private FavoriteCoinService favoriteCoinService;

  @Mock
  private FavoriteCoinRepository favoriteCoinRepository;

  private CoinService coinService;

  @BeforeEach
  void setUp() {
    this.coinService = new CoinService(this.coinRepository, this.favoriteCoinService, this.favoriteCoinRepository);
  }

  @Test
  void addCoinToFavorites_GivenNonExistingCoinIdIsGiven_ShouldThrowException() {
    var coinsTemplate = new CoinsTemplate();
    coinsTemplate.setData(Collections.emptyList());
    assertThatExceptionOfType(CoinException.class)
        .isThrownBy(() -> this.coinService.addCoinToFavorites(coinsTemplate, "lorem"))
        .withMessage("Coin with id: lorem is not found");
    verifyNoInteractions(this.coinRepository);
    verifyNoInteractions(this.favoriteCoinService);
  }
}
