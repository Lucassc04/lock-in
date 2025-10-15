package lucas.lockIn.lockIn_backend.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long access_expiration;

    @Value("${jwt.refresh.expiration}")
    private long refresh_expiration;

    /**
     * Retrieves the signing key used for JWT token generation and validation.
     * The key is generated from the configured secret using HMAC-SHA algorithm.
     *
     * @return SecretKey object used for signing and verifying JWT tokens
     */
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generates a JWT access token for the specified user.
     * The access token is a short duration token.
     * The refresh token is a long duration token.
     * The token includes the username as subject, userId as a custom claim,
     * and is configured with issuance and expiration timestamps.
     *
     * @param username the username to be set as the token subject
     * @param userId the user ID to be included as a custom claim in the token
     * @param access if the token to be generated is an access token
     * @return a signed JWT access token string if access is true, refresh token string if not
     */
    public String generateToken(String username, Long userId, boolean access){
            return Jwts.builder()
                    .subject(username)
                    .claim("userId", userId)
                    .issuedAt(new Date())
                    .expiration(new Date(System.currentTimeMillis() + (access ? access_expiration : refresh_expiration)))
                    .signWith(getSigningKey())
                    .compact();
    }

    /**
     * Validates the integrity and authenticity of a JWT token.
     * Verifies the token signature and structure without checking expiration.
     *
     * @param token the JWT token string to be validated
     * @return true if the token is valid and properly signed, false otherwise
     */
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    /**
     * Extracts the username from the JWT token.
     * The username is stored in the token's subject claim.
     *
     * @param token the JWT token string
     * @return the username extracted from the token subject
     */
    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts the user ID from the JWT token.
     * The user ID is stored as a custom claim in the token.
     *
     * @param token the JWT token string
     * @return the user ID extracted from the token claims
     */
    public Long extractUserId(String token){
        return extractClaim(token, claims -> claims.get("userId", Long.class));
    }

    /**
     * Extracts all claims from the JWT token.
     * Parses and validates the token signature, then returns the payload containing all claims.
     *
     * @param token the JWT token string
     * @return Claims object containing all token claims
     * @throws JwtException if the token is invalid or signature verification fails
     */
    private Claims extractAllClaims(String token) throws JwtException {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extracts a specific claim from the JWT token using a custom resolver function.
     * This is a generic method that allows extraction of any claim type.
     *
     * @param <T> the type of the claim to be extracted
     * @param token the JWT token string
     * @param claimsResolver a function that resolves the desired claim from the Claims object
     * @return the extracted claim value
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
}
