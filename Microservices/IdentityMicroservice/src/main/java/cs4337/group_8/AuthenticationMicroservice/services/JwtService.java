package cs4337.group_8.AuthenticationMicroservice.services;

import cs4337.group_8.AuthenticationMicroservice.entities.JwtRefreshTokenEntity;
import cs4337.group_8.AuthenticationMicroservice.repositories.JwtRefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private final String secretKey;
    private final long jwtExpiration;
    private final long jwtRefreshExpiration;

    private final JwtRefreshTokenRepository jwtRefreshTokenRepository;

    public JwtService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.expiration}") long jwtExpiration,
                      @Value("${jwt.refresh.expiration}") long jwtRefreshExpiration,
                      JwtRefreshTokenRepository jwtRefreshTokenRepository) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.jwtRefreshExpiration = jwtRefreshExpiration;
        this.jwtRefreshTokenRepository = jwtRefreshTokenRepository;
    }


    public String generateToken(UserDetails userDetails, String accessToken) {
        assert userDetails != null;
        HashMap<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("access_token", accessToken);
        return generateToken(extraClaims, userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        assert userDetails != null;
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        String refreshToken = buildToken(new HashMap<>(), userDetails, jwtRefreshExpiration);
        JwtRefreshTokenEntity refreshTokenEntity = new JwtRefreshTokenEntity();
        refreshTokenEntity.setUserId(Integer.parseInt(userDetails.getUsername()));
        refreshTokenEntity.setRefreshToken(refreshToken);
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(jwtRefreshExpiration));
        jwtRefreshTokenRepository.save(refreshTokenEntity);
        return refreshToken;
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenSignatureValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String refreshJwtToken(String jwtToken) {
        Claims claims = extractAllClaims(jwtToken);

        String accessToken = (String) claims.get("access_token");
        String userId = (String) claims.get("userId");
        JwtRefreshTokenEntity refreshTokenEntity = jwtRefreshTokenRepository.findByUserId(Integer.parseInt(userId))
                .orElseThrow(() -> new JwtException("Refresh token not found"));

        String refreshJwtToken = generateToken(new org.springframework.security.core.userdetails.User(
                userId,
                "",
                List.of(new SimpleGrantedAuthority("USER"))
        ), accessToken);

        return refreshJwtToken;
    }


    public boolean isTokenExpired(String token) {
        assert token != null;
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        Instant now = Instant.now();
        Instant expiry = now.plusMillis(expiration);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}