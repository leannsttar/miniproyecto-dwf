package sv.edu.udb.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 3. Define el esquema de seguridad que se usará
@SecurityScheme(
        name = "bearerAuth", // Un nombre interno para referenciarlo
        type = SecuritySchemeType.HTTP, // El tipo de seguridad
        scheme = "bearer", // El esquema a usar (Bearer Token)
        bearerFormat = "JWT" // Un hint para la UI sobre el formato del token
)
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



}
