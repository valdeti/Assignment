package com.assignment.users;

public class UserFixtures {

  public static final String ID = "wa68162c-ef6d-46a4-8890-1a7c4253ed92";
  public static final String FIRST_NAME = "John";
  public static final String LAST_NAME = "Doe";
  public static final String EMAIL = "john_doe@email.com";
  public static final String PASSWORD = "password";

  private UserFixtures() {
  }

  public static User create() {
    return new User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
  }
}
