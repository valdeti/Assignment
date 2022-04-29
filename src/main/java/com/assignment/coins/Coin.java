package com.assignment.coins;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coins")
public class Coin  {

  @Id
  @Column(name = "coin_id")
  String coinId;

  @Column(name = "rank")
  String rank;

  @Column(name = "symbol")
  String symbol;

  @Column(name = "name")
  String name;

  @Column(name = "supply")
  String supply;

  @Column(name = "maxSupply")
  String maxSupply;

  public Coin(String coinId, String rank, String symbol, String name, String supply, String maxSupply) {
    this.coinId = coinId;
    this.rank = rank;
    this.symbol = symbol;
    this.name = name;
    this.supply = supply;
    this.maxSupply = maxSupply;
  }

  public Coin() {
  }
}
