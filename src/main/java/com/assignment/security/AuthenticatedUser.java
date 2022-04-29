package com.assignment.security;

import java.util.List;
import java.util.Objects;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthenticatedUser extends User {

  private final String id;

  public AuthenticatedUser(String id,
                           String username,
                           String password,
                           List<SimpleGrantedAuthority> authorities) {
    super(username, password, authorities);
    this.id = id;
  }

  public String getId() {
    return id;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof AuthenticatedUser) {
      return id.equals(((AuthenticatedUser) other).getId()) && super.equals(other);
    }
    return super.equals(other);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, getUsername());
  }
}
