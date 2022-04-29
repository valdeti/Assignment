package com.assignment.security;

import com.assignment.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Authentication")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final JwtUtil jwtTokenUtil;

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
    log.debug("REST request to authorize request with email: {}", authenticationRequest.email());
    try {
      this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.email(),
              authenticationRequest.password())
      );
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Incorrect credentials", e);
    }
		final UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(authenticationRequest.email());
		final String jwt = this.jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
  }
}
