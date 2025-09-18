package sv.edu.udb.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ParqueRequest {
    @NotBlank private String nombre;
    @NotBlank private String distrito;
    @NotNull  @DecimalMin("0.0001") private Double areaHa;
    @DecimalMin(value="-90.0")  @DecimalMax(value="90.0")   private Double lat;
    @DecimalMin(value="-180.0") @DecimalMax(value="180.0")  private Double lon;
}
