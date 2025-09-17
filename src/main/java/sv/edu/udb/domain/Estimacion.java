package sv.edu.udb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "estimacion")
@Getter @Setter @NoArgsConstructor
public class Estimacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "medicion_id", nullable = false)
    private Medicion medicion;

    @NotNull @DecimalMin("0.0")
    @Column(name = "biomasa_kg", nullable = false)
    private Double biomasaKg;

    @NotNull @DecimalMin("0.0")
    @Column(name = "carbono_kg", nullable = false)
    private Double carbonoKg;

    @NotNull @DecimalMin("0.0")
    @Column(name = "co2e_kg", nullable = false)
    private Double co2eKg;

    @NotNull @DecimalMin("0.0")
    @Column(name = "fraccion_carbono", nullable = false)
    private Double fraccionCarbono;

    @Column(name = "incertidumbre_porc")
    private Double incertidumbrePorc;

    @Column(name = "notas", length = 500)
    private String notas;
}
