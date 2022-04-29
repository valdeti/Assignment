package com.assignment.security;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

  private SecurityUtil() {

  }

  public static String currentLoggedInUserId() {
    return authenticatedUser()
        .map(AuthenticatedUser::getId)
        .orElseThrow();
  }

  private static Optional<AuthenticatedUser> authenticatedUser() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
        .map(Authentication::getPrincipal)
        .filter(principal -> principal instanceof AuthenticatedUser)
        .map(AuthenticatedUser.class::cast);
  }

}
