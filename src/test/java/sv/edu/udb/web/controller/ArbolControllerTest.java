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
import sv.edu.udb.domain.Especie;
import sv.edu.udb.domain.Parque;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ArbolController:
 * - POST ok y 400 por faltantes
 * - GET list, GET id, 404 id inexistente
 * - PUT cambio de especie y coords
 * - DELETE ok y 404
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ArbolControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    ParqueRepository parqueRepo;
    @Autowired
    EspecieRepository especieRepo;

    private Parque mkParque() {
        Parque p = new Parque();
        p.setNombre("P-" + UUID.randomUUID().toString().substring(0, 8)); // nombre único
        p.setDistrito("D");
        p.setAreaHa(1.0);
        return parqueRepo.save(p);
    }

    private Especie mkEspecieUnique(String base, String rho) {
        Especie e = new Especie();
        e.setNombreCientifico(base + "_" + UUID.randomUUID().toString().substring(0, 8)); // nombre científico único
        e.setDensidadMaderaRho(new BigDecimal(rho));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        return especieRepo.save(e);
    }

    @Test
    @DisplayName("POST /api/arboles - crea ok")
    void create_ok() throws Exception {
        var p = mkParque();
        var e = mkEspecieUnique("Cedrela", "0.52");

        var body = Map.of("parque_id", p.getId(), "especie_id", e.getId(), "lat", 13.7, "lon", -89.2);
        mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parque_id").value(p.getId()))
                .andExpect(jsonPath("$.especie_id").value(e.getId()));
    }

    @Test
    @DisplayName("POST /api/arboles - 400 si faltan refs")
    void create_400_missingRefs() throws Exception {
        var body = Map.of("lat", 13.7, "lon", -89.2);
        mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/arboles - lista no vacía")
    void list_ok() throws Exception {
        var p = mkParque();
        var e = mkEspecieUnique("Cedrela", "0.52");
        var body = Map.of("parque_id", p.getId(), "especie_id", e.getId());
        mvc.perform(post("/api/arboles").contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsString(body)))
                .andExpect(status().isCreated());

        mvc.perform(get("/api/arboles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(org.hamcrest.Matchers.empty())));
    }

    @Test
    @DisplayName("GET /api/arboles/{id} - 404 si no existe")
    void get_404() throws Exception {
        mvc.perform(get("/api/arboles/{id}", 9999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /api/arboles/{id} - cambia especie y coords")
    void update_ok() throws Exception {
        var p = mkParque();
        var e1 = mkEspecieUnique("Cedrela", "0.52");
        var e2 = mkEspecieUnique("Tabebuia_rosea", "0.65");

        var created = mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of(
                                "parque_id", p.getId(),
                                "especie_id", e1.getId(),
                                "lat", 1.0, "lon", 2.0))))
                .andReturn();
        String id = created.getResponse().getHeader("Location").replace("/api/arboles/", "");

        // 🔧 Incluir parque_id para cumplir @NotNull del DTO
        var patch = Map.of(
                "parque_id", p.getId(),
                "especie_id", e2.getId(),
                "lat", 9.0,
                "lon", 8.0
        );

        mvc.perform(put("/api/arboles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especie_id").value(e2.getId()))
                .andExpect(jsonPath("$.lat").value(9.0))
                .andExpect(jsonPath("$.lon").value(8.0));
    }


    @Test
    @DisplayName("DELETE /api/arboles/{id} - ok y luego 404")
    void delete_ok_then_404() throws Exception {
        var p = mkParque();
        var e = mkEspecieUnique("X", "0.60");
        var res = mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("parque_id", p.getId(), "especie_id", e.getId()))))
                .andReturn();
        String id = res.getResponse().getHeader("Location").replace("/api/arboles/", "");

        mvc.perform(delete("/api/arboles/{id}", id)).andExpect(status().isNoContent());
        mvc.perform(delete("/api/arboles/{id}", id)).andExpect(status().isNotFound());
    }
}
