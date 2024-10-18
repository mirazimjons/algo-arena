package uz.thejaver.algoarena.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.security.jwt.secret-key}")
    private String secretKey;

    @Value("${app.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.security.jwt.refresh-expiration}")
    private long refreshExpiration;

    @Nonnull
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(@Nonnull String token, @Nonnull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(@Nonnull Map<String, Object> extraClaims, @Nonnull UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(@Nonnull UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    @Nonnull
    private String buildToken(
            @Nonnull Map<String, Object> extraClaims,
            @Nonnull UserDetails userDetails,
            @Nonnull Long expiration
    ) {
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    @Nonnull
    public Boolean isTokenValid(@Nonnull String token) {
        return !isTokenExpired(token);
    }

    @Nonnull
    public Boolean isTokenValid(@Nonnull String token, @Nonnull UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Nonnull
    private Boolean isTokenExpired(@Nonnull String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(@Nonnull String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(@Nonnull String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Nonnull
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
