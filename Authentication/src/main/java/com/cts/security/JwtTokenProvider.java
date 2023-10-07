package com.cts.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt.secret}")
    private String jwtSecret;
    @Value("${app.jwt.expiration-milliseconds}")

    private long jwtExpirationDate;

    public String generateToken(Authentication authentication){
        String username=authentication.getName();
        Date currentDate=new Date();
        Date expiryDate=new Date(currentDate.getTime()+jwtExpirationDate);
        var token=Jwts.builder().setSubject(username)
                .setExpiration(expiryDate)
                .setIssuedAt(new Date())
                .signWith(key(),SignatureAlgorithm.HS256)
                .compact();

            return token;
    }

    private Key key(){
       return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsername(String token){
        Claims claims=Jwts.parserBuilder().setSigningKey(key())
                .build().parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token){
       try{
           Jwts.parserBuilder().setSigningKey(key())
                   .build().parse(token);
           return true;
       }
       catch(MalformedJwtException ex){
           throw new MyAppException(HttpStatus.BAD_REQUEST,"Invalid JWT token");
       }
       catch(ExpiredJwtException ex){
           throw new MyAppException(HttpStatus.BAD_REQUEST,"Expired JWT token");
       }
       catch(UnsupportedJwtException ex){
           throw new MyAppException(HttpStatus.BAD_REQUEST,"Unsupported JWT Token");
       }
       catch(IllegalArgumentException ex){
           throw new MyAppException(HttpStatus.BAD_REQUEST,"JWT Claims String is empty");
       }


    }


}