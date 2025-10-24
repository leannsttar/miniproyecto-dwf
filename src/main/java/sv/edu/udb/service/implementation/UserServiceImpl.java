package sv.edu.udb.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.auth.dto.request.RegisterRequest;
import sv.edu.udb.auth.dto.response.AuthResponse;
import sv.edu.udb.auth.dto.request.AuthRequest; // ajusta el paquete
import sv.edu.udb.repository.UserRepository;
import sv.edu.udb.repository.domain.User;
import sv.edu.udb.service.JwtService;
import sv.edu.udb.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // <-- importante

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmailIgnoreCase(req.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        if (userRepository.existsByUsernameIgnoreCase(req.getUsername())) {
            throw new IllegalArgumentException("El username ya está en uso");
        }

        var user = new User();
        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.save(user);

        var token = jwtService.generateToken(user.getEmail(), List.of("USER"));
        return AuthResponse.builder().accessToken(token).build();
    }

    @Override
    public AuthResponse login(AuthRequest req) {
        // Si tu AuthRequest trae email, úsalo; si trae username, úsalo como username.
        String email = req.getEmail();

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, req.getPassword())
        );

        UserDetails principal = (UserDetails) auth.getPrincipal();
        var token = jwtService.generateToken(principal.getUsername(), List.of("USER"));

        return AuthResponse.builder().accessToken(token).build();
    }
}
