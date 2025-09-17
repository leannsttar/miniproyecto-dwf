package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.domain.Estimacion;

import java.util.List;

public interface EstimacionRepository extends JpaRepository<Estimacion, Long> {
    List<Estimacion> findByMedicion_Arbol_Parque_Id(Long parqueId);
}
