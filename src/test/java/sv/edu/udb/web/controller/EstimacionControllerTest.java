package sv.edu.udb.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.domain.Arbol;
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Medicion;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.repository.ParqueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * EstimacionController:
 * - POST manual ok y 400
 * - POST desde medición ok y 404 si mId inexistente
 * - GET list / get/{id}
 * - DELETE ok y 404
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EstimacionControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    ParqueRepository parqueRepo;
    @Autowired
    EspecieRepository especieRepo;
    @Autowired
    ArbolRepository arbolRepo;
    @Autowired
    MedicionRepository medicionRepo;

    private Medicion mkMedicion() {
        // Parque "genérico"
        Parque p = new Parque();
        p.setNombre("P-" + System.nanoTime()); // también único por si acaso
        p.setDistrito("D");
        p.setAreaHa(1.0);
        parqueRepo.save(p);

        // Especie con nombre único para no violar la restricción UNIQUE(nombre_cientifico)
        Especie e = new Especie();
        e.setNombreCientifico("Cedrela_" + java.util.UUID.randomUUID()); // ← único
        e.setDensidadMaderaRho(new java.math.BigDecimal("0.60"));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        especieRepo.save(e);

        // Árbol y medición baseø
        Arbol a = new Arbol();
        a.setParque(p);
        a.setEspecie(e);
        arbolRepo.save(a);

        Medicion m = new Medicion();
        m.setArbol(a);
        m.setFecha(java.time.LocalDate.of(2025, 1, 1));
        m.setDbhCm(30.0);
        m.setAlturaM(15.0);
        return medicionRepo.save(m);
    }


    @Test
    @DisplayName("POST /api/estimaciones - manual ok")
    void createManual_ok() throws Exception {
        Medicion m = mkMedicion();
        var body = Map.of(
                "medicion_id", m.getId(),
                "biomasa_kg", 100.0,
                "carbono_kg", 47.0,
                "co2e_kg", 172.33,
                "fraccion_carbono", 0.47
        );
        mvc.perform(post("/api/estimaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.medicion_id").value(m.getId()))
                .andExpect(jsonPath("$.carbono_kg").value(47.0));
    }

    @Test
    @DisplayName("POST /api/estimaciones - manual 400 por valores inválidos")
    void createManual_400() throws Exception {
        var invalid = Map.of(
                "medicion_id", 1,
                "biomasa_kg", -1.0,
                "carbono_kg", -1.0,
                "co2e_kg", -1.0,
                "fraccion_carbono", -0.1
        );
        mvc.perform(post("/api/estimaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/estimaciones/desde-medicion - ok y 404 si medición no existe")
    void createDesdeMedicion_ok_and_404() throws Exception {
        Medicion m = mkMedicion();

        // OK
        mvc.perform(post("/api/estimaciones/desde-medicion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicion_id", m.getId(), "fraccion_carbono", 0.47))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.medicion_id").value(m.getId()));

        // 404
        mvc.perform(post("/api/estimaciones/desde-medicion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicion_id", 9999, "fraccion_carbono", 0.47))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/estimaciones - lista y GET por id")
    void list_and_get() throws Exception {
        Medicion m = mkMedicion();
        var res = mvc.perform(post("/api/estimaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicion_id", m.getId(), "biomasa_kg", 10.0, "carbono_kg", 4.7, "co2e_kg", 17.233, "fraccion_carbono", 0.47))))
                .andReturn();
        String id = res.getResponse().getHeader("Location").replace("/api/estimaciones/", "");

        mvc.perform(get("/api/estimaciones")).andExpect(status().isOk())
                .andExpect(jsonPath("$", not(org.hamcrest.Matchers.empty())));

        mvc.perform(get("/api/estimaciones/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Long.parseLong(id)));
    }

    @Test
    @DisplayName("DELETE /api/estimaciones/{id} - ok y luego 404")
    void delete_ok_then_404() throws Exception {
        Medicion m = mkMedicion();
        var res = mvc.perform(post("/api/estimaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicion_id", m.getId(), "biomasa_kg", 10.0, "carbono_kg", 4.7, "co2e_kg", 17.233, "fraccion_carbono", 0.47))))
                .andReturn();
        String id = res.getResponse().getHeader("Location").replace("/api/estimaciones/", "");

        mvc.perform(delete("/api/estimaciones/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/estimaciones/{id}", id)).andExpect(status().isNotFound());
    }
}
