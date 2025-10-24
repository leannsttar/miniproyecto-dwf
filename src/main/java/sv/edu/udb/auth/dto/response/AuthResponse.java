package sv.edu.udb.auth.dto.response;



import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class AuthResponse {
    String accessToken;
    @Builder.Default String tokenType = "Bearer";

}
