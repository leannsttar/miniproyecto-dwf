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
import org.springframework.transaction.annotation.Transactional;
import sv.edu.udb.repository.domain.Especie;
import sv.edu.udb.repository.domain.Parque;
import sv.edu.udb.repository.EspecieRepository;
import sv.edu.udb.repository.ParqueRepository;

import java.math.BigDecimal;
import java.util.Map;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ArbolControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ParqueRepository parqueRepo;
    @Autowired EspecieRepository especieRepo;

    private Parque crearParque() {
        Parque p = new Parque();
        p.setNombre("Parque Central");
        p.setDistrito("Distrito 1");
        p.setAreaHa(1.0);
        return parqueRepo.save(p);
    }

    private Especie crearEspecie(String nombreCientifico, String rho) {
        Especie e = new Especie();
        e.setNombreCientifico(nombreCientifico);
        e.setDensidadMaderaRho(new BigDecimal(rho));
        e.setFuenteRho("FAO");
        e.setVersionRho("v1");
        return especieRepo.save(e);
    }

    @Test
    @DisplayName("POST /api/arboles - crea ok")
    void create_ok() throws Exception {
        var parque = crearParque();
        var especie = crearEspecie("Cedrela odorata", "0.52");

        var body = Map.of(
                "parqueId", parque.getId(),
                "especieId", especie.getId(),
                "lat", 13.7,
                "lon", -89.2
        );

        mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.parqueId").value(parque.getId()))
                .andExpect(jsonPath("$.especieId").value(especie.getId()))
                .andExpect(jsonPath("$.lat").value(13.7))
                .andExpect(jsonPath("$.lon").value(-89.2));
    }

    @Test
    @DisplayName("POST /api/arboles - 400 si faltan refs requeridas")
    void create_400_missingRefs() throws Exception {
        var body = Map.of("lat", 13.7, "lon", -89.2); // faltan parqueId y especieId

        mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/arboles - lista no vacía")
    void list_ok() throws Exception {
        var parque = crearParque();
        var especie = crearEspecie("Swietenia macrophylla", "0.60");

        // crear uno para asegurar lista no vacía
        mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of(
                                "parqueId", parque.getId(),
                                "especieId", especie.getId(),
                                "lat", 1.0,
                                "lon", 2.0
                        ))))
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
    @DisplayName("PUT /api/arboles/{id} - actualiza especie y coords")
    void update_ok() throws Exception {
        var parque = crearParque();
        var especie1 = crearEspecie("Cedrela odorata", "0.52");
        var especie2 = crearEspecie("Tabebuia rosea", "0.65");

        // crear
        var createRes = mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of(
                                "parqueId", parque.getId(),
                                "especieId", especie1.getId(),
                                "lat", 1.0,
                                "lon", 2.0
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long id = om.readTree(createRes.getResponse().getContentAsString()).get("id").asLong();

        // actualizar (el DTO tiene @NotNull para parqueId/especieId)
        var patch = Map.of(
                "parqueId", parque.getId(),
                "especieId", especie2.getId(),
                "lat", 9.0,
                "lon", 8.0
        );

        mvc.perform(put("/api/arboles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.especieId").value(especie2.getId()))
                .andExpect(jsonPath("$.lat").value(9.0))
                .andExpect(jsonPath("$.lon").value(8.0));
    }

    @Test
    @DisplayName("DELETE /api/arboles/{id} - 204 y luego 404")
    void delete_ok_then_404() throws Exception {
        var parque = crearParque();
        var especie = crearEspecie("Pinus oocarpa", "0.55");

        var res = mvc.perform(post("/api/arboles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of(
                                "parqueId", parque.getId(),
                                "especieId", especie.getId()
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long id = om.readTree(res.getResponse().getContentAsString()).get("id").asLong();

        mvc.perform(delete("/api/arboles/{id}", id))
                .andExpect(status().isNoContent());

        mvc.perform(delete("/api/arboles/{id}", id))
                .andExpect(status().isNotFound());
    }
}
