package sv.edu.udb.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "resultado_parque",
        uniqueConstraints = @UniqueConstraint(columnNames = {"parque_id", "anio"}))
@Getter @Setter @NoArgsConstructor
public class ResultadoParque {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "parque_id", nullable = false)
    private Parque parque;

    @Column(name = "anio", nullable = false)
    private int anio;

    @NotNull @DecimalMin("0.0")
    @Column(name = "stock_carbono_t", nullable = false)
    private Double stockCarbonoT;

    @NotNull @DecimalMin("0.0")
    @Column(name = "captura_anual_t", nullable = false)
    private Double capturaAnualT;

//    @Column(name = "top_especies_json", columnDefinition = "TEXT")
//    private String topEspeciesJson;
//
//    @Column(name = "rango_incertidumbre_porc")
//    private Double rangoIncertidumbrePorc;
}
