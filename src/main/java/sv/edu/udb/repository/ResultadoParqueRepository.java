package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.domain.ResultadoParque;

import java.util.List;
import java.util.Optional;

public interface ResultadoParqueRepository extends JpaRepository<ResultadoParque, Long> {

    Optional<ResultadoParque> findByParque_IdAndAnio(Long parqueId, int anio);

    List<ResultadoParque> findByParque_Id(Long parqueId);

    Optional<ResultadoParque> findTopByParque_IdOrderByAnioDesc(Long parqueId);
}