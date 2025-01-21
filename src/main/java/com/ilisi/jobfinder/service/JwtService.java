package com.ilisi.jobfinder.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String SECRETKEY;

    @Value("${application.security.jwt.expiration}")
    private Long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshExpiration;

    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    ///generate token without extra clails
//    public String generateToken(User userDetails) {
//        return generateToken(new HashMap<>(),userDetails);
//    }
//
//    ///generate token with extra claims
//    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
//        return Jwts
//                .builder()
//                .setClaims(extraClaims)
//                .setSubject(userDetails.getUsername()) // set subject for jwt token
//                .setIssuedAt(new Date(System.currentTimeMillis())) //this will help us to check and validate expiration of token
//                .setExpiration(new Date(System.currentTimeMillis() +1000 *60 *24)) // expiration = currentTime + 24h
//                .signWith(getSignKey(),SignatureAlgorithm.HS256)
//                .compact();
//        //return createToken(claims, userName);
//
//    }
    public String generateToken(String email) {
        return generateToken(new HashMap<>(), email);
    }

    public String generateRefreshToken(String email) {
        return buildToken(new HashMap<>(), email,refreshExpiration);
    }

    private String generateToken(Map<String, Object> extraClaims, String email) {
        return buildToken(extraClaims, email,jwtExpiration);
    }

    private String buildToken(Map<String, Object> extraClaims, String email, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(email) // Utilisation de l'email comme sujet
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *60 * 24)) // Expiration après 24 heures
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Expiration après 1 min
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String extractedUsername = extractEmail(token);
        return (extractedUsername.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /*private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }*/

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRETKEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}