package sv.edu.udb.service.implementation;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sv.edu.udb.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret}")
    private String secret;

    @Value("${security.jwt.ttl-seconds}")
    private long ttlSeconds;

    private Key key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    @Override
    public String generateToken(String subject, Collection<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + ttlSeconds * 1000);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("roles", roles == null ? List.of("USER") : roles)
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public String createToken(String subject, long ttlSeconds, List<String> roles) {
        return "";
    }
    @Override
    public boolean validate(String token) {
        try {
            parse(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    @Override
    public String getSubject(String token) {
        return parse(token).getSubject();
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<String> getRoles(String token) {
        Object claim = parse(token).get("roles");
        if (claim instanceof Collection<?> c) {
            return c.stream().filter(Objects::nonNull).map(Object::toString).toList();
        }
        return List.of("USER");
    }

    private Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}