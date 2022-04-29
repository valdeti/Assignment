package com.assignment.security;

import com.assignment.users.User;
import com.assignment.users.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) {
    var user = this.userRepository.findUserByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User with email: %s not found".formatted(username)));
    return toUserDetails(user);
  }

  private UserDetails toUserDetails(User user) {
    return new AuthenticatedUser(
        user.getId(),
        user.getEmail(),
        user.getPassword(),
        new ArrayList<>()
    );
  }
}
