package sv.edu.udb.repository.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "parque")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @NotBlank
    @Column(name = "distrito", nullable = false, length = 120)
    private String distrito;

    @DecimalMin("0.0001")
    @Column(name = "area_ha", nullable = false)
    private Double areaHa;

    @DecimalMin(value = "-90.0") @DecimalMax(value = "90.0")
    @Column(name = "lat")
    private Double lat;

    @DecimalMin(value = "-180.0") @DecimalMax(value = "180.0")
    @Column(name = "lon")
    private Double lon;

    @Builder.Default
    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn = Instant.now();
}
