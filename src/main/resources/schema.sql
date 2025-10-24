-- Limpieza segura de tablas (orden inverso a FKs)
DROP TABLE IF EXISTS resultado_parque;
DROP TABLE IF EXISTS estimacion;
DROP TABLE IF EXISTS medicion;
DROP TABLE IF EXISTS arbol;
DROP TABLE IF EXISTS parque;
DROP TABLE IF EXISTS especie;

-- =========================
-- ESPECIE
-- =========================
CREATE TABLE especie
(
    id                  BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre_cientifico   VARCHAR(160)  NOT NULL,
    nombre_comun        VARCHAR(160),
    densidad_madera_rho DECIMAL(3, 2) NOT NULL, -- [0.10, 1.50]
    fuente_rho          VARCHAR(160)  NOT NULL,
    version_rho         VARCHAR(40)   NOT NULL,
    CONSTRAINT uq_especie_nombre_cientifico UNIQUE (nombre_cientifico)
);

-- =========================
-- PARQUE
-- =========================
CREATE TABLE parque
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre    VARCHAR(160) NOT NULL,
    distrito  VARCHAR(160) NOT NULL,
    area_ha DOUBLE NOT NULL,
    lat DOUBLE,
    lon DOUBLE,
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- ARBOL
-- =========================
CREATE TABLE arbol
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    parque_id  BIGINT NOT NULL,
    especie_id BIGINT NOT NULL,
    lat DOUBLE,
    lon DOUBLE,
    creado_en  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_arbol_parque FOREIGN KEY (parque_id) REFERENCES parque (id),
    CONSTRAINT fk_arbol_especie FOREIGN KEY (especie_id) REFERENCES especie (id)
);

-- =========================
-- MEDICION
-- =========================
CREATE TABLE medicion
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT,
    arbol_id      BIGINT NOT NULL,
    fecha         DATE   NOT NULL,
    dbh_cm DOUBLE NOT NULL,   -- >= 0.01
    altura_m DOUBLE NOT NULL, -- >= 0.0
    observaciones VARCHAR(255),
    CONSTRAINT fk_medicion_arbol FOREIGN KEY (arbol_id) REFERENCES arbol (id)
);

-- =========================
-- ESTIMACION
-- =========================
CREATE TABLE estimacion
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    medicion_id BIGINT NOT NULL,
    biomasa_kg DOUBLE NOT NULL,       -- >= 0
    carbono_kg DOUBLE NOT NULL,       -- >= 0
    co2e_kg DOUBLE NOT NULL,          -- >= 0
    fraccion_carbono DOUBLE NOT NULL, -- >= 0
    incertidumbre_porc DOUBLE,
    notas       VARCHAR(500),
    CONSTRAINT fk_estimacion_medicion FOREIGN KEY (medicion_id) REFERENCES medicion (id)
);

-- =========================
-- RESULTADO_PARQUE
-- =========================
CREATE TABLE resultado_parque
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    parque_id BIGINT NOT NULL,
    anio      INT    NOT NULL,
    stock_carbono_t DOUBLE,
    captura_anual_t DOUBLE,
    CONSTRAINT fk_resultado_parque_parque FOREIGN KEY (parque_id) REFERENCES parque (id),
    CONSTRAINT uq_resultado_parque UNIQUE (parque_id, anio)
);

-- Índices útiles
CREATE INDEX idx_arbol_parque ON arbol (parque_id);
CREATE INDEX idx_arbol_especie ON arbol (especie_id);
CREATE INDEX idx_medicion_arbol ON medicion (arbol_id);
CREATE INDEX idx_estimacion_medicion ON estimacion (medicion_id);
CREATE INDEX idx_resultado_parque_anio ON resultado_parque (anio);
