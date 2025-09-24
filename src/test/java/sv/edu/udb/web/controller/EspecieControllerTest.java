package sv.edu.udb.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * EspecieController:
 * - POST ok y 400
 * - GET list y get/{id}, 404 cuando no existe
 * - PUT ok y 404
 * - DELETE ok y 404
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EspecieControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test @DisplayName("POST /api/especies - crea especie válida")
    void create_ok() throws Exception {
        var body = Map.of(
                "nombre_cientifico", "Tabebuia rosea",
                "nombre_comun", "Roble de sabana",
                "densidad_madera_rho", new BigDecimal("0.65"),
                "fuente_rho", "FAO 2022",
                "version_rho", "v1"
        );
        mvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre_cientifico").value("Tabebuia rosea"))
                .andExpect(jsonPath("$.id").exists());
        // Nota: no verificamos header Location porque el controlador no lo envía
    }

    @Test @DisplayName("POST /api/especies - validaciones 400 (NotBlank y rango rho)")
    void create_400() throws Exception {
        var invalid = Map.of(
                "nombre_cientifico", "",
                "densidad_madera_rho", new BigDecimal("0.05"),
                "fuente_rho", "",
                "version_rho", ""
        );
        mvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test @DisplayName("GET /api/especies - lista contiene elementos")
    void list_ok() throws Exception {
        var body = Map.of(
                "nombre_cientifico", "Cedrela odorata",
                "densidad_madera_rho", new BigDecimal("0.52"),
                "fuente_rho", "FAO",
                "version_rho", "v1"
        );
        mvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/especies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre_cientifico").exists())
                .andExpect(jsonPath("$", not(org.hamcrest.Matchers.empty())));
    }

    @Test @DisplayName("GET /api/especies/{id} - 404 si no existe")
    void get_404() throws Exception {
        mvc.perform(get("/api/especies/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("PUT /api/especies/{id} - actualiza y retorna 200")
    void update_ok() throws Exception {
        // Crear primero y leer id del body
        var body = Map.of(
                "nombre_cientifico", "Dalbergia retusa",
                "densidad_madera_rho", new BigDecimal("0.95"),
                "fuente_rho", "FAO",
                "version_rho", "v1"
        );
        var createRes = mvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode created = om.readTree(createRes.getResponse().getContentAsString());
        long id = created.get("id").asLong();

        var patch = Map.of(
                "nombre_cientifico", "Dalbergia retusa (upd)",
                "densidad_madera_rho", new BigDecimal("0.90"),
                "fuente_rho", "FAO-upd",
                "version_rho", "v2"
        );
        mvc.perform(put("/api/especies/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre_cientifico").value("Dalbergia retusa (upd)"))
                .andExpect(jsonPath("$.fuente_rho").value("FAO-upd"));
    }

    @Test @DisplayName("PUT /api/especies/{id} - 404 si no existe")
    void update_404() throws Exception {
        var patch = Map.of(
                "nombre_cientifico", "X",
                "densidad_madera_rho", new BigDecimal("0.6"),
                "fuente_rho", "FAO",
                "version_rho", "v1"
        );
        mvc.perform(put("/api/especies/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    @Test @DisplayName("DELETE /api/especies/{id} - 204 y 404 si repito")
    void delete_ok_then_404() throws Exception {
        var body = Map.of(
                "nombre_cientifico", "ToDelete",
                "densidad_madera_rho", new BigDecimal("0.6"),
                "fuente_rho", "FAO",
                "version_rho", "v1"
        );
        var createRes = mvc.perform(post("/api/especies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        long id = om.readTree(createRes.getResponse().getContentAsString()).get("id").asLong();

        mvc.perform(delete("/api/especies/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(delete("/api/especies/{id}", id))
                .andExpect(status().isNotFound());
    }
}
