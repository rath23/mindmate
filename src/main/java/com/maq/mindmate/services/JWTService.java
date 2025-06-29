package com.maq.mindmate.services;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.maq.mindmate.configuration.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class JWTService {

    private final JwtProperties jwtProperties;
    public JWTService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    //    private final String secretKey="TAYiVbVHGxFt0uQZocpWt6PZGL+PKeKLxlMpC0qKJn8=";

    // public JWTService(){
    //     secretKey = generateSecretKey();
    // }

    // public String generateSecretKey() {
    //     try {
    //         KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
    //         SecretKey secretKey = keyGen.generateKey();
    //         String keyToReturn = Base64.getEncoder().encodeToString(secretKey.getEncoded());
    //         System.out.println(keyToReturn);
    //         return keyToReturn;
    //     } catch (NoSuchAlgorithmException e) {
    //         throw new RuntimeException("Error generating secret key", e);
    //     }
    // }

    //   private Key getKey() {
    // System.out.println("Secret Key: " + secretKey);
    //     byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    //     return Keys.hmacShaKeyFor(keyBytes);
    // }

          private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }



    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts
                .builder()
                .header()
                .type("JWT")
                .and()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .and()
                .signWith(getKey())
//                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    // Extract the username from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract a claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    // Check if the token is expired
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Validate the token against user details and expiration
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateTokenForWebSockets(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        String username = extractUsername(token);

        // Optional: You can load actual roles from DB if needed
        return new UsernamePasswordAuthenticationToken(
                username,
                null,
                List.of(new SimpleGrantedAuthority("USER")) // default role
        );
    }

}

