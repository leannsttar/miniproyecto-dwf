package sv.edu.udb.service;

import java.util.Collection;
import java.util.List;

public interface JwtService {
    String generateToken(String subject, Collection<String> roles);

    String createToken(String subject, long ttlSeconds, List<String> roles);

    boolean validate(String token);

    String getSubject(String token);

    @SuppressWarnings("unchecked")
    List<String> getRoles(String token);
}
