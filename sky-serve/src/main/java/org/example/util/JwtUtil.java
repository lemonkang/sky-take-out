package org.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.example.property.JwtProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    @Autowired
   JwtProperty jwtProperty;
   Key key ; ;
   @PostConstruct
   public void init() {
       
       key = Keys.hmacShaKeyFor(jwtProperty.getJwtKey().getBytes(StandardCharsets.UTF_8));
   }
   @PreDestroy
   public void destroy() {
       log.info("PreDestroy JwtUtil");
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
    public boolean validateToken(String token){
      return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }
}
