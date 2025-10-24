package sv.edu.udb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.repository.domain.Parque;

public interface ParqueRepository extends JpaRepository<Parque, Long> { }
