package com.assignment.exceptions;

public class FavoriteCoinException extends RuntimeException {

  private FavoriteCoinException(String message) {
    super(message);
  }

  public static FavoriteCoinException favoriteCoinNotFound(String coinId) {
    return new FavoriteCoinException("Favorite coin with id: %s is not found".formatted(coinId));
  }
}
