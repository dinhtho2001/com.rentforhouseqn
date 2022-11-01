-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: rentforhouse
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `user_id` bigint NOT NULL,
  `house_id` bigint NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `comment_house_id` (`house_id`),
  CONSTRAINT `comment_house_id` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (1,'nhà đẹp',2,1,'user','2001-01-01 00:00:00','user','2001-01-01 00:00:00'),(2,'Nhà xây lâu chưa bạn',1,1,'admin','2001-01-01 00:00:00','admin','2001-01-01 00:00:00');
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house`
--

DROP TABLE IF EXISTS `house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `house` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `area` varchar(50) NOT NULL,
  `description` text,
  `detailsumary` text,
  `price` float NOT NULL,
  `image` varchar(255) DEFAULT NULL,
  `user_id` bigint NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `house_user_id` (`user_id`),
  CONSTRAINT `house_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house`
--

LOCK TABLES `house` WRITE;
/*!40000 ALTER TABLE `house` DISABLE KEYS */;
INSERT INTO `house` VALUES (1,'Cho thuê khách sạn 5 tầng đường Chương Dương, P. Nguyễn Văn Cừ, TP. Quy Nhơn','Đường Chương Dương, Phường Nguyễn Văn Cừ, Quy Nhơn, Bình Định','117.6 m2','Cho thuê khách sạn 5 tầng đường Chương Dương, P. Nguyễn Văn Cừ, TP. Quy Nhơn, T. Bình Định.','default',75000000,'default.png',1,'admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01'),(2,'CHUYÊN CHO THUÊ CĂN HỘ EMPIRE CITY THỦ THIÊM 1-2-3-4PN, GIÁ TỐT NHẤT THỊ TRƯỜNG CHỈ TỪ 15 TR/THÁNG','Dự án Empire City Thủ Thiêm, Quận 2, Hồ Chí Minh','64m2','* Cập nhật giá thuê Empire City Thủ Thiêm: Tòa Linden, Tilia. Xem nhà ngay, thương lượng trực tiếp chính chủ:','default',65000000,'default.png',2,'user','2001-01-01 01:01:01','admin','2001-01-01 01:01:01'),(10,'Cho thuê nhà liền kề, biệt thự Vườn Đào, đường Lạc Long Quân, Tây hồ','Cho thuê nhà liền kề, biệt thự Vườn Đào, đường Lạc Long Quân, Tây hồ','350 m2','Vị trí khu vực trung tâm. Giao thông thuận tiện thông thoáng. Khu vực Vườn Đào Lostte. Thông Võ Chí Công - Lạc Long Quân - An Dương Vương - Âu Cơ - Nhật Chiêu,... Hồ Tây lộng gió thoáng mát.',NULL,23000000,'default.png',1,'admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01'),(11,'cho thuê chung cư cao cấp tại Mễ trì, 2PN2VS',' Lê quang đạo, Phường Mễ Trì, Quận Nam Từ Liêm, Hà Nội','87m2','- Tòa nhà hạng sang, nhiều người nước ngoài ở - Nhà mới bàn giao - Hệ thống an ninh, pccc cực tốt, kiểm tra định kỳ hàng tháng - Nằm trên trục đường lớn, nhà view công viên cực đẹp - Diện tích 87m2 - Nhà 2 ngủ, 2 vệ sinh, 1 khách - Nội thất cơ bản - Nhà phù hợp cho người Tây Tứ Trạch','- Diện tích 87m2 - Nhà 2 ngủ, 2 vệ sinh, 1 khách - Nội thất cơ bản - Nhà phù hợp cho người Tây Tứ Trạch',17000000,'default.jpg',1,'admin','2022-10-28 10:32:11','admin','2022-10-28 10:32:11'),(12,'CHÍNH CHỦ BÁN 03 NGÕ 63 ĐƯỜNG AN DƯƠNG VƯƠNG , QUẬN TÂY HỒ , HÀ NỘI , XÂY MỚI 05 TẦNG , NGÕ 3M','Phường Phú Thượng, Tây Hồ, Hà Nội','30m2','Diện tích sổ đỏ : như sơ đồ tách thửa.Diện tích xây dựng: 30m² / tầng = 05 tầnggiá bán 4,3 tỷ đến 4,65 tỷ /tuỳ căn',NULL,4300000000,'default.jpg',2,'user','2022-10-28 10:32:11','user','2022-10-28 10:32:11');
/*!40000 ALTER TABLE `house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `house_type`
--

DROP TABLE IF EXISTS `house_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `house_type` (
  `house_id` bigint NOT NULL,
  `type_id` bigint NOT NULL,
  PRIMARY KEY (`house_id`,`type_id`),
  KEY `type_id` (`type_id`),
  CONSTRAINT `house_id` FOREIGN KEY (`house_id`) REFERENCES `house` (`id`),
  CONSTRAINT `type_id` FOREIGN KEY (`type_id`) REFERENCES `type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `house_type`
--

LOCK TABLES `house_type` WRITE;
/*!40000 ALTER TABLE `house_type` DISABLE KEYS */;
INSERT INTO `house_type` VALUES (1,1),(2,1),(10,1),(11,1),(12,2);
/*!40000 ALTER TABLE `house_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime DEFAULT NULL,
  `description` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'ROLE_ADMIN','admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01','Quản trị'),(2,'ROLE_USER','admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01','Người dùng'),(3,'ROLE_STAFF','admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01','Nhân viên');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `type`
--

DROP TABLE IF EXISTS `type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `type`
--

LOCK TABLES `type` WRITE;
/*!40000 ALTER TABLE `type` DISABLE KEYS */;
INSERT INTO `type` VALUES (1,'Nhà cho thuê','admin','2001-01-01 00:00:00','admin','2001-01-01 00:00:00'),(2,'Nhà bán','admin','2001-01-01 00:00:00','admin','2001-01-01 00:00:00'),(4,'Phòng trọ','admin','2001-01-01 00:00:00','admin','2001-01-01 00:00:00');
/*!40000 ALTER TABLE `type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `last_name` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(150) NOT NULL,
  `phone` varchar(10) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `createddate` datetime DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `modifieddate` datetime DEFAULT NULL,
  `status` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'a','nguyễn văn','admin','0123456789','admin@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01',_binary ''),(2,'b','nguyễn văn','user','0123456789','user@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01',_binary ''),(3,'c','nguyễn văn','staff','0123456789','staff@gmail.com','$2a$10$/RUbuT9KIqk6f8enaTQiLOXzhnUkiwEJRdtzdrMXXwU7dgnLKTCYG','admin','2001-01-01 01:01:01','admin','2001-01-01 01:01:01',_binary '');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `role_id` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (1,1),(2,2),(3,3);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-11-01 12:18:11
