package sv.edu.udb.service.implementation;



import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sv.edu.udb.repository.domain.User;


import java.util.Collection;
import java.util.List;
import java.util.Objects;


@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDetailsImpl implements UserDetails {
    Long id;
    String email;
    String password;
    @Builder.Default Collection<? extends GrantedAuthority> authorities = List.of();


    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }


    public static UserDetailsImpl from(User user) { return from(user, List.of("USER")); }


    public static UserDetailsImpl from(User user, Collection<String> roles) {
        List<SimpleGrantedAuthority> auths =
                (roles == null ? List.<String>of() : roles).stream()
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                        .distinct()
                        .map(SimpleGrantedAuthority::new)
                        .toList();


        return UserDetailsImpl.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(auths)
                .build();
        }
}