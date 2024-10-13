package UtoPlan.UtoPlan.CORS;

import UtoPlan.UtoPlan.DB.Entity.UserEntity;
import UtoPlan.UtoPlan.DB.Repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Autowired
    private UserRepository userRepository;

    private static final String SECRET = "U29tZVNlY3JldEtleVdpdGhTZWxmUmFuZG9tQ2hhcnM="; // Base64 인코딩된 시크릿 키 // 최소 32자 이상

    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));

    // JWT 토큰 생성
    public String generateToken(Long num) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, String.valueOf(num));
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10시간 유효
                .signWith(SECRET_KEY) // 비밀키로 서명
                .compact();
    }

    public Long extractUserId(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT 토큰이 비어 있습니다.");
        }
        System.out.println("Received Token: " + token);

        token = token.trim();

        // Bearer 접두사가 있는 경우 제거
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = extractAllClaims(token);
            System.out.println("Extracting claims from token: " + token);
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            throw new IllegalArgumentException("JWT 토큰 형식이 잘못되었습니다. 올바른 JWT 형식이어야 합니다.", e);
        }
    }


    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("JWT 토큰을 파싱하는 중 오류가 발생했습니다.", e);
        }
    }

    public Claims extractClaimsWithoutValidation(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            token = token.replaceAll("\\s+", "");
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                // 서명 부분이 있더라도 검증하지 않기 위해 서명 제외한 부분만 파싱
                String payload = parts[1];
                return Jwts.parserBuilder().build().parseClaimsJwt(parts[0] + "." + payload + ".").getBody();
            } else {
                throw new IllegalArgumentException("Invalid JWT token format");
            }
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }

    public UserEntity getUserFromToken(String token) {
        Claims claims = extractClaimsWithoutValidation(token);
        Long userNum = Long.parseLong(claims.getSubject()); // 토큰에서 사용자 num 값 추출
        return userRepository.findById(userNum)
                .orElseThrow(() -> new RuntimeException("User not found"));

    }
}
