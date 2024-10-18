package uz.thejaver.algoarena.config.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uz.thejaver.algoarena.config.security.SecurityConstants;
import uz.thejaver.algoarena.config.security.UserDetailsImpl;

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

    public <T> T extractClaim(@Nonnull String token, @Nonnull Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(@NonNull UserDetailsImpl userDetails) {
        return buildAccessToken(new HashMap<>(), userDetails, jwtExpiration);
    }

    public String generateRefreshToken(@Nonnull UserDetailsImpl userDetails) {
        return buildRefreshToken(Map.of(), userDetails, refreshExpiration);
    }

    @Nonnull
    private String buildAccessToken(
            @Nonnull Map<String, Object> extraClaims,
            @Nonnull UserDetailsImpl userDetails,
            @Nonnull Long expiration
    ) {
        extraClaims.put(SecurityConstants.ROLES_CLAIM_NAME, userDetails.getAuthorities());
        return buildToken(extraClaims, userDetails, expiration);
    }

    @Nonnull
    private String buildRefreshToken(
            @Nonnull Map<String, Object> extraClaims,
            @Nonnull UserDetailsImpl userDetails,
            @Nonnull Long expiration
    ) {
        return buildToken(extraClaims, userDetails, expiration);
    }

    @Nonnull
    private String buildToken(
            @Nonnull Map<String, Object> extraClaims,
            @Nonnull UserDetailsImpl userDetails,
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
    public String extractUsername(@NonNull String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(@Nonnull String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Nonnull
    private Boolean isTokenExpired(@Nonnull String token) {
        return extractExpiration(token).before(new Date());
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
