package com.e5.ems.util;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.e5.ems.exception.AuthenticationException;

/**
 * <p>
 *     It contains Utility methods of JWT
 * </p>
 */
@Component
public class JwtUtil {


    // 15 minutes in milliseconds
    private static final long EXPIRATION_TIME = 900000;
    private static final SecretKey key;
    // This block is used for generating the key
    static  {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        key = keyGenerator.generateKey();
    }

    /**
     * <p>
     *     This method is used for extract the username from the token
     * </p>
     * @param token
     *          is used for extracting the username from token
     * @return the extracted string
     */
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * <p>
     *     This method is used for extract claims
     * </p>
     * @param token
     *          is used for extracting claims
     * @param claimsResolver
     *          is used for get the Claims as input and return the username claim from that.
     * @return the
     */
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *
     * @param token
     *          is used for extracting the claims from token by verify with key
     * @return {@link Claims}
     *          the payload is returned
     */
    private static Claims extractAllClaims(String token) {
       try {
           return Jwts
                   .parser()
                   .verifyWith(key)
                   .build()
                   .parseSignedClaims(token)
                   .getPayload();
       } catch (JwtException e) {
           throw new AuthenticationException("Token cannot be parsable, enter valid token");
       }
    }

    /**
     * <p>
     *     This method is used for generating the new JWT token
     * </p>
     * @param username
     *          is used for set the username into claims
     * @return the generated token
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * <p>
     *     This method is used for create a token using claims
     * </p>
     * @param claims
     *          is used for create a payload of JWT token
     * @param subject
     *          is used for add username in claims
     * @return the generated token
     */
    private static String createToken(Map<String, Object> claims, String subject) {
        try {
            return Jwts.builder()
                    .claims(claims)
                    .subject(subject)
                    .issuedAt(new Date(System.currentTimeMillis()))
                    .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(key)
                    .compact();
        } catch (JwtException e) {
            throw new AuthenticationException("Token cannot be generated");
        }
    }

    /**
     * <p>
     *     This method is used for validate the token is valid or not
     * </p>
     * @param token
     *          is used for get the expiration details
     * @return {@code true} if the token is not expired otherwise {@code false}
     */
    public static boolean isTokenExpired(String token) {
         if(extractClaim(token, Claims::getExpiration).before(new Date())) {
             throw new AuthenticationException("Token expired");
         }
         return true;
    }

    /**
     * <p>
     *     This method is used for validating the token by username
     * </p>
     * @param token
     *          is used for extract the username in the token
     * @param userDetails
     *
     */
    public static void validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if(!username.equals(userDetails.getUsername()) || !isTokenExpired(token) ) {
            throw new AuthenticationException("Invalid token");
        }

    }
}