CREATE DATABASE  IF NOT EXISTS `pokemon` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `pokemon`;
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: pokemon
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `pokemon_stats`
--

DROP TABLE IF EXISTS `pokemon_stats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pokemon_stats` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pokemon_id` int DEFAULT NULL,
  `hp` int NOT NULL,
  `attack` int NOT NULL,
  `defense` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pokemon_id` (`pokemon_id`),
  CONSTRAINT `pokemon_stats_ibfk_1` FOREIGN KEY (`pokemon_id`) REFERENCES `pokemons` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pokemon_stats`
--

LOCK TABLES `pokemon_stats` WRITE;
/*!40000 ALTER TABLE `pokemon_stats` DISABLE KEYS */;
INSERT INTO `pokemon_stats` VALUES (1,1,35,55,40),(2,2,78,84,78),(3,3,35,45,160);
/*!40000 ALTER TABLE `pokemon_stats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pokemon_types`
--

DROP TABLE IF EXISTS `pokemon_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pokemon_types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `pokemon_id` int DEFAULT NULL,
  `type_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pokemon_id` (`pokemon_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `pokemon_types_ibfk_1` FOREIGN KEY (`pokemon_id`) REFERENCES `pokemons` (`id`) ON DELETE CASCADE,
  CONSTRAINT `pokemon_types_ibfk_2` FOREIGN KEY (`type_id`) REFERENCES `types` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pokemon_types`
--

LOCK TABLES `pokemon_types` WRITE;
/*!40000 ALTER TABLE `pokemon_types` DISABLE KEYS */;
INSERT INTO `pokemon_types` VALUES (1,1,1),(2,2,2),(3,2,4),(4,3,5);
/*!40000 ALTER TABLE `pokemon_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pokemons`
--

DROP TABLE IF EXISTS `pokemons`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pokemons` (
  `id` int NOT NULL AUTO_INCREMENT,
  `trainer_id` int DEFAULT NULL,
  `nickname` varchar(100) DEFAULT NULL,
  `rarity` enum('Common','Rare','Legendary') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `trainer_id` (`trainer_id`),
  CONSTRAINT `pokemons_ibfk_1` FOREIGN KEY (`trainer_id`) REFERENCES `trainers` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pokemons`
--

LOCK TABLES `pokemons` WRITE;
/*!40000 ALTER TABLE `pokemons` DISABLE KEYS */;
INSERT INTO `pokemons` VALUES (1,1,'Pikachu','Rare'),(2,1,'Charizard','Legendary'),(3,2,'Onix','Common');
/*!40000 ALTER TABLE `pokemons` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainers`
--

DROP TABLE IF EXISTS `trainers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `experience_points` float DEFAULT '0',
  `is_gym_leader` tinyint(1) DEFAULT '0',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainers`
--

LOCK TABLES `trainers` WRITE;
/*!40000 ALTER TABLE `trainers` DISABLE KEYS */;
INSERT INTO `trainers` VALUES (1,'Ash Ketchum',1500.5,0,'2026-01-09 00:20:22'),(2,'Brock',5000,1,'2026-01-09 00:20:22'),(3,'Misty',4200.75,1,'2026-01-09 00:20:22');
/*!40000 ALTER TABLE `trainers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `types`
--

DROP TABLE IF EXISTS `types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `types` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `type_name` (`type_name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `types`
--

LOCK TABLES `types` WRITE;
/*!40000 ALTER TABLE `types` DISABLE KEYS */;
INSERT INTO `types` VALUES (12,'Bug'),(16,'Dark'),(15,'Dragon'),(5,'Electric'),(18,'Fairy'),(7,'Fighting'),(2,'Fire'),(10,'Flying'),(14,'Ghost'),(4,'Grass'),(9,'Ground'),(6,'Ice'),(1,'Normal'),(8,'Poison'),(11,'Psychic'),(13,'Rock'),(17,'Steel'),(3,'Water');
/*!40000 ALTER TABLE `types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `view_detailed_pokemons`
--

DROP TABLE IF EXISTS `view_detailed_pokemons`;
/*!50001 DROP VIEW IF EXISTS `view_detailed_pokemons`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_detailed_pokemons` AS SELECT 
 1 AS `id`,
 1 AS `nickname`,
 1 AS `rarity`,
 1 AS `trainer_name`,
 1 AS `hp`,
 1 AS `attack`,
 1 AS `defense`,
 1 AS `types`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `view_trainer_performance`
--

DROP TABLE IF EXISTS `view_trainer_performance`;
/*!50001 DROP VIEW IF EXISTS `view_trainer_performance`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `view_trainer_performance` AS SELECT 
 1 AS `id`,
 1 AS `name`,
 1 AS `is_gym_leader`,
 1 AS `start_of_journey`,
 1 AS `count_of_pokemon`,
 1 AS `average_attack`*/;
SET character_set_client = @saved_cs_client;

--
-- Dumping routines for database 'pokemon'
--

--
-- Final view structure for view `view_detailed_pokemons`
--

/*!50001 DROP VIEW IF EXISTS `view_detailed_pokemons`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_detailed_pokemons` AS select `p`.`id` AS `id`,`p`.`nickname` AS `nickname`,`p`.`rarity` AS `rarity`,`t`.`name` AS `trainer_name`,`ps`.`hp` AS `hp`,`ps`.`attack` AS `attack`,`ps`.`defense` AS `defense`,group_concat(distinct `tp`.`type_name` separator ', ') AS `types` from ((((`pokemons` `p` join `trainers` `t` on((`p`.`trainer_id` = `t`.`id`))) join `pokemon_stats` `ps` on((`p`.`id` = `ps`.`pokemon_id`))) left join `pokemon_types` `pt` on((`p`.`id` = `pt`.`pokemon_id`))) left join `types` `tp` on((`pt`.`type_id` = `tp`.`id`))) group by `p`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_trainer_performance`
--

/*!50001 DROP VIEW IF EXISTS `view_trainer_performance`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_trainer_performance` AS select `t`.`id` AS `id`,`t`.`name` AS `name`,`t`.`is_gym_leader` AS `is_gym_leader`,`t`.`created_at` AS `start_of_journey`,count(`p`.`id`) AS `count_of_pokemon`,ifnull(avg(`ps`.`attack`),0) AS `average_attack` from ((`trainers` `t` left join `pokemons` `p` on((`t`.`id` = `p`.`trainer_id`))) left join `pokemon_stats` `ps` on((`p`.`id` = `ps`.`pokemon_id`))) group by `t`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-09  0:21:15
