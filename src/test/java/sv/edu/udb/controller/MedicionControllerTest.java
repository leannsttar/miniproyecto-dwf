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
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MedicionControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;
    @Autowired ArbolRepository arbolRepo;

    private Arbol mkArbol() {
        Parque p = new Parque();
        p.setNombre("P-" + UUID.randomUUID().toString().substring(0, 8));
        p.setDistrito("D");
        p.setAreaHa(1.0);
        parqueRepo.save(p);

        Especie e = new Especie();
        e.setNombreCientifico("Cedrela_" + UUID.randomUUID().toString().substring(0, 8));
        e.setDensidadMaderaRho(new BigDecimal("0.52"));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        especieRepo.save(e);

        Arbol a = new Arbol();
        a.setParque(p);
        a.setEspecie(e);
        return arbolRepo.save(a);
    }

    @Test @DisplayName("POST /api/mediciones - crea ok")
    void create_ok() throws Exception {
        Arbol a = mkArbol();
        var body = Map.of("arbolId", a.getId(), "fecha", "2025-01-01", "dbhCm", 30.0, "alturaM", 15.0, "observaciones", "OK");

        mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.arbolId").value(a.getId()))
                .andExpect(jsonPath("$.dbhCm").value(30.0));
    }

    @Test @DisplayName("POST /api/mediciones - 400 por validaciones")
    void create_400() throws Exception {
        var invalid = Map.of("fecha", LocalDate.now().plusDays(1).toString());
        mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("GET /api/mediciones - lista no vacía")
    void list_ok() throws Exception {
        Arbol a = mkArbol();
        mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("arbolId", a.getId(), "fecha", "2025-01-01", "dbhCm", 10.0, "alturaM", 5.0))))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/mediciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test @DisplayName("GET /api/mediciones/{id} - 404 si no existe")
    void get_404() throws Exception {
        mvc.perform(get("/api/mediciones/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/mediciones/{id} - actualiza valores")
    void update_ok() throws Exception {
        Arbol a = mkArbol();

        var createdResponse = mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("arbolId", a.getId(), "fecha", "2025-01-01", "dbhCm", 10.0, "alturaM", 5.0))))
                .andReturn();

        var createdJson = om.readTree(createdResponse.getResponse().getContentAsString());
        Long id = createdJson.get("id").asLong();

        var patch = Map.of("arbolId", a.getId(), "fecha", "2025-01-10", "dbhCm", 12.0, "alturaM", 6.0);
        mvc.perform(put("/api/mediciones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dbhCm").value(12.0))
                .andExpect(jsonPath("$.alturaM").value(6.0));
    }

    @Test @DisplayName("DELETE /api/mediciones/{id} - ok y luego 404")
    void delete_ok_then_404() throws Exception {
        Arbol a = mkArbol();

        var createdResponse = mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("arbolId", a.getId(), "fecha", "2025-01-01", "dbhCm", 10.0, "alturaM", 5.0))))
                .andReturn();

        var createdJson = om.readTree(createdResponse.getResponse().getContentAsString());
        Long id = createdJson.get("id").asLong();

        mvc.perform(delete("/api/mediciones/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/mediciones/{id}", id)).andExpect(status().isNotFound());
    }
}
