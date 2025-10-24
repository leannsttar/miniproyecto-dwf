package sv.edu.udb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.repository.domain.Arbol;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Medicion;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.MedicionRepository;
import sv.edu.udb.repository.ParqueRepository;

import java.util.Map;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EstimacionControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired ArbolRepository arbolRepo;
    @Autowired MedicionRepository medicionRepo;

    private Medicion mkMedicion() {
        Parque p = new Parque();
        p.setNombre("P-" + System.nanoTime());
        p.setDistrito("D");
        p.setAreaHa(1.0);
        parqueRepo.save(p);

        Especie e = new Especie();
        e.setNombreCientifico("Cedrela_" + java.util.UUID.randomUUID());
        e.setDensidadMaderaRho(new java.math.BigDecimal("0.60"));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        especieRepo.save(e);

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
    @DisplayName("POST /api/estimaciones/desde-medicion - ok y 404 si medición no existe")
    void createDesdeMedicion_ok_and_404() throws Exception {
        Medicion m = mkMedicion();

        // OK
        mvc.perform(post("/api/estimaciones/desde-medicion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicionId", m.getId(), "fraccionCarbono", 0.47))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.medicionId").value(m.getId()));

        // 404
        mvc.perform(post("/api/estimaciones/desde-medicion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicionId", 9999, "fraccionCarbono", 0.47))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /api/estimaciones - lista y GET por id")
    void list_and_get() throws Exception {
        Medicion m = mkMedicion();

        var res = mvc.perform(post("/api/estimaciones/desde-medicion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicionId", m.getId(), "fraccionCarbono", 0.47))))
                .andExpect(status().isCreated())
                .andReturn();

        // Parsear ID desde el JSON
        String json = res.getResponse().getContentAsString();
        long id = om.readTree(json).get("id").asLong();

        mvc.perform(get("/api/estimaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(org.hamcrest.Matchers.empty())));

        mvc.perform(get("/api/estimaciones/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.medicionId").value(m.getId()));
    }

    @Test
    @DisplayName("DELETE /api/estimaciones/{id} - ok y luego 404")
    void delete_ok_then_404() throws Exception {
        Medicion m = mkMedicion();

        var res = mvc.perform(post("/api/estimaciones/desde-medicion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("medicionId", m.getId(), "fraccionCarbono", 0.47))))
                .andExpect(status().isCreated())
                .andReturn();

        String json = res.getResponse().getContentAsString();
        long id = om.readTree(json).get("id").asLong();

        mvc.perform(delete("/api/estimaciones/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/estimaciones/{id}", id)).andExpect(status().isNotFound());
    }
}
