package com.assignment.coins;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinsTemplate {

  List<CoinTemplateFields> data;

  @Getter
  @Setter
  public static class CoinTemplateFields {

    String id;
    String rank;
    String symbol;
    String name;
    String supply;
    String maxSupply;
    String marketCapUsd;
    String volumeUsd24Hr;
    String priceUsd;
    String changePercent24Hr;
    String vwap24Hr;
    String explorer;
  }
}
