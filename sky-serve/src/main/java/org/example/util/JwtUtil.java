package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.example.property.JwtProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
   @Autowired
   JwtProperty jwtProperty;
   Key key ; ;
   @PostConstruct
   public void init() {
       key = Keys.hmacShaKeyFor(jwtProperty.getJwtKey().getBytes(StandardCharsets.UTF_8));
   }

    public String generateToken(String subject){
      return   Jwts.builder().setSubject(subject)
              .setExpiration(new Date(System.currentTimeMillis()+jwtProperty.getJwtExpiration()))
              .signWith(key).compact();
    }
    public Claims parseToken(String token){
       Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
       return claims;
    }
}
