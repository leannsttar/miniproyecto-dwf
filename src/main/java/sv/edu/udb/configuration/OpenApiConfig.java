package sv.edu.udb.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("API – Línea base 2025 Carbono Urbano Soyapango")
                        .version("v1")
                        .description("""
                    Estima biomasa con Chave 2014 (con/sin altura), 
                    C = AGB × 0.47, CO₂e = C × (44/12),
                    y genera resultados agregados por parque/año 
                    (captura referencial: 0.28 kg C/m²/año).
                """));
    }
    @Bean
    GroupedOpenApi catalogos() {
        return GroupedOpenApi.builder()
                .group("Catálogos")
                .pathsToMatch("/api/parques/**", "/api/especies/**")
                .build();
    }

    @Bean
    GroupedOpenApi muestreo() {
        return GroupedOpenApi.builder()
                .group("Muestreo")
                .pathsToMatch("/api/arboles/**", "/api/mediciones/**")
                .build();
    }

    @Bean
    GroupedOpenApi calculoResultados() {
        return GroupedOpenApi.builder()
                .group("Cálculo & Resultados")
                .pathsToMatch("/api/estimaciones/**", "/api/resultados/**")
                .build();
    }




}
