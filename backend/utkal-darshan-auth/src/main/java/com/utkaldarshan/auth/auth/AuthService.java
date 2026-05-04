package com.utkaldarshan.auth.auth;

import com.utkaldarshan.auth.auth.dto.AuthResponse;
import com.utkaldarshan.auth.auth.dto.LoginRequest;
import com.utkaldarshan.auth.auth.dto.RegisterRequest;
import com.utkaldarshan.auth.common.ApiException;
import com.utkaldarshan.auth.user.User;
import com.utkaldarshan.auth.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UserRepository users;
  private final BCryptPasswordEncoder encoder;
  private final JwtService jwt;

  public AuthService(UserRepository users, BCryptPasswordEncoder encoder, JwtService jwt) {
    this.users = users;
    this.encoder = encoder;
    this.jwt = jwt;
  }

  @Transactional
  public AuthResponse register(RegisterRequest req) {
    if (users.existsByEmailIgnoreCase(req.getEmail())) {
      throw new ApiException(HttpStatus.CONFLICT, "Email already exists");
    }

    User u = new User();
    u.setName(req.getName().trim());
    u.setEmail(req.getEmail().trim().toLowerCase());
    u.setPasswordHash(encoder.encode(req.getPassword()));
    users.save(u);

    String token = jwt.createToken(u.getEmail());
    return new AuthResponse(true, "Registration successful", token);
  }

  public AuthResponse login(LoginRequest req) {
    User u =
        users
            .findByEmailIgnoreCase(req.getEmail().trim())
            .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

    boolean ok = encoder.matches(req.getPassword(), u.getPasswordHash());
    if (!ok) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    String token = jwt.createToken(u.getEmail());
    return new AuthResponse(true, "Login successful", token);
  }
}

