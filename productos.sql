-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-05-2025 a las 12:07:46
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
-- Base de datos: `productos`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `producto`
--

CREATE TABLE `producto` (
  `id` int(11) NOT NULL,
  `nombre_producto` varchar(255) NOT NULL,
  `resena` varchar(255) NOT NULL,
  `stock` int(11) NOT NULL,
  `tipo_producto` enum('HOMBRE','MUJER','UNISEX') NOT NULL,
  `valor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `producto`
--

INSERT INTO `producto` (`id`, `nombre_producto`, `resena`, `stock`, `tipo_producto`, `valor`) VALUES
(1, 'Nuevo nombre', 'Actualizado', 10, 'HOMBRE', 100),
(2, 'Nocturno', 'Aroma intenso con notas de madera', 80, 'HOMBRE', 34990),
(3, 'Esencia Clara', 'Toques cítricos y herbal, muy fresco', 60, 'UNISEX', 27990),
(4, 'Dama Rosa', 'Base de jazmín con vainilla dulce', 90, 'MUJER', 26990),
(5, 'Sombra Azul', 'Fragancia con cuero y especias', 70, 'HOMBRE', 31990),
(6, 'Brisa Marina', 'Aroma marino suave y fresco', 85, 'UNISEX', 28990),
(7, 'Flor del Alba', 'Notas florales y frutales suaves', 100, 'MUJER', 30990),
(8, 'Fuego Intenso', 'Perfume fuerte y seductor', 50, 'HOMBRE', 35990),
(9, 'Equilibrio', 'Combinación neutra y versátil', 95, 'UNISEX', 26990),
(10, 'Luz Dorada', 'Aroma elegante y floral', 75, 'MUJER', 29990),
(11, 'Perfume 12312', 'aasdasd', 120, 'MUJER', 29990);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(11) NOT NULL,
  `cargo` enum('EMPLEADO','GERENTE') NOT NULL,
  `nombre` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `cargo`, `nombre`) VALUES
(1, 'GERENTE', 'María González'),
(2, 'EMPLEADO', 'Juan López'),
(3, 'EMPLEADO', 'Lucía Pérez'),
(4, 'GERENTE', 'Manuel Silva'),
(5, 'EMPLEADO', 'Fernanda Rojas'),
(6, 'EMPLEADO', 'Andrés Morales'),
(7, 'GERENTE', 'Daniela Castillo'),
(8, 'EMPLEADO', 'Carlos Vargas'),
(9, 'EMPLEADO', 'Ana Fuentes'),
(10, 'GERENTE', 'Benjamín Ramírez');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `producto`
--
ALTER TABLE `producto`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `producto`
--
ALTER TABLE `producto`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
