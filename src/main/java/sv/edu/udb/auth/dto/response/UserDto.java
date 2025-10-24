package sv.edu.udb.auth.dto.response;

import lombok.Value;
import sv.edu.udb.repository.domain.User; // o la ruta de tu entidad

@Value
public class UserDto {
    Long id;
    String firstname;
    String lastname;
    String email;
    String username;

    public UserDto(User u) {
        this.id = u.getId();
        this.firstname = u.getFirstname();
        this.lastname = u.getLastname();
        this.email = u.getEmail();
        this.username = u.getUsername();
    }
}
