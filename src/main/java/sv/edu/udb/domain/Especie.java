package sv.edu.udb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "especie", uniqueConstraints = @UniqueConstraint(columnNames = "nombre_cientifico"))
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Especie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "nombre_cientifico", nullable = false, length = 160)
    private String nombreCientifico;

    @Column(name = "nombre_comun", length = 160)
    private String nombreComun;

    @DecimalMin("0.10")
    @DecimalMax("1.50")
    @Column(name = "densidad_madera_rho", precision = 4, scale = 3, nullable = false)
    private BigDecimal densidadMaderaRho;

    @NotBlank
    @Column(name = "fuente_rho", length = 120, nullable = false)
    private String fuenteRho;

    @NotBlank
    @Column(name = "version_rho", length = 60, nullable = false)
    private String versionRho;
}
