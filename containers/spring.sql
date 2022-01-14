-- MySQL dump 10.13  Distrib 8.0.20, for Linux (x86_64)
--
-- Host: localhost    Database: SPRING
-- ------------------------------------------------------
-- Server version	8.0.20

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `id` varchar(255) NOT NULL,
  `end_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `start_date` datetime(6) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_mt8ulcc4k7fnc56rxaeu1sa33` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` VALUES ('02fd6b48-33f0-4bdc-af52-ba4bbd802db7','2021-11-07 11:58:43.933000','test name1','2021-11-06 12:58:43.933000',_binary ''),('6a4868cd-cdc9-4eef-9318-2f07ccc3e24e','2021-11-07 11:58:43.933000','test name2','2021-11-06 12:58:43.933000',_binary '\0');
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_races`
--

DROP TABLE IF EXISTS `event_races`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_races` (
  `event_id` varchar(255) NOT NULL,
  `races_id` varchar(255) NOT NULL,
  UNIQUE KEY `UK_t4u5abfwhf2bre440schtjq3` (`races_id`),
  KEY `FKsn3xqchgpc4krqdlpf0gpjgh0` (`event_id`),
  CONSTRAINT `FK7f0yppk5qpggkqs8168iy9yqo` FOREIGN KEY (`races_id`) REFERENCES `race` (`id`),
  CONSTRAINT `FKsn3xqchgpc4krqdlpf0gpjgh0` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_races`
--

LOCK TABLES `event_races` WRITE;
/*!40000 ALTER TABLE `event_races` DISABLE KEYS */;
/*!40000 ALTER TABLE `event_races` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race`
--

DROP TABLE IF EXISTS `race`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `race` (
  `id` varchar(255) NOT NULL,
  `category` varchar(8) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `end_time` datetime(6) DEFAULT NULL,
  `start_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_34ay6n28b5fe2cycvw3qx8r4e` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `race`
--

LOCK TABLES `race` WRITE;
/*!40000 ALTER TABLE `race` DISABLE KEYS */;
INSERT INTO `race` VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','CAT3','Test Race 8',NULL,'2021-11-24 12:33:05.572000'),('1884c898-ff32-40f6-8647-fcc2f7894dd9','CAT3','Test Race 6','2021-11-24 12:30:15.532000','2021-11-24 12:30:15.532000'),('292155e3-29cf-42e2-b5f0-a5003f4f08c0','CAT3','Test Race','2021-11-24 11:46:40.548000','2021-11-24 11:14:18.479000'),('5846a11a-e628-4c44-a8b0-f0ab73b31e68','CAT5','D.P',NULL,NULL),('5f921909-25d7-433d-a966-7f4c43982d9c','CAT3','Test Race 7','2021-11-24 12:31:56.869000','2021-11-24 12:31:56.869000'),('77b5aacc-d841-4949-8207-5a737ffa23aa','CAT5','Carl Brutananadilewski',NULL,NULL);
/*!40000 ALTER TABLE `race` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race_finish_order`
--

DROP TABLE IF EXISTS `race_finish_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `race_finish_order` (
  `race_id` varchar(255) NOT NULL,
  `finish_order` datetime(6) DEFAULT NULL,
  `finish_order_key` varchar(255) NOT NULL,
  PRIMARY KEY (`race_id`,`finish_order_key`),
  KEY `FK52epfrofb2cu900s9i9l6cdcx` (`finish_order_key`),
  CONSTRAINT `FK52epfrofb2cu900s9i9l6cdcx` FOREIGN KEY (`finish_order_key`) REFERENCES `racer` (`id`),
  CONSTRAINT `FKkmis1de9ss55kklqh380amkb1` FOREIGN KEY (`race_id`) REFERENCES `race` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `race_finish_order`
--

LOCK TABLES `race_finish_order` WRITE;
/*!40000 ALTER TABLE `race_finish_order` DISABLE KEYS */;
INSERT INTO `race_finish_order` VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','2021-11-24 12:53:58.633000','ce851d5a-df8b-4bf5-ab41-ef0c95a30236');
/*!40000 ALTER TABLE `race_finish_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `race_racers`
--

DROP TABLE IF EXISTS `race_racers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `race_racers` (
  `race_id` varchar(255) NOT NULL,
  `racers_id` varchar(255) NOT NULL,
  UNIQUE KEY `UK_rdmw93dcho5bnks5hy0rdt40e` (`racers_id`),
  KEY `FKl5c63waba1wbrk7567lqt2dq7` (`race_id`),
  CONSTRAINT `FK8k14l732x67t5kulic5aibb8b` FOREIGN KEY (`racers_id`) REFERENCES `racer` (`id`),
  CONSTRAINT `FKl5c63waba1wbrk7567lqt2dq7` FOREIGN KEY (`race_id`) REFERENCES `race` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `race_racers`
--

LOCK TABLES `race_racers` WRITE;
/*!40000 ALTER TABLE `race_racers` DISABLE KEYS */;
INSERT INTO `race_racers` VALUES ('008e44be-3d2a-4633-896d-8e2a50bdff5b','0e596a03-76c3-4c52-9cbe-b560c58ef750'),('008e44be-3d2a-4633-896d-8e2a50bdff5b','66f97578-3595-4f19-86c2-7e4dd4000167'),('008e44be-3d2a-4633-896d-8e2a50bdff5b','ce851d5a-df8b-4bf5-ab41-ef0c95a30236');
/*!40000 ALTER TABLE `race_racers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `racer`
--

DROP TABLE IF EXISTS `racer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `racer` (
  `id` varchar(255) NOT NULL,
  `category` varchar(8) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `birth_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `racer`
--

LOCK TABLES `racer` WRITE;
/*!40000 ALTER TABLE `racer` DISABLE KEYS */;
INSERT INTO `racer` VALUES ('08668f04-0a72-4753-adda-c261025c3d1b','CAT1','Catheryn',_binary '\0','Treutel','2005-08-25 12:26:20.102000'),('0e596a03-76c3-4c52-9cbe-b560c58ef750','CAT5','Test',_binary '\0','User2',NULL),('0edab7f4-1ca6-4fa2-8da7-ad1f6ef1fef0','CAT1','Vivien',_binary '\0','Turcotte','1927-01-30 04:01:44.663000'),('123ddca6-111d-4f3e-b2ec-ad72847a9c32','CAT5','Ling',_binary '\0','Thiel','2009-03-19 11:07:52.560000'),('14a535d2-b41c-4c02-a111-3916870c4aea','CAT5','Otilia',_binary '\0','Lesch','1997-03-19 14:47:07.206000'),('2dcd7ff6-7c77-41b9-b818-fc3e813e8b65','CAT2','Test',_binary '','User1',NULL),('5c81eed8-acf5-4630-bca2-30ab15684449','CAT5','Brook',_binary '\0','Torp','1947-08-05 19:44:10.496000'),('66f97578-3595-4f19-86c2-7e4dd4000167','CAT1','Test',_binary '\0','User',NULL),('6a26f863-338a-43a4-bc42-9d394587a858','CAT1','Andy',_binary '\0','Lang','1946-01-11 13:11:13.125000'),('8536179f-172f-4a65-b5b7-aa6c474f231b','CAT5','Cierra',_binary '\0','Lowe','1966-06-20 13:47:34.288000'),('92c4b707-4bd0-4260-b1c4-cdd12fd1f98a','CAT1','Robby',_binary '\0','Ratke','1987-11-26 05:07:20.001000'),('a2b38550-1898-4039-bea2-ff2ef0ee890e','CAT5','Sharlene',_binary '\0','Lueilwitz','2011-02-02 21:38:30.519000'),('b221015d-9c64-4b27-af81-3f359bf13c95','CAT1','Clifton',_binary '\0','Koss','1999-01-22 12:23:48.373000'),('b979c339-8ada-471e-8be7-de2450047c62','CAT1','Cinderella',_binary '\0','Heathcote','1929-01-30 14:35:16.070000'),('bdb93c7c-b6a2-4c04-bb62-dede941cce01','CAT1','Hassan',_binary '\0','Schulist','1993-01-14 22:50:59.765000'),('ce851d5a-df8b-4bf5-ab41-ef0c95a30236','CAT1','Teddy',_binary '\0','Crane',NULL),('d0e02e70-b428-4d51-bb76-d382f4253138','CAT1','Dianne',_binary '\0','Johnson','1952-06-04 10:01:09.310000'),('de2d9652-a1ed-4ee7-ba01-f60e6ce58186','CAT1','Meredith',_binary '\0','Simonis','1979-05-02 21:15:07.840000'),('e4f332ca-198f-408b-89b4-3e21b50c50eb','CAT1','Tyron',_binary '\0','Tremblay','1972-05-20 13:54:02.094000'),('e9942b25-043f-42c5-8704-01631dff6c23','CAT1','Lynwood',_binary '\0','Bins','1955-01-01 09:37:43.729000'),('ed156f43-5e1c-4ff1-bbe6-c421b976926f','CAT1','Kayla',_binary '\0','Powlowski','2003-04-16 22:37:24.438000');
/*!40000 ALTER TABLE `racer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('1023c9e1-6900-4520-b8ec-7753a5cdf120','Test',_binary '\0','User','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','USER','testuser2','test2@fake.fake','ACTIVE'),('5a66dfd6-dab0-453a-965a-6f302f903a3e','Teddy',_binary '\0','Crane','cbe0cd68cbca3868250c0ba545c48032f43eb0e8a5e6bab603d109251486f77a91e46a3146d887e37416c6bdb6cbe701bd514de778573c9b0068483c1c626aec','ROOT','tcrane','teddycrane@gmail.com','ACTIVE'),('5e0c215b-4309-4902-97cf-01e7fc2a17b1','Test',_binary '\0','User','ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff','ADMIN','admin','admintestuser@teddycrane.com','ACTIVE'),('7825ff10-d79e-494c-bfc4-a0184ae7badf','Test',_binary '\0','User','58ed8cdf67334eec66c872644262a921f2e77b4ada5891af0664bf3078dcf9d400564bb8aff3e37aad2b6529efb557d2dd2d61daf54fb9e3cbb47f3348dfd821','USER','testuser1','test1@fake.fake','ACTIVE');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-28 21:01:50
