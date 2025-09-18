-- ===== ESPECIES =====
INSERT INTO especie (nombre_cientifico, nombre_comun, densidad_madera_rho, fuente_rho, version_rho)
VALUES ('Tabebuia rosea', 'Roble de sabana', 0.65, 'FAO 2022', 'v1'),
       ('Dalbergia retusa', 'Cocobolo', 0.95, 'FAO 2022', 'v1'),
       ('Cedrela odorata', 'Cedro', 0.40, 'FAO 2022', 'v1'),
       ('Swietenia macrophylla', 'Caoba', 0.55, 'FAO 2022', 'v1'),
       ('Mangifera indica', 'Mango', 0.60, 'FAO 2022', 'v1');

-- ===== PARQUES =====
-- capturaAnualT = 2.8 * area_ha:
-- P1: 11.73 ha -> 32.844    | P2: 63.40 ha -> 177.52
-- P3:  9.85 ha -> 27.58     | P4: 26.37 ha -> 73.836
-- P5:  4.92 ha -> 13.776
INSERT INTO parque (nombre, distrito, area_ha, lat, lon, creado_en)
VALUES ('Parque Cuscatlán', 'San Salvador Centro', 11.73, 13.6981, -89.1912, CURRENT_TIMESTAMP),  -- id=1
       ('Parque Bicentenario', 'Antiguo Cuscatlán', 63.40, 13.6684, -89.2415, CURRENT_TIMESTAMP), -- id=2
       ('Parque Infantil', 'San Salvador Centro', 9.85, 13.7029, -89.1951, CURRENT_TIMESTAMP),    -- id=3
       ('Parque Satélite', 'Mejicanos', 26.37, 13.7403, -89.2206, CURRENT_TIMESTAMP),             -- id=4
       ('Parque La Vega', 'Soyapango', 4.92, 13.7197, -89.1519, CURRENT_TIMESTAMP);
-- id=5

-- ===== ARBOLES (1 árbol -> 1 medición -> 1 estimación) =====
INSERT INTO arbol (parque_id, especie_id, lat, lon, creado_en)
VALUES (1, 1, 13.6991, -89.1920, CURRENT_TIMESTAMP), -- id=1  P1
       (1, 3, 13.7002, -89.1934, CURRENT_TIMESTAMP), -- id=2  P1
       (1, 4, 13.7016, -89.1946, CURRENT_TIMESTAMP), -- id=3  P1
       (2, 2, 13.6703, -89.2421, CURRENT_TIMESTAMP), -- id=4  P2
       (2, 4, 13.6712, -89.2430, CURRENT_TIMESTAMP), -- id=5  P2
       (2, 5, 13.6724, -89.2442, CURRENT_TIMESTAMP), -- id=6  P2
       (2, 1, 13.6731, -89.2450, CURRENT_TIMESTAMP), -- id=7  P2
       (3, 5, 13.7033, -89.1962, CURRENT_TIMESTAMP), -- id=8  P3
       (3, 1, 13.7047, -89.1974, CURRENT_TIMESTAMP), -- id=9  P3
       (4, 1, 13.7415, -89.2211, CURRENT_TIMESTAMP), -- id=10 P4
       (4, 3, 13.7423, -89.2228, CURRENT_TIMESTAMP), -- id=11 P4
       (4, 4, 13.7436, -89.2239, CURRENT_TIMESTAMP), -- id=12 P4
       (5, 3, 13.7206, -89.1527, CURRENT_TIMESTAMP);
-- id=13 P5

-- ===== MEDICIONES (todas en 2024) =====
INSERT INTO medicion (arbol_id, fecha, dbh_cm, altura_m, observaciones)
VALUES (1, '2024-09-15', 25.4, 12.7, 'P1-A1'),
       (2, '2024-09-20', 18.0, 9.5, 'P1-A2'),
       (3, '2024-09-25', 22.0, 10.8, 'P1-A3'),
       (4, '2024-10-05', 32.1, 15.3, 'P2-A4'),
       (5, '2024-08-22', 20.0, 11.2, 'P2-A5'),
       (6, '2024-07-11', 21.0, 10.5, 'P2-A6'),
       (7, '2024-07-17', 23.5, 11.0, 'P2-A7'),
       (8, '2024-07-05', 40.0, 18.0, 'P3-A8'),
       (9, '2024-07-12', 28.0, 13.5, 'P3-A9'),
       (10, '2024-06-11', 22.0, 10.5, 'P4-A10'),
       (11, '2024-06-20', 19.5, 9.8, 'P4-A11'),
       (12, '2024-06-28', 24.1, 11.6, 'P4-A12'),
       (13, '2024-05-30', 17.9, 8.7, 'P5-A13');

-- ===== ESTIMACIONES =====
-- CO2e = carbono_kg * (44/12) ≈ * 3.6666667
-- P1 stock 2024 = (211.5 + 220.0 + 210.5) / 1000 = 0.642 t
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES (1, 430.0, 211.5, 775.5, 0.47, 5.0, 'P1-A1'),
       (2, 320.0, 220.0, 806.6667, 0.47, 5.0, 'P1-A2'),
       (3, 320.0, 210.5, 771.8333, 0.47, 5.0, 'P1-A3');

-- P2 stock 2024 = (510.3 + 612.7 + 550.0 + 520.0) / 1000 = 2.193 t
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES (4, 2600.0, 510.3, 1871.1, 0.47, 5.0, 'P2-A4'),
       (5, 430.0, 612.7, 2246.5667, 0.47, 5.0, 'P2-A5'),
       (6, 380.0, 550.0, 2016.6667, 0.47, 5.0, 'P2-A6'),
       (7, 410.0, 520.0, 1906.6667, 0.47, 5.0, 'P2-A7');

-- P3 stock 2024 = (480.5 + 477.5) / 1000 = 0.958 t
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES (8, 1920.0, 480.5, 1761.8333, 0.47, 5.0, 'P3-A8'),
       (9, 1280.0, 477.5, 1750.8333, 0.47, 5.0, 'P3-A9');

-- P4 stock 2024 = (150.0 + 129.4 + 107.6) / 1000 = 0.387 t
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES (10, 380.0, 150.0, 550.0, 0.47, 5.0, 'P4-A10'),
       (11, 250.0, 129.4, 474.4667, 0.47, 5.0, 'P4-A11'),
       (12, 300.0, 107.6, 394.5333, 0.47, 5.0, 'P4-A12');

-- P5 stock 2024 = (173.0) / 1000 = 0.173 t
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES (13, 340.0, 173.0, 634.3333, 0.47, 5.0, 'P5-A13');

-- ===== RESULTADOS DE PARQUE (consistentes con lo anterior) =====
-- Stock 2024 (t): P1=0.642 | P2=2.193 | P3=0.958 | P4=0.387 | P5=0.173
-- Captura 2024 (t/año): P1=32.844 | P2=177.52 | P3=27.58 | P4=73.836 | P5=13.776
INSERT INTO resultado_parque (parque_id, anio, stock_carbono_t, captura_anual_t)
VALUES (1, 2024, 0.642, 32.844),
       (2, 2024, 2.193, 177.520),
       (3, 2024, 0.958, 27.580),
       (4, 2024, 0.387, 73.836),
       (5, 2024, 0.173, 13.776);
