package com.assignment.coins;

public class CoinFixtures {

  public static final String COIN_ID = "bitcoin";
  public static final String RANK = "1";
  public static final String SYMBOL = "BTC";
  public static final String NAME = "Bitcoin";
  public static final String SUPPLY = "19023450.0000000000000000";
  public static final String MAX_SUPPLY = "21000000.0000000000000000";

  private CoinFixtures() {
  }

  public static Coin create() {
    return new Coin(COIN_ID, RANK, SYMBOL, NAME, SUPPLY, MAX_SUPPLY);
  }
}
