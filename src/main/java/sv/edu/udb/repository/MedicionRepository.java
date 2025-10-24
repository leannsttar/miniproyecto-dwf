package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Medicion;

public interface MedicionRepository extends JpaRepository<Medicion, Long> { }
