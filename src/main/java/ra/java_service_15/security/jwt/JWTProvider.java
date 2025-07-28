package ra.java_service_15.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean validateToken(String token)  {
        try{
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){
            log.error("JWT token expired!");
        }catch (UnsupportedJwtException e){
            log.error("JWT token unsupported!");
        }catch (MalformedJwtException e){
            log.error("JWT token malformed!");
        }catch (SignatureException e){
            log.error("JWT token signature error!");
        }catch (IllegalArgumentException e){
            log.error("JWT token argument error!");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public String refreshToken(String username,String token) {
        if(validateToken(token) && getUsernameFromToken(token).equals(username)){
            Date now = new Date();
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(new Date(now.getTime() + refreshExpiration))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
        }
        return null;
    }
}
