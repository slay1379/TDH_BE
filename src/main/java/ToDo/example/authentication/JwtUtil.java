package ToDo.example.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class JwtUtil {
    private final String SECRET_KEY = Optional.ofNullable(System.getenv("SECRET_KEY"))
            .orElseThrow(() -> new IllegalStateException("SECRET_KEY 환경 변수가 설정되지 않았습니다."));

    private final String REFRESH_SECRET_KEY = Optional.ofNullable(System.getenv("REFRESH_SECRET_KEY"))
            .orElseThrow(() -> new IllegalStateException("REFRESH_SECRET_KEY 환경 변수가 설정되지 않았습니다."));


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(String username) {
        return createToken(new HashMap<>(), username, SECRET_KEY, 1000 * 60 * 60); // 1-hour expiration
    }

    public String generatePasswordResetToken(String email) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private String createToken(Map<String, Object> claims, String subject, String secretKey, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isRefreshTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractRefreshUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            System.err.println("유효하지 않은 서명입니다: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("토큰이 만료되었습니다: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("토큰 유효성 검사 중 문제가 발생했습니다: " + e.getMessage());
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(REFRESH_SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Refresh 토큰 유효성 검사 중 문제가 발생했습니다: " + e.getMessage());
            throw new SecurityException("Refresh JWT validation error");
        }
    }

}
