package com.calypso.binar.security;

import com.calypso.binar.service.exception.UserLoggedOutException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtService {
  private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

  @Value("${security.jwtSecret}")
  private String jwtSecret;

  @Value("${security.jwtExpirationMs}")
  private int jwtExpirationMs;

  private final List<String> blacklisted = new ArrayList<>();

  public String generateJwtToken(UserDetails userPrincipal) {
    return generateTokenFromUsername(userPrincipal.getUsername());
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      validateToken(authToken);
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    } catch (UserLoggedOutException e) {
      logger.error(e.getMessage());
    }
    return false;
  }

  public void logOut(String token){
    this.blacklisted.add(token);
  }

  private boolean isBlacklisted(String token){
    return this.blacklisted.contains(token);
  }

  private void validateToken(String authToken) throws UserLoggedOutException {
    if(isBlacklisted(authToken)){
      throw new UserLoggedOutException();
    }
  }

}
