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
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ArbolRepository;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MedicionController:
 * - POST ok y 400 por validaciones
 * - GET list y get/{id} (404)
 * - PUT actualiza valores
 * - DELETE ok y 404
 */
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
        p.setNombre("P-" + UUID.randomUUID().toString().substring(0, 8)); // único
        p.setDistrito("D");
        p.setAreaHa(1.0);
        parqueRepo.save(p);

        Especie e = new Especie();
        e.setNombreCientifico("Cedrela_" + UUID.randomUUID().toString().substring(0, 8)); // único
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
        var body = Map.of("arbol_id", a.getId(), "fecha", "2025-01-01", "dbh_cm", 30.0, "altura_m", 15.0, "observaciones", "OK");
        mvc.perform(post("/api/mediciones").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.arbol_id").value(a.getId()));
    }

    @Test @DisplayName("POST /api/mediciones - 400 por validaciones (fecha futura, campos null)")
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
                        .content(om.writeValueAsString(Map.of("arbol_id", a.getId(), "fecha", "2025-01-01", "dbh_cm", 10.0, "altura_m", 5.0))))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/mediciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(org.hamcrest.Matchers.empty())));
    }

    @Test @DisplayName("GET /api/mediciones/{id} - 404 si no existe")
    void get_404() throws Exception {
        mvc.perform(get("/api/mediciones/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/mediciones/{id} - actualiza dbh y altura")
    void update_ok() throws Exception {
        Arbol a = mkArbol();
        var created = mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("arbol_id", a.getId(), "fecha", "2025-01-01", "dbh_cm", 10.0, "altura_m", 5.0))))
                .andReturn();
        String id = created.getResponse().getHeader("Location").replace("/api/mediciones/", "");

        var patch = Map.of("arbol_id", a.getId(), "fecha", "2025-01-10", "dbh_cm", 12.0, "altura_m", 6.0);
        mvc.perform(put("/api/mediciones/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dbh_cm").value(12.0))
                .andExpect(jsonPath("$.altura_m").value(6.0));
    }

    @Test @DisplayName("DELETE /api/mediciones/{id} - ok y luego 404")
    void delete_ok_then_404() throws Exception {
        Arbol a = mkArbol();
        var created = mvc.perform(post("/api/mediciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("arbol_id", a.getId(), "fecha", "2025-01-01", "dbh_cm", 10.0, "altura_m", 5.0))))
                .andReturn();
        String id = created.getResponse().getHeader("Location").replace("/api/mediciones/", "");

        mvc.perform(delete("/api/mediciones/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/mediciones/{id}", id)).andExpect(status().isNotFound());
    }
}
