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
import sv.edu.udb.repository.ParqueRepository;
import sv.edu.udb.repository.domain.Parque;

import java.util.Map;

import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete; // (no usado pero handy)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ResultadoParqueControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @Autowired ParqueRepository parqueRepo;

    private Long mkParque(double areaHa) {
        Parque p = new Parque();
        p.setNombre("P-" + System.nanoTime());
        p.setDistrito("D");
        p.setAreaHa(areaHa);
        return parqueRepo.save(p).getId();
    }

    @Test
    @DisplayName("POST /api/resultados/recalcular - ok y 404 si parque no existe")
    void recalcular_ok_and_404() throws Exception {
        Long parqueId = mkParque(2.0);

        // OK
        mvc.perform(post("/api/resultados/recalcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("parqueId", parqueId, "anio", 2025))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parqueId").value(parqueId))
                .andExpect(jsonPath("$.anio").value(2025))
                .andExpect(jsonPath("$.capturaAnualT").exists());

        // 404
        mvc.perform(post("/api/resultados/recalcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("parqueId", 999999, "anio", 2025))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GETs de lectura: listAll, listByParque, getByParqueAndAnio")
    void reads_ok() throws Exception {
        Long p1 = mkParque(1.0);
        Long p2 = mkParque(3.0);

        // Precalcular algunos
        mvc.perform(post("/api/resultados/recalcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("parqueId", p1, "anio", 2025))))
                .andExpect(status().isOk());

        mvc.perform(post("/api/resultados/recalcular")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(Map.of("parqueId", p2, "anio", 2024))))
                .andExpect(status().isOk());

        mvc.perform(get("/api/resultados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(org.hamcrest.Matchers.empty())));

        mvc.perform(get("/api/resultados/parque/{parqueId}", p1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].parqueId").value(p1));

        mvc.perform(get("/api/resultados/{p}/{a}", p2, 2024))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.parqueId").value(p2))
                .andExpect(jsonPath("$.anio").value(2024));
    }
}
