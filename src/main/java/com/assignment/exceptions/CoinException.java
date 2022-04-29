package com.assignment.exceptions;

public class CoinException extends RuntimeException {

  private CoinException(String message) {
    super(message);
  }

  public static CoinException notFound(String id) {
    return new CoinException("Coin with id: %s is not found".formatted(id));
  }

  public static CoinException notAvailable(String id) {
    return new CoinException("No data is available at the moment");
  }
}
