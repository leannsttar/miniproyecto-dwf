package sv.edu.udb.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank private String firstname;
    @NotBlank private String lastname;
    @Email @NotBlank private String email;
    @NotBlank @Size(min = 8) private String password;
    @NotBlank private String username;
}