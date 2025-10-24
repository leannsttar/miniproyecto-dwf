package sv.edu.udb.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.service.implementation.UserDetailsImpl; // tu clase

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user with email: " + email));
        // Usa tu principal personalizado; ajusta roles si tienes otro origen
        return UserDetailsImpl.from(user, List.of("USER"));
    }
}
