package sv.edu.udb.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.ParqueRepository;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests de integración del ParqueController:
 * - POST crea parque válido
 * - POST valida campos (400)
 * - PUT actualiza
 * - GET lista y GET por id
 * - DELETE elimina y responde 204
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ParqueControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ParqueRepository parqueRepo;

    @Test @DisplayName("POST /api/parques - crea parque válido")
    void create_ok() throws Exception {
        var body = Map.of(
                "nombre", "Parque Cuscatlán",
                "distrito", "San Salvador Centro",
                "areaHa", 12.5,
                "lat", 13.698,
                "lon", -89.191
        );

        mvc.perform(post("/api/parques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Parque Cuscatlán"))
                .andExpect(jsonPath("$.areaHa").value(12.5));
    }

    @Test @DisplayName("POST /api/parques - validaciones 400 (NotBlank y área > 0)")
    void create_validations() throws Exception {
        var invalid = Map.of(
                "nombre", "",
                "distrito", "",
                "area_ha", 0.0
        );

        mvc.perform(post("/api/parques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("PUT /api/parques/{id} - actualiza nombre/distrito/área")
    void update_ok() throws Exception {
        // arrange: crear uno directo por repo
        Parque p = new Parque(); p.setNombre("P"); p.setDistrito("D"); p.setAreaHa(1.0);
        p = parqueRepo.save(p);

        var patch = Map.of(
                "nombre", "Parque Actualizado",
                "distrito", "Distrito 2",
                "areaHa", 5.0
        );

        mvc.perform(put("/api/parques/{id}", p.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Parque Actualizado"))
                .andExpect(jsonPath("$.distrito").value("Distrito 2"))
                .andExpect(jsonPath("$.areaHa").value(5.0));
    }

    @Test @DisplayName("GET /api/parques - lista contiene elementos")
    void list_contains() throws Exception {
        Parque p = new Parque(); p.setNombre("PX"); p.setDistrito("DX"); p.setAreaHa(2.0);
        parqueRepo.save(p);

        mvc.perform(get("/api/parques"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test @DisplayName("GET /api/parques/{id} - devuelve por id")
    void get_byId() throws Exception {
        Parque p = new Parque(); p.setNombre("PGet"); p.setDistrito("DGet"); p.setAreaHa(3.0);
        p = parqueRepo.save(p);

        mvc.perform(get("/api/parques/{id}", p.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("PGet"));
    }

    @Test @DisplayName("DELETE /api/parques/{id} - elimina y retorna 204, 404 si se repitio")
    void delete_ok() throws Exception {
        Parque p = new Parque(); p.setNombre("PDel"); p.setDistrito("DDel"); p.setAreaHa(1.0);
        p = parqueRepo.save(p);

        mvc.perform(delete("/api/parques/{id}", p.getId()))
                .andExpect(status().isNoContent());

        mvc.perform(delete("/api/parques/{id}", p.getId()))
                .andExpect(status().isNotFound());
    }
}
