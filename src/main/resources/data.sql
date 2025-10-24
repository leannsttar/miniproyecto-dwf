-- -- ===== ESPECIES =====
-- INSERT INTO especie (nombre_cientifico, nombre_comun, densidad_madera_rho, fuente_rho, version_rho)
-- VALUES ('Tabebuia rosea', 'Roble de sabana', 0.65, 'FAO 2022', 'v1'),
--        ('Dalbergia retusa', 'Cocobolo', 0.95, 'FAO 2022', 'v1'),
--        ('Cedrela odorata', 'Cedro', 0.40, 'FAO 2022', 'v1'),
--        ('Swietenia macrophylla', 'Caoba', 0.55, 'FAO 2022', 'v1'),
--        ('Mangifera indica', 'Mango', 0.60, 'FAO 2022', 'v1');
--
-- -- ===== PARQUES =====
-- -- capturaAnualT = 2.8 * area_ha:
-- -- P1: 11.73 ha -> 32.844    | P2: 63.40 ha -> 177.52
-- -- P3:  9.85 ha -> 27.58     | P4: 26.37 ha -> 73.836
-- -- P5:  4.92 ha -> 13.776
-- INSERT INTO parque (nombre, distrito, area_ha, lat, lon, creado_en)
-- VALUES ('Parque Cuscatlán', 'San Salvador Centro', 11.73, 13.6981, -89.1912, CURRENT_TIMESTAMP),  -- id=1
--        ('Parque Bicentenario', 'Antiguo Cuscatlán', 63.40, 13.6684, -89.2415, CURRENT_TIMESTAMP), -- id=2
--        ('Parque Infantil', 'San Salvador Centro', 9.85, 13.7029, -89.1951, CURRENT_TIMESTAMP),    -- id=3
--        ('Parque Satélite', 'Mejicanos', 26.37, 13.7403, -89.2206, CURRENT_TIMESTAMP),             -- id=4
--        ('Parque La Vega', 'Soyapango', 4.92, 13.7197, -89.1519, CURRENT_TIMESTAMP);
-- -- id=5
--
-- -- ===== ARBOLES (1 árbol -> 1 medición -> 1 estimación) =====
-- INSERT INTO arbol (parque_id, especie_id, lat, lon, creado_en)
-- VALUES (1, 1, 13.6991, -89.1920, CURRENT_TIMESTAMP), -- id=1  P1
--        (1, 3, 13.7002, -89.1934, CURRENT_TIMESTAMP), -- id=2  P1
--        (1, 4, 13.7016, -89.1946, CURRENT_TIMESTAMP), -- id=3  P1
--        (2, 2, 13.6703, -89.2421, CURRENT_TIMESTAMP), -- id=4  P2
--        (2, 4, 13.6712, -89.2430, CURRENT_TIMESTAMP), -- id=5  P2
--        (2, 5, 13.6724, -89.2442, CURRENT_TIMESTAMP), -- id=6  P2
--        (2, 1, 13.6731, -89.2450, CURRENT_TIMESTAMP), -- id=7  P2
--        (3, 5, 13.7033, -89.1962, CURRENT_TIMESTAMP), -- id=8  P3
--        (3, 1, 13.7047, -89.1974, CURRENT_TIMESTAMP), -- id=9  P3
--        (4, 1, 13.7415, -89.2211, CURRENT_TIMESTAMP), -- id=10 P4
--        (4, 3, 13.7423, -89.2228, CURRENT_TIMESTAMP), -- id=11 P4
--        (4, 4, 13.7436, -89.2239, CURRENT_TIMESTAMP), -- id=12 P4
--        (5, 3, 13.7206, -89.1527, CURRENT_TIMESTAMP);
-- -- id=13 P5
--
-- -- ===== MEDICIONES (todas en 2024) =====
-- INSERT INTO medicion (arbol_id, fecha, dbh_cm, altura_m, observaciones)
-- VALUES (1, '2024-09-15', 25.4, 12.7, 'P1-A1'),
--        (2, '2024-09-20', 18.0, 9.5, 'P1-A2'),
--        (3, '2024-09-25', 22.0, 10.8, 'P1-A3'),
--        (4, '2024-10-05', 32.1, 15.3, 'P2-A4'),
--        (5, '2024-08-22', 20.0, 11.2, 'P2-A5'),
--        (6, '2024-07-11', 21.0, 10.5, 'P2-A6'),
--        (7, '2024-07-17', 23.5, 11.0, 'P2-A7'),
--        (8, '2024-07-05', 40.0, 18.0, 'P3-A8'),
--        (9, '2024-07-12', 28.0, 13.5, 'P3-A9'),
--        (10, '2024-06-11', 22.0, 10.5, 'P4-A10'),
--        (11, '2024-06-20', 19.5, 9.8, 'P4-A11'),
--        (12, '2024-06-28', 24.1, 11.6, 'P4-A12'),
--        (13, '2024-05-30', 17.9, 8.7, 'P5-A13');
--
-- -- ===== ESTIMACIONES =====
-- -- CO2e = carbono_kg * (44/12) ≈ * 3.6666667
-- -- P1 stock 2024 = (211.5 + 220.0 + 210.5) / 1000 = 0.642 t
-- INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
-- VALUES (1, 430.0, 211.5, 775.5, 0.47, 5.0, 'P1-A1'),
--        (2, 320.0, 220.0, 806.6667, 0.47, 5.0, 'P1-A2'),
--        (3, 320.0, 210.5, 771.8333, 0.47, 5.0, 'P1-A3');
--
-- -- P2 stock 2024 = (510.3 + 612.7 + 550.0 + 520.0) / 1000 = 2.193 t
-- INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
-- VALUES (4, 2600.0, 510.3, 1871.1, 0.47, 5.0, 'P2-A4'),
--        (5, 430.0, 612.7, 2246.5667, 0.47, 5.0, 'P2-A5'),
--        (6, 380.0, 550.0, 2016.6667, 0.47, 5.0, 'P2-A6'),
--        (7, 410.0, 520.0, 1906.6667, 0.47, 5.0, 'P2-A7');
--
-- -- P3 stock 2024 = (480.5 + 477.5) / 1000 = 0.958 t
-- INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
-- VALUES (8, 1920.0, 480.5, 1761.8333, 0.47, 5.0, 'P3-A8'),
--        (9, 1280.0, 477.5, 1750.8333, 0.47, 5.0, 'P3-A9');
--
-- -- P4 stock 2024 = (150.0 + 129.4 + 107.6) / 1000 = 0.387 t
-- INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
-- VALUES (10, 380.0, 150.0, 550.0, 0.47, 5.0, 'P4-A10'),
--        (11, 250.0, 129.4, 474.4667, 0.47, 5.0, 'P4-A11'),
--        (12, 300.0, 107.6, 394.5333, 0.47, 5.0, 'P4-A12');
--
-- -- P5 stock 2024 = (173.0) / 1000 = 0.173 t
-- INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
-- VALUES (13, 340.0, 173.0, 634.3333, 0.47, 5.0, 'P5-A13');
--
-- -- ===== RESULTADOS DE PARQUE (consistentes con lo anterior) =====
-- -- Stock 2024 (t): P1=0.642 | P2=2.193 | P3=0.958 | P4=0.387 | P5=0.173
-- -- Captura 2024 (t/año): P1=32.844 | P2=177.52 | P3=27.58 | P4=73.836 | P5=13.776
-- INSERT INTO resultado_parque (parque_id, anio, stock_carbono_t, captura_anual_t)
-- VALUES (1, 2024, 0.642, 32.844),
--        (2, 2024, 2.193, 177.520),
--        (3, 2024, 0.958, 27.580),
--        (4, 2024, 0.387, 73.836),
--        (5, 2024, 0.173, 13.776);


-- ===== ESPECIES =====
INSERT INTO especie (nombre_cientifico, nombre_comun, densidad_madera_rho, fuente_rho, version_rho)
VALUES ('Tabebuia rosea', 'Roble de sabana', 0.65, 'FAO 2022', 'v1'),
       ('Dalbergia retusa', 'Cocobolo',       0.95, 'FAO 2022', 'v1'),
       ('Cedrela odorata',  'Cedro',          0.40, 'FAO 2022', 'v1'),
       ('Swietenia macrophylla','Caoba',      0.55, 'FAO 2022', 'v1'),
       ('Mangifera indica', 'Mango',          0.60, 'FAO 2022', 'v1');

-- ===== PARQUES =====
-- capturaAnualT = 2.8 * area_ha:
-- P1: 11.73 ha -> 32.844    | P2: 63.40 ha -> 177.520
-- P3:  9.85 ha -> 27.580    | P4: 26.37 ha -> 73.836
-- P5:  4.92 ha -> 13.776
INSERT INTO parque (nombre, distrito, area_ha, lat, lon, creado_en)
VALUES ('Parque Cuscatlán',   'San Salvador Centro', 11.73, 13.6981, -89.1912, CURRENT_TIMESTAMP),  -- id=1
       ('Parque Bicentenario','Antiguo Cuscatlán',   63.40, 13.6684, -89.2415, CURRENT_TIMESTAMP),  -- id=2
       ('Parque Infantil',    'San Salvador Centro',  9.85, 13.7029, -89.1951, CURRENT_TIMESTAMP),  -- id=3
       ('Parque Satélite',    'Mejicanos',           26.37, 13.7403, -89.2206, CURRENT_TIMESTAMP),  -- id=4
       ('Parque La Vega',     'Soyapango',            4.92, 13.7197, -89.1519, CURRENT_TIMESTAMP);  -- id=5

-- ===== ARBOLES =====
-- P1: arboles 1..3 | P2: 4..7 | P3: 8..9 | P4: 10..12 | P5: 13
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
       (5, 3, 13.7206, -89.1527, CURRENT_TIMESTAMP); -- id=13 P5

-- =====================================================================
-- =========================== MEDICIONES ===============================
-- =====================================================================

-- ===== MEDICIONES 2023 (IDs 1..13) =====
INSERT INTO medicion (arbol_id, fecha, dbh_cm, altura_m, observaciones)
VALUES (1,  '2023-07-10', 24.5, 12.0, 'P1-A1-2023'),
       (2,  '2023-07-15', 17.2,  9.0, 'P1-A2-2023'),
       (3,  '2023-07-20', 21.0, 10.2, 'P1-A3-2023'),
       (4,  '2023-08-05', 31.5, 15.0, 'P2-A4-2023'),
       (5,  '2023-08-12', 19.3, 10.8, 'P2-A5-2023'),
       (6,  '2023-08-20', 20.1, 10.0, 'P2-A6-2023'),
       (7,  '2023-09-02', 22.8, 10.7, 'P2-A7-2023'),
       (8,  '2023-07-03', 39.0, 17.5, 'P3-A8-2023'),
       (9,  '2023-07-09', 27.1, 13.0, 'P3-A9-2023'),
       (10, '2023-06-11', 21.5, 10.0, 'P4-A10-2023'),
       (11, '2023-06-20', 18.8,  9.4, 'P4-A11-2023'),
       (12, '2023-06-28', 23.6, 11.2, 'P4-A12-2023'),
       (13, '2023-05-30', 17.5,  8.5, 'P5-A13-2023');

-- ===== MEDICIONES 2024 (IDs 14..26) =====
INSERT INTO medicion (arbol_id, fecha, dbh_cm, altura_m, observaciones)
VALUES (1,  '2024-09-15', 25.4, 12.7, 'P1-A1-2024'),
       (2,  '2024-09-20', 18.0,  9.5, 'P1-A2-2024'),
       (3,  '2024-09-25', 22.0, 10.8, 'P1-A3-2024'),
       (4,  '2024-10-05', 32.1, 15.3, 'P2-A4-2024'),
       (5,  '2024-08-22', 20.0, 11.2, 'P2-A5-2024'),
       (6,  '2024-07-11', 21.0, 10.5, 'P2-A6-2024'),
       (7,  '2024-07-17', 23.5, 11.0, 'P2-A7-2024'),
       (8,  '2024-07-05', 40.0, 18.0, 'P3-A8-2024'),
       (9,  '2024-07-12', 28.0, 13.5, 'P3-A9-2024'),
       (10, '2024-06-11', 22.0, 10.5, 'P4-A10-2024'),
       (11, '2024-06-20', 19.5,  9.8, 'P4-A11-2024'),
       (12, '2024-06-28', 24.1, 11.6, 'P4-A12-2024'),
       (13, '2024-05-30', 17.9,  8.7, 'P5-A13-2024');

-- ===== MEDICIONES 2025 (IDs 27..39) =====
INSERT INTO medicion (arbol_id, fecha, dbh_cm, altura_m, observaciones)
VALUES (1,  '2025-07-10', 26.0, 13.0, 'P1-A1-2025'),
       (2,  '2025-07-15', 18.7,  9.9, 'P1-A2-2025'),
       (3,  '2025-07-20', 22.5, 11.1, 'P1-A3-2025'),
       (4,  '2025-08-05', 32.6, 15.6, 'P2-A4-2025'),
       (5,  '2025-08-12', 20.5, 11.5, 'P2-A5-2025'),
       (6,  '2025-08-20', 21.5, 10.9, 'P2-A6-2025'),
       (7,  '2025-09-02', 24.0, 11.4, 'P2-A7-2025'),
       (8,  '2025-07-03', 41.0, 18.5, 'P3-A8-2025'),
       (9,  '2025-07-09', 28.5, 14.0, 'P3-A9-2025'),
       (10, '2025-06-11', 22.6, 10.9, 'P4-A10-2025'),
       (11, '2025-06-20', 19.9, 10.0, 'P4-A11-2025'),
       (12, '2025-06-28', 24.5, 11.8, 'P4-A12-2025'),
       (13, '2025-05-30', 18.3,  8.9, 'P5-A13-2025');

-- =====================================================================
-- =========================== ESTIMACIONES =============================
-- =====================================================================

-- ===== ESTIMACIONES 2023 (medicion_id 1..13) =====
-- CO2e = carbono_kg * (44/12) = carbono_kg * 3.6666667
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES
-- P1 (stock 2023 = 0.600 t)
(1,  426.0, 200.0, 733.3333, 0.47, 5.0, 'P1-A1-2023'),
(2,  446.8, 210.0, 770.0000, 0.47, 5.0, 'P1-A2-2023'),
(3,  404.3, 190.0, 696.6667, 0.47, 5.0, 'P1-A3-2023'),

-- P2 (stock 2023 = 2.060 t)
(4, 1063.8, 500.0, 1833.3333, 0.47, 5.0, 'P2-A4-2023'),
(5, 1106.4, 520.0, 1906.6667, 0.47, 5.0, 'P2-A5-2023'),
(6, 1085.1, 510.0, 1870.0000, 0.47, 5.0, 'P2-A6-2023'),
(7, 1127.7, 530.0, 1943.3333, 0.47, 5.0, 'P2-A7-2023'),

-- P3 (stock 2023 = 0.930 t)
(8, 1000.0, 470.0, 1723.3333, 0.47, 5.0, 'P3-A8-2023'),
(9,  978.7, 460.0, 1686.6667, 0.47, 5.0, 'P3-A9-2023'),

-- P4 (stock 2023 = 0.390 t)
(10, 297.9, 140.0,  513.3333, 0.47, 5.0, 'P4-A10-2023'),
(11, 276.6, 130.0,  476.6667, 0.47, 5.0, 'P4-A11-2023'),
(12, 255.3, 120.0,  440.0000, 0.47, 5.0, 'P4-A12-2023'),

-- P5 (stock 2023 = 0.160 t)
(13, 340.4, 160.0,  586.6667, 0.47, 5.0, 'P5-A13-2023');

-- ===== ESTIMACIONES 2024 (medicion_id 14..26) =====
-- (mismos valores que tu dataset original 2024)
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES
-- P1 stock 2024 = 0.642 t
(14, 430.0, 211.5,  775.5,    0.47, 5.0, 'P1-A1-2024'),
(15, 320.0, 220.0,  806.6667, 0.47, 5.0, 'P1-A2-2024'),
(16, 320.0, 210.5,  771.8333, 0.47, 5.0, 'P1-A3-2024'),

-- P2 stock 2024 = 2.193 t
(17, 2600.0, 510.3, 1871.1,   0.47, 5.0, 'P2-A4-2024'),
(18, 430.0,  612.7, 2246.5667,0.47, 5.0, 'P2-A5-2024'),
(19, 380.0,  550.0, 2016.6667,0.47, 5.0, 'P2-A6-2024'),
(20, 410.0,  520.0, 1906.6667,0.47, 5.0, 'P2-A7-2024'),

-- P3 stock 2024 = 0.958 t
(21, 1920.0, 480.5, 1761.8333,0.47, 5.0, 'P3-A8-2024'),
(22, 1280.0, 477.5, 1750.8333,0.47, 5.0, 'P3-A9-2024'),

-- P4 stock 2024 = 0.387 t
(23, 380.0, 150.0,  550.0,   0.47, 5.0, 'P4-A10-2024'),
(24, 250.0, 129.4,  474.4667,0.47, 5.0, 'P4-A11-2024'),
(25, 300.0, 107.6,  394.5333,0.47, 5.0, 'P4-A12-2024'),

-- P5 stock 2024 = 0.173 t
(26, 340.0, 173.0,  634.3333,0.47, 5.0, 'P5-A13-2024');

-- ===== ESTIMACIONES 2025 (medicion_id 27..39) =====
INSERT INTO estimacion (medicion_id, biomasa_kg, carbono_kg, co2e_kg, fraccion_carbono, incertidumbre_porc, notas)
VALUES
-- P1 (stock 2025 = 0.645 t)
(27, 457.4, 215.0,  788.3333, 0.47, 5.0, 'P1-A1-2025'),
(28, 478.7, 225.0,  825.0000, 0.47, 5.0, 'P1-A2-2025'),
(29, 436.2, 205.0,  751.6667, 0.47, 5.0, 'P1-A3-2025'),

-- P2 (stock 2025 = 2.220 t)
(30, 1095.7, 515.0, 1888.3333, 0.47, 5.0, 'P2-A4-2025'),
(31, 1329.8, 625.0, 2291.6667, 0.47, 5.0, 'P2-A5-2025'),
(32, 1180.9, 555.0, 2035.0000, 0.47, 5.0, 'P2-A6-2025'),
(33, 1117.0, 525.0, 1925.0000, 0.47, 5.0, 'P2-A7-2025'),

-- P3 (stock 2025 = 0.975 t)
(34, 1042.6, 490.0, 1796.6667, 0.47, 5.0, 'P3-A8-2025'),
(35, 1031.9, 485.0, 1778.3333, 0.47, 5.0, 'P3-A9-2025'),

-- P4 (stock 2025 = 0.393 t)
(36, 323.4, 152.0,  557.3333, 0.47, 5.0, 'P4-A10-2025'),
(37, 278.7, 131.0,  480.3333, 0.47, 5.0, 'P4-A11-2025'),
(38, 234.0, 110.0,  403.3333, 0.47, 5.0, 'P4-A12-2025'),

-- P5 (stock 2025 = 0.178 t)
(39, 378.7, 178.0,  652.6667, 0.47, 5.0, 'P5-A13-2025');

-- =====================================================================
-- ======================= RESULTADO PARQUE ============================
-- =====================================================================
-- Stocks consistentes con las sumas de carbono_kg por parque/año.
-- Captura = 2.8 * area_ha (constante por parque, independiente del año).

INSERT INTO resultado_parque (parque_id, anio, stock_carbono_t, captura_anual_t)
VALUES
-- 2023
(1, 2023, 0.600,  32.844),
(2, 2023, 2.060, 177.520),
(3, 2023, 0.930,  27.580),
(4, 2023, 0.390,  73.836),
(5, 2023, 0.160,  13.776),

-- 2024
(1, 2024, 0.642,  32.844),
(2, 2024, 2.193, 177.520),
(3, 2024, 0.958,  27.580),
(4, 2024, 0.387,  73.836),
(5, 2024, 0.173,  13.776),

-- 2025
(1, 2025, 0.645,  32.844),
(2, 2025, 2.220, 177.520),
(3, 2025, 0.975,  27.580),
(4, 2025, 0.393,  73.836),
(5, 2025, 0.178,  13.776);
