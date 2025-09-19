-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 19-09-2025 a las 09:14:15
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `disfrazarte_main`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `dsfr`
--

CREATE TABLE `dsfr` (
  `id` int(11) NOT NULL,
  `nombre` varchar(255) NOT NULL,
  `tematica` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `talla` varchar(50) DEFAULT NULL,
  `stock` int(11) NOT NULL,
  `available` int(11) NOT NULL,
  `estado` enum('Nuevo','Usado','En reparación') NOT NULL DEFAULT 'Nuevo',
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `dsfr`
--

INSERT INTO `dsfr` (`id`, `nombre`, `tematica`, `description`, `talla`, `stock`, `available`, `estado`, `notes`) VALUES
(1, 'Disfraz de Santa Claus', 'Navidad', 'Traje rojo de Santa Claus completo', 'M', 5, 5, 'Nuevo', 'Incluye gorro y cinturón'),
(2, 'Disfraz de Renito', 'Navidad', 'Disfraz de reno marrón con cuernos', 'M', 4, 4, 'Nuevo', 'Ideal para acompañar a Santa en eventos navideños'),
(3, 'Disfraz de Ángel', 'Navidad', 'Vestido blanco con alas y halo', 'S', 3, 3, 'Nuevo', 'Perfecto para representar la paz y la pureza de la Navidad'),
(4, 'Disfraz de Bruja', 'Halloween', 'Traje negro con sombrero puntiagudo', 'M', 6, 6, 'Nuevo', 'Incluye sombrero y escoba para Halloween'),
(5, 'Disfraz de Fantasma', 'Halloween', 'Sábana blanca con ojos', 'M', 8, 8, 'Nuevo', 'Sencillo y clásico para Halloween'),
(6, 'Disfraz de Calabaza', 'Halloween', 'Traje naranja con capucha de calabaza', 'M', 5, 5, 'Nuevo', 'Divertido y llamativo para fiestas de Halloween'),
(7, 'Disfraz de Conejo', 'Pascua', 'Traje blanco con orejas largas', 'S', 4, 4, 'Nuevo', 'Perfecto para actividades de Pascua y búsquedas de huevos'),
(8, 'Disfraz de Huevo de Pascua', 'Pascua', 'Traje colorido con detalles de huevo', 'M', 3, 3, 'Nuevo', 'Ideal para representar la alegría de la Pascua'),
(9, 'Disfraz de Carnaval', 'Carnaval', 'Disfraz multicolor con plumas', 'L', 7, 7, 'Nuevo', 'Incluye plumas y accesorios coloridos para Carnaval'),
(10, 'Disfraz de Payaso', 'Carnaval', 'Traje colorido con peluca', 'M', 6, 6, 'Nuevo', 'Divertido y alegre, ideal para animar fiestas'),
(11, 'Disfraz de Rey Mago', 'Día de Reyes', 'Traje elegante con capa dorada', 'L', 4, 4, 'Nuevo', 'Incluye capa y corona para representar a los Reyes Magos'),
(12, 'Disfraz de Pastorcito', 'Día de Reyes', 'Traje tradicional con bastón', 'S', 5, 4, 'Nuevo', 'Auténtico disfraz tradicional de pastorcito'),
(13, 'Disfraz de Cumpleaños Niño', 'Cumpleaños', 'Traje divertido y colorido para niños', 'S', 6, 5, 'Nuevo', 'Perfecto para animar fiestas infantiles'),
(14, 'Disfraz de Cumpleaños Niña', 'Cumpleaños', 'Vestido colorido con corona', 'M', 5, 5, 'Nuevo', 'Incluye corona y detalles brillantes para la niña cumpleañera'),
(15, 'Disfraz de Halloween Adulto', 'Halloween', 'Disfraz completo adulto', 'XL', 3, 3, 'Nuevo', 'Incluye todos los accesorios para un disfraz completo de Halloween');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `events`
--

CREATE TABLE `events` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `detalles` varchar(255) NOT NULL,
  `event_date` date NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` enum('Programado','Cancelado','Finalizado') DEFAULT 'Programado'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `events`
--

INSERT INTO `events` (`id`, `name`, `detalles`, `event_date`, `location`, `notes`, `created_at`, `status`) VALUES
(1, 'Navidad 2025', 'Fiesta de Navidad', '2025-12-25', 'Quito', 'Evento navideño para clientes', '2025-09-19 05:57:37', 'Programado'),
(2, 'Año Nuevo 2026', 'Celebración de Año Nuevo', '2026-01-01', 'Guayaquil', 'Brindis de año nuevo', '2025-09-19 05:57:37', 'Programado'),
(3, 'Día de Reyes 2026', 'Celebración de Reyes', '2026-01-06', 'Cuenca', 'Entrega de regalos', '2025-09-19 05:57:37', 'Programado'),
(4, 'Halloween 2025', 'Fiesta de disfraces', '2025-10-31', 'Ambato', 'Evento de disfraces de Halloween', '2025-09-19 05:57:37', 'Programado'),
(5, 'Pascua 2026', 'Fiesta de Pascua', '2026-04-17', 'Loja', 'Búsqueda de huevos', '2025-09-19 05:57:37', 'Programado'),
(6, 'Carnaval 2026', 'Celebración de Carnaval', '2026-02-15', 'Manta', 'Desfile y fiesta', '2025-09-19 05:57:37', 'Programado'),
(7, 'Cumpleaños Juan', 'Fiesta cumpleaños', '2025-11-05', 'Quito', 'Cumpleaños de Juan', '2025-09-19 05:57:37', 'Programado'),
(8, 'Cumpleaños de Sofía', 'Fiesta cumpleaños', '2025-11-12', 'Guayaquil', 'Cumpleaños normal.', '2025-09-19 05:57:37', 'Programado'),
(9, 'Evento de Prueba', 'No lo sé :c', '2025-09-17', 'Calle Sin Nombre 152', 'Esta es una nota adicional.', '2025-09-19 06:12:23', 'Programado'),
(10, 'Cambiate a Movistar.', 'Evento pensado para que la gente se cambie a Movistar.\nClaro que sí.', '2025-09-22', 'Quito.', 'Llevar su teléfono.', '2025-09-19 06:12:53', 'Programado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event_assignments`
--

CREATE TABLE `event_assignments` (
  `id` int(11) NOT NULL,
  `event_costume_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `lending_id` int(11) DEFAULT NULL,
  `assigned_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `event_assignments`
--

INSERT INTO `event_assignments` (`id`, `event_costume_id`, `user_id`, `lending_id`, `assigned_at`) VALUES
(2, 23, 8, NULL, '2025-09-19 06:14:50'),
(3, 18, 6, NULL, '2025-09-19 06:16:26'),
(4, 19, 6, NULL, '2025-09-19 06:16:26');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event_colors`
--

CREATE TABLE `event_colors` (
  `event_date` date NOT NULL,
  `color` char(7) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `event_colors`
--

INSERT INTO `event_colors` (`event_date`, `color`) VALUES
('2025-09-17', '#E5A087'),
('2025-09-22', '#8FC4F0'),
('2025-10-31', '#FFD9B3'),
('2025-11-05', '#CCFFEB'),
('2025-11-12', '#F0CCFF'),
('2025-12-25', '#FFCCCC'),
('2026-01-01', '#FFF2CC'),
('2026-01-06', '#CCE5FF'),
('2026-02-15', '#FFE0CC'),
('2026-04-17', '#E0FFCC');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `event_costumes`
--

CREATE TABLE `event_costumes` (
  `id` int(11) NOT NULL,
  `event_id` int(11) NOT NULL,
  `dsfr_id` int(11) NOT NULL,
  `qty_reserved` int(11) NOT NULL DEFAULT 1,
  `qty_assigned` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `event_costumes`
--

INSERT INTO `event_costumes` (`id`, `event_id`, `dsfr_id`, `qty_reserved`, `qty_assigned`) VALUES
(1, 1, 1, 2, 0),
(2, 1, 2, 2, 0),
(3, 1, 3, 1, 0),
(4, 2, 9, 2, 0),
(5, 2, 10, 2, 0),
(6, 3, 11, 1, 0),
(7, 3, 12, 2, 0),
(8, 4, 4, 2, 0),
(9, 4, 5, 3, 0),
(10, 4, 6, 2, 0),
(11, 4, 15, 1, 0),
(12, 5, 7, 1, 0),
(13, 5, 8, 1, 0),
(14, 6, 9, 2, 0),
(15, 6, 10, 2, 0),
(16, 7, 13, 1, 0),
(17, 7, 14, 1, 0),
(18, 8, 13, 1, 0),
(19, 8, 14, 1, 0),
(20, 9, 7, 0, 0),
(21, 9, 12, 0, 0),
(23, 10, 10, 0, 0);

--
-- Disparadores `event_costumes`
--
DELIMITER $$
CREATE TRIGGER `trg_event_costumes_before_insert` BEFORE INSERT ON `event_costumes` FOR EACH ROW BEGIN
    DECLARE
        total_reserved INT DEFAULT 0 ; DECLARE avail INT DEFAULT 0 ;
    SELECT
        available
    INTO avail
FROM
    dsfr
WHERE
    id = NEW.dsfr_id ;
SELECT
    COALESCE(SUM(ec.qty_reserved),
    0)
INTO total_reserved
FROM
    event_costumes ec
JOIN EVENTS e ON
    ec.event_id = e.id
WHERE
    ec.dsfr_id = NEW.dsfr_id AND e.event_date =(
    SELECT
        event_date
    FROM EVENTS
WHERE
    id = NEW.event_id
) ; IF(
    total_reserved + NEW.qty_reserved
) > avail THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT
    = 'No hay suficientes unidades disponibles para reservar en esa fecha.' ;
END IF ;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `trg_event_costumes_before_update` BEFORE UPDATE ON `event_costumes` FOR EACH ROW BEGIN
    DECLARE
        total_reserved INT DEFAULT 0 ; DECLARE avail INT DEFAULT 0 ;
    SELECT
        available
    INTO avail
FROM
    dsfr
WHERE
    id = NEW.dsfr_id ;
SELECT
    COALESCE(SUM(ec.qty_reserved),
    0)
INTO total_reserved
FROM
    event_costumes ec
JOIN EVENTS e ON
    ec.event_id = e.id
WHERE
    ec.dsfr_id = NEW.dsfr_id AND e.event_date =(
    SELECT
        event_date
    FROM EVENTS
WHERE
    id = NEW.event_id
) AND ec.id <> NEW.id ; IF(
    total_reserved + NEW.qty_reserved
) > avail THEN SIGNAL SQLSTATE '45000'
SET MESSAGE_TEXT
    = 'No hay suficientes unidades disponibles para reservar en esa fecha (update).' ;
END IF ;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `lendings`
--

CREATE TABLE `lendings` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `dsfr_id` int(11) NOT NULL,
  `date_out` varchar(255) NOT NULL,
  `date_return` varchar(255) DEFAULT NULL,
  `event_id` int(11) DEFAULT NULL,
  `returned` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `lendings`
--

INSERT INTO `lendings` (`id`, `user_id`, `dsfr_id`, `date_out`, `date_return`, `event_id`, `returned`) VALUES
(1, 4, 12, '19-09-2025', NULL, NULL, 0),
(2, 4, 13, '19-09-2025', '19-09-2025', NULL, 0),
(3, 3, 12, '19-09-2025', '19-09-2025', NULL, 0),
(4, 3, 13, '19-09-2025', NULL, NULL, 0),
(5, 12, 3, '19-09-2025', '19-09-2025', NULL, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `last_name_p` varchar(30) NOT NULL,
  `last_name_m` varchar(30) NOT NULL,
  `domicilio` varchar(250) DEFAULT NULL,
  `tel` varchar(25) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `sanctions` int(11) DEFAULT 0,
  `sanc_money` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `name`, `last_name_p`, `last_name_m`, `domicilio`, `tel`, `email`, `sanctions`, `sanc_money`) VALUES
(1, 'Juan', 'Pérez', 'Gómez', 'Av. Quito 123, Quito', '0991234567', 'juan.perez@email.com', 0, 0),
(2, 'María', 'López', 'Sánchez', 'Calle Guayaquil 45, Guayaquil', '0987654321', 'maria.lopez@email.com', 0, 0),
(3, 'Carlos', 'Martínez', 'Ramírez', 'Av. Loja 678, Loja', '0971122334', 'carlos.martinez@email.com', 0, 0),
(4, 'Ana', 'García', 'Vásquez', 'Calle Cuenca 89, Cuenca', '0969988776', 'ana.garcia@email.com', 0, 0),
(5, 'Luis', 'Torres', 'Molina', 'Av. Ambato 321, Ambato', '0998877665', 'luis.torres@email.com', 0, 0),
(6, 'Sofía', 'Flores', 'Castro', 'Calle Manta 12, Manta', '0985566778', 'sofia.flores@email.com', 0, 0),
(7, 'Andrés', 'Rojas', 'Chávez', 'Av. Riobamba 56, Riobamba', '0974455667', 'andres.rojas@email.com', 0, 0),
(8, 'Valentina', 'Suárez', 'Pacheco', 'Calle Ibarra 78, Ibarra', '0963344556', 'valentina.suarez@email.com', 0, 0),
(9, 'Diego', 'Vega', 'Cordero', 'Av. Esmeraldas 90, Esmeraldas', '0992233445', 'diego.vega@email.com', 0, 0),
(10, 'Camila', 'Ortiz', 'Salazar', 'Calle Santo Domingo 14, Santo Domingo', '0981122334', 'camila.ortiz@email.com', 0, 0),
(11, 'Miguel', 'Castillo', 'Romero', 'Av. Babahoyo 33, Babahoyo', '0975566778', 'miguel.castillo@email.com', 0, 0),
(12, 'Isabella', 'Morales', 'Díaz', 'Calle Portoviejo 22, Portoviejo', '0967788990', 'isabella.morales@email.com', 0, 0),
(13, 'Fernando', 'Méndez', 'Paredes', 'Av. Loja 11, Loja', '0993344556', 'fernando.mendez@email.com', 0, 0),
(14, 'Daniela', 'Ramos', 'Guerrero', 'Calle Latacunga 88, Latacunga', '0984455667', 'daniela.ramos@email.com', 0, 0),
(15, 'Sebastián', 'Cabrera', 'Vásquez', 'Av. Tulcán 77, Tulcán', '0975566443', 'sebastian.cabrera@email.com', 0, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_costumes`
--

CREATE TABLE `user_costumes` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `dsfr_id` int(11) NOT NULL,
  `qty` int(11) NOT NULL DEFAULT 1,
  `status` enum('Reservado','Asignado','Cancelado','Devuelto') NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `dsfr`
--
ALTER TABLE `dsfr`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_events_date` (`event_date`);

--
-- Indices de la tabla `event_assignments`
--
ALTER TABLE `event_assignments`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ea_ec_fk` (`event_costume_id`),
  ADD KEY `ea_user_fk` (`user_id`),
  ADD KEY `ea_lending_fk` (`lending_id`);

--
-- Indices de la tabla `event_colors`
--
ALTER TABLE `event_colors`
  ADD PRIMARY KEY (`event_date`);

--
-- Indices de la tabla `event_costumes`
--
ALTER TABLE `event_costumes`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `event_id` (`event_id`,`dsfr_id`),
  ADD KEY `idx_event_costumes_dsfr` (`dsfr_id`);

--
-- Indices de la tabla `lendings`
--
ALTER TABLE `lendings`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_lendings_user` (`user_id`),
  ADD KEY `fk_lendings_dsfr` (`dsfr_id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `user_costumes`
--
ALTER TABLE `user_costumes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_user_costumes_user` (`user_id`),
  ADD KEY `fk_user_costumes_dsfr` (`dsfr_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `dsfr`
--
ALTER TABLE `dsfr`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `events`
--
ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `event_assignments`
--
ALTER TABLE `event_assignments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `event_costumes`
--
ALTER TABLE `event_costumes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT de la tabla `lendings`
--
ALTER TABLE `lendings`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT de la tabla `user_costumes`
--
ALTER TABLE `user_costumes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `event_assignments`
--
ALTER TABLE `event_assignments`
  ADD CONSTRAINT `ea_ec_fk` FOREIGN KEY (`event_costume_id`) REFERENCES `event_costumes` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ea_lending_fk` FOREIGN KEY (`lending_id`) REFERENCES `lendings` (`id`) ON DELETE SET NULL,
  ADD CONSTRAINT `ea_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `event_costumes`
--
ALTER TABLE `event_costumes`
  ADD CONSTRAINT `ec_dsfr_fk` FOREIGN KEY (`dsfr_id`) REFERENCES `dsfr` (`id`),
  ADD CONSTRAINT `ec_event_fk` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `lendings`
--
ALTER TABLE `lendings`
  ADD CONSTRAINT `lendings_dsfr_fk` FOREIGN KEY (`dsfr_id`) REFERENCES `dsfr` (`id`),
  ADD CONSTRAINT `lendings_user_fk` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `user_costumes`
--
ALTER TABLE `user_costumes`
  ADD CONSTRAINT `fk_user_costumes_dsfr` FOREIGN KEY (`dsfr_id`) REFERENCES `dsfr` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_user_costumes_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
