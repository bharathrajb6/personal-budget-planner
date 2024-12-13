package com.example.personal_budget_planner.Service.Impl;

import com.example.personal_budget_planner.Model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    /***
     * THis method is used to generate the secret key for the jwt token
     * @return
     */
    private SecretKey getSignInKey() {
        String SECRET_KEY = "QmBfQdj4+vxDXSrrYfItNTkuszwFaTClm5PC3Y1SVj2HkdIJNzSqLXXL9+zJM76R";
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

    /***
     * This method is used to generate the token
     * @param user
     * @return
     */
    public String generateToken(User user) {
        return Jwts.builder().
                subject(user.getUsername()).
                issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis() + 24 * 60 * 1000)).
                signWith(getSignInKey()).compact();
    }

    /***
     * This method is used to extract the username from the token
     * @param token
     * @return
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /***
     * This method is used to extract the claim from the token
     * @param token
     * @param claimsTFunction
     * @return
     * @param <T>
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    /***
     * This method is used to extract all the claims from the token
     * @param token
     * @return
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    }

    /***
     * This method is used to check if the token is valid
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /***
     * This method is used to check if the token is expired
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /***
     * This method is used to extract the expiration date from the token
     * @param token
     * @return
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
