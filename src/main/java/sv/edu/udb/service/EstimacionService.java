package sv.edu.udb.service;

import sv.edu.udb.repository.domain.Estimacion;
import java.util.List;

public interface EstimacionService {
    List<Estimacion> findAll();
    Estimacion findById(Long id);
    Estimacion create(Estimacion e);
    Estimacion createFromMedicion(Long medicionId, Double fraccionCarbono);
    void delete(Long id);
}
