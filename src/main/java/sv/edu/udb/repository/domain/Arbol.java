package sv.edu.udb.repository.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "arbol")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Arbol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "parque_id", nullable = false)
    private Parque parque;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "especie_id", nullable = false)
    private Especie especie;

    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    @Column(name = "lat")
    private Double lat;

    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @Column(name = "lon")
    private Double lon;

    @Builder.Default
    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn = Instant.now();
}
