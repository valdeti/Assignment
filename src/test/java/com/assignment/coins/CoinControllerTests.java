package com.assignment.coins;


import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assignment.coins.favorite.FavoriteCoin;
import com.assignment.coins.favorite.FavoriteCoinRepository;
import com.assignment.security.SecurityUtil;
import com.assignment.security.UserDetailsServiceImpl;
import com.assignment.users.UserFixtures;
import com.assignment.coins.favorite.FavoriteCoinMapper;
import com.assignment.exceptions.FavoriteCoinException;
import com.assignment.security.jwt.JwtUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@WebMvcTest(CoinController.class)
class CoinControllerTests {

  private static MockedStatic<SecurityUtil> mockedSecurityUtil;

  @MockBean
  private FavoriteCoinRepository favoriteCoinRepository;
  @MockBean
  private FavoriteCoinMapper favoriteCoinMapper;
  @MockBean
  private CoinService coinService;
  @MockBean
  private RestTemplate restTemplate;

  @MockBean
  private JwtUtil jwtUtil;

  @MockBean
  private UserDetailsServiceImpl userDetailsServiceImpl;

  @Autowired
  private MockMvc mockMvc;

  @BeforeAll
  static void beforeAll() {
    mockedSecurityUtil = Mockito.mockStatic(SecurityUtil.class);
    mockedSecurityUtil.when(SecurityUtil::currentLoggedInUserId).thenReturn(UserFixtures.ID);
  }

  @AfterAll
  static void afterAll() {
    mockedSecurityUtil.close();
  }

  @Test
  @WithMockUser(username = "John")
  void listAllCoins_GivenValidRequest_ShouldMakeAPICall() throws Exception {
    var coinsTemplate = new CoinsTemplate();
    coinsTemplate.setData(Collections.emptyList());
    given(this.restTemplate.getForObject(anyString(), any())).willReturn(coinsTemplate);
    doNothing().when(this.coinService).addCoinToFavorites(any(), any());
    this.mockMvc.perform(post("/api/coins/favorite/{coinId}", "bitcoin")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(this.restTemplate).getForObject("https://api.coincap.io/v2/assets", CoinsTemplate.class);
    verifyNoMoreInteractions(this.restTemplate);
  }

  @Test
  @WithMockUser(username = "John")
  void addFavoriteCoin_GivenValidCoinId_ShouldAddCoinToFavorites() throws Exception {
    var coinTemplate = new CoinsTemplate();
    given(this.restTemplate.getForObject(anyString(), any())).willReturn(coinTemplate);
    doNothing().when(this.coinService).addCoinToFavorites(any(), any());
    this.mockMvc.perform(post("/api/coins/favorite/{coinId}", "bitcoin")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    verify(this.restTemplate).getForObject("https://api.coincap.io/v2/assets", CoinsTemplate.class);
    verifyNoMoreInteractions(this.restTemplate);
  }

  @Test
  @WithMockUser(username = "John")
  void removeFavoriteCoin_GivenValidCoinId_ShouldRemoveCoinFromFavorites() throws Exception {
    var favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoin(CoinFixtures.create());
    favoriteCoin.setUser(UserFixtures.create());
    given(this.favoriteCoinRepository.findByUserIdAndCoinId(any(), any())).willReturn(Optional.of(favoriteCoin));
    doNothing().when(this.coinService).removeCoinFromFavorites(any());
    this.mockMvc.perform(post("/api/coins/favorite/{coinId}/remove", "bitcoin")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    verify(this.favoriteCoinRepository).findByUserIdAndCoinId(UserFixtures.ID, CoinFixtures.COIN_ID);
    verify(this.coinService).removeCoinFromFavorites(favoriteCoin);
    verifyNoMoreInteractions(this.favoriteCoinMapper);
    verifyNoMoreInteractions(this.favoriteCoinRepository);
  }

  @Test
  @WithMockUser(username = "John")
  void removeFavoriteCoin_GivenInValidCoinIdIsGiven_ShouldThrowException() throws Exception {
    given(this.favoriteCoinRepository.findByUserIdAndCoinId(any(), any())).willThrow(FavoriteCoinException.class);
    doNothing().when(this.coinService).removeCoinFromFavorites(any());
    this.mockMvc.perform(post("/api/coins/favorite/{coinId}/remove", "lorem")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    verify(this.favoriteCoinRepository).findByUserIdAndCoinId(UserFixtures.ID, "lorem");
    verifyNoInteractions(this.coinService);
  }

  @Test
  @WithMockUser(username = "John")
  void listAll_GivenValidRequest_ShouldReturnAllCoinsFavorite() throws Exception {
    var favoriteCoin = new FavoriteCoin();
    favoriteCoin.setCoin(CoinFixtures.create());
    favoriteCoin.setUser(UserFixtures.create());
    var coinsResponse = new CoinsResponse(CoinFixtures.COIN_ID, CoinFixtures.RANK, CoinFixtures.SYMBOL, CoinFixtures.NAME, CoinFixtures.SUPPLY,
        CoinFixtures.MAX_SUPPLY);
    given(this.favoriteCoinRepository.findAllByUserId(any())).willReturn(List.of(favoriteCoin));
    given(this.favoriteCoinMapper.map(any())).willReturn(coinsResponse);
    this.mockMvc.perform(get("/api/coins/favorite/all")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id", equalTo(CoinFixtures.COIN_ID)))
        .andExpect(jsonPath("$[0].rank", equalTo(CoinFixtures.RANK)))
        .andExpect(jsonPath("$[0].symbol", equalTo(CoinFixtures.SYMBOL)))
        .andExpect(jsonPath("$[0].name", equalTo(CoinFixtures.NAME)))
        .andExpect(jsonPath("$[0].supply", equalTo(CoinFixtures.SUPPLY)))
        .andExpect(jsonPath("$[0].maxSupply", equalTo(CoinFixtures.MAX_SUPPLY)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "John")
  void listAllCoins_GivenAPIReturnNull_ShouldThrowException() throws Exception {//
    given(this.restTemplate.getForObject(any(), any())).willReturn(null);
    this.mockMvc.perform(get("/api/coins")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    verify(this.restTemplate).getForObject("https://api.coincap.io/v2/assets", CoinsTemplate.class);
    verifyNoInteractions(this.coinService);
  }

  @Test
  @WithMockUser(username = "John")
  void addFavoriteCoin_GivenAPIReturnNull_ShouldThrowException() throws Exception {//
    given(this.restTemplate.getForObject(any(), any())).willReturn(null);
    this.mockMvc.perform(post("/api/coins/favorite/{coinId}", "bitcoin")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    verify(this.restTemplate).getForObject("https://api.coincap.io/v2/assets", CoinsTemplate.class);
    verifyNoInteractions(this.coinService);
  }
}
