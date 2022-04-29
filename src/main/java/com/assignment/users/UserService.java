package com.assignment.users;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  public void createUser(UserRequest request) {
    var encodedPassword = this.passwordEncoder.encode(request.password());
    User user = new User(request.firstName(), request.lastName(), request.email(), encodedPassword);
    this.userRepository.save(user);
  }
}
