package sv.edu.udb.repository.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "medicion")
@Getter @Setter @NoArgsConstructor
public class Medicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "arbol_id", nullable = false)
    private Arbol arbol;

    @NotNull @PastOrPresent
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @DecimalMin("0.01")
    @Column(name = "dbh_cm", nullable = false)
    private Double dbhCm;

    @DecimalMin("0.0")
    @Column(name = "altura_m", nullable = false)
    private Double alturaM = 0.0;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}
