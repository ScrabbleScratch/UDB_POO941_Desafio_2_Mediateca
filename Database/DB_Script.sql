DROP DATABASE mediateca;

CREATE DATABASE mediateca;

USE mediateca;

CREATE USER IF NOT EXISTS 'mediateca'@'%' IDENTIFIED WITH mysql_native_password BY 'mediateca';

GRANT ALL PRIVILEGES ON mediateca.* TO 'mediateca'@'%';

--
-- Table structure for table `cd`
--

DROP TABLE IF EXISTS `cds`;

CREATE TABLE `cds` (
  `codigo` varchar(8) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `artista` varchar(100) NOT NULL,
  `genero` varchar(50) NOT NULL,
  `duracion` float NOT NULL,
  `canciones` int NOT NULL,
  `unidades` int NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `cd`
--

LOCK TABLES `cds` WRITE;

INSERT INTO `cds` VALUES ('CD000001','25','Adele','pop',105,12,100),('CD000002','Hybrid Theory','Linking Park','alternativo',105,8,280),('CD000003','But Seriously','Phil Collins','pop',85,10,100),('CD000004','Nevermind','Nirvana','grunge',42,5,100);

UNLOCK TABLES;

--
-- Table structure for table `dvd`
--

DROP TABLE IF EXISTS `dvds`;

CREATE TABLE `dvds` (
  `codigo` varchar(8) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `director` varchar(100) NOT NULL,
  `duracion` float NOT NULL,
  `genero` varchar(50) NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `dvd`
--

LOCK TABLES `dvds` WRITE;

INSERT INTO `dvds` VALUES ('DVD00001','Buscando a Nemo','Andrew Stanton',140,'infantil'),('DVD00002','Los Increibles','Brad Bird',140,'infantil'),('DVD00003','Transformers','Michael Bay',130,'Accion'),('DVD00004','El Caballero de la Noche','Christopher Nolan',140,'Accion');

UNLOCK TABLES;

--
-- Table structure for table `libro`
--

DROP TABLE IF EXISTS `libros`;

CREATE TABLE `libros` (
  `codigo` varchar(8) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `autor` varchar(100) NOT NULL,
  `paginas` int NOT NULL,
  `editorial` varchar(100) NOT NULL,
  `isbn` varchar(10) NOT NULL,
  `anio_publicacion` int NOT NULL,
  `unidades` int NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `libro`
--

LOCK TABLES `libros` WRITE;

INSERT INTO `libros` VALUES ('LIB00001','Don Quijote de la Mancha','Miguel de Cervantes',600,'Alfaguara','325189676',2008,300),('LIB00002','Cumbres Borrascosas','Emily Bronte',300,'Lumen','274604507',2003,235),('LIB00003','El ultimo deseo','Andrzej Sapkowski',500,'Alamut','321988833',2012,200),('LIB00004','La rebelion de Atlas','Ayn Rand',600,'Planeta','245489886',1957,100),('LIB00005','Meditaciones de Marco Aurelio','Marco Aurelio',300,'Edaf','259455539',2020,100),('LIB00006','El universo en una cascara de nuez','Stephen Hawkign',400,'Booket','343178354',2020,125);

UNLOCK TABLES;

--
-- Table structure for table `revista`
--

DROP TABLE IF EXISTS `revistas`;

CREATE TABLE `revistas` (
  `codigo` varchar(8) NOT NULL,
  `titulo` varchar(100) NOT NULL,
  `editorial` varchar(100) NOT NULL,
  `periodicidad` varchar(50) NOT NULL,
  `fecha_publicacion` varchar(50) NOT NULL,
  `unidades` int NOT NULL,
  PRIMARY KEY (`codigo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `revista`
--

LOCK TABLES `revistas` WRITE;

INSERT INTO `revistas` VALUES ('REV00001','NATURE','Nature Publishing Group','semanal','1869',100),('REV00002','SCIENCE','AAAS','semanal','1880',200),('REV00003','ASTROPHYS','Springer Science','semanal','1969',100),('REV00004','National Geographic','National Geographic','semanal','1888',100);

UNLOCK TABLES;
