package sv.edu.udb.web.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class MedicionRequest {
    @NotNull private Long arbolId;
    @NotNull @PastOrPresent private LocalDate fecha;
    @NotNull @DecimalMin("0.01") private Double dbhCm;
    @NotNull @DecimalMin("0.0")  private Double alturaM;
    private String observaciones;
}
