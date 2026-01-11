CREATE DATABASE  IF NOT EXISTS `railway` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `railway`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: turntable.proxy.rlwy.net    Database: railway
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `isActive` int NOT NULL,
  `idRole` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idRole` (`idRole`),
  CONSTRAINT `account_ibfk_1` FOREIGN KEY (`idRole`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (7,'admin@gmail.com','$2a$10$0ga5cHBbGbDQK4kCaWFh2.YiYe4MgSe8L2fFl785jOvc9i1TB0ZZ.',1,1),(8,'over2505@gmail.com','$2a$10$GK7999pVnR3FJGbKgMFVQetwQW/kpffX.Jy6iUToMnClOzkzXUBaO',1,3),(19,'hai@gmail.com','$2a$10$Xw5DyPGjKkJ0tCqxjUIE1O9J7.AwXzuLAbtUNHlB2yzHkIiyxoEdW',1,2),(20,'quanvm@kaopiz.com','$2a$10$NqX41kna/yv9A7oxHnM6lu0WB7cx7t1OLmdsNFuwDYu/dkEY8msg6',1,3),(21,'nvhai@gmail.com','$2a$10$1iiyjyh5sgo1g780Pi8fL.0kRNY8v8m7mESsKS2poPTKOQVKJFw3i',1,2),(23,'haint@bsscommerce.com','$2a$10$lShWlVWf9wMMEpY6yFR9z.og5Iatu7Q//Tcc7ftWhm4SEMpD2bU.m',1,3);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bill`
--

DROP TABLE IF EXISTS `bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bill` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paymentDate` datetime DEFAULT NULL,
  `totalBeforeTax` float NOT NULL,
  `totalAfterTax` float NOT NULL,
  `idPaymentMethod` int DEFAULT NULL,
  `idPaymentStatus` int NOT NULL,
  `idCustomer` int NOT NULL,
  `createdAt` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idPaymentMethod` (`idPaymentMethod`),
  KEY `idPaymentStatus` (`idPaymentStatus`),
  KEY `idCustomer` (`idCustomer`),
  CONSTRAINT `bill_ibfk_1` FOREIGN KEY (`idPaymentMethod`) REFERENCES `paymentmethod` (`id`),
  CONSTRAINT `bill_ibfk_2` FOREIGN KEY (`idPaymentStatus`) REFERENCES `paymentstatus` (`id`),
  CONSTRAINT `bill_ibfk_3` FOREIGN KEY (`idCustomer`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bill`
--

LOCK TABLES `bill` WRITE;
/*!40000 ALTER TABLE `bill` DISABLE KEYS */;
INSERT INTO `bill` VALUES (34,'2026-01-08 17:44:53',7500000,8250000,NULL,4,25,'2026-01-08 17:44:53'),(35,'2026-01-08 17:48:43',19000000,20900000,NULL,1,43,'2026-01-08 17:48:43'),(36,'2026-01-08 17:55:18',10000000,11000000,NULL,4,43,'2026-01-08 17:55:18'),(37,'2026-01-09 03:19:47',1000,1100,NULL,4,14,'2026-01-09 03:19:47'),(38,'2026-01-09 03:24:11',2000,2200,1,2,14,'2026-01-09 03:24:11'),(39,'2026-01-09 03:30:18',2000,2200,NULL,4,44,'2026-01-09 03:30:18'),(40,'2026-01-09 22:37:49',2500000,2750000,NULL,1,25,'2026-01-09 22:37:49'),(41,'2026-01-10 09:56:43',2500000,2750000,NULL,4,25,'2026-01-10 09:56:43');
/*!40000 ALTER TABLE `bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `blacklist`
--

DROP TABLE IF EXISTS `blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `blacklist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idCustomer` int NOT NULL,
  `idHotel` int NOT NULL,
  `count` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idCustomer` (`idCustomer`),
  KEY `idHotel` (`idHotel`),
  CONSTRAINT `blacklist_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `users` (`id`),
  CONSTRAINT `blacklist_ibfk_2` FOREIGN KEY (`idHotel`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `blacklist`
--

LOCK TABLES `blacklist` WRITE;
/*!40000 ALTER TABLE `blacklist` DISABLE KEYS */;
INSERT INTO `blacklist` VALUES (1,27,1,3);
/*!40000 ALTER TABLE `blacklist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookingroom`
--

DROP TABLE IF EXISTS `bookingroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookingroom` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contractCheckInTime` datetime NOT NULL,
  `contractCheckOutTime` datetime NOT NULL,
  `actualCheckInTime` datetime DEFAULT NULL,
  `actualCheckOutTime` datetime DEFAULT NULL,
  `idCustomer` int NOT NULL,
  `idRoomPromotion` int DEFAULT NULL,
  `idRoom` int NOT NULL,
  `idEmployee` int DEFAULT NULL,
  `idBill` int NOT NULL,
  `status` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idRoom` (`idRoom`),
  KEY `idCustomer` (`idCustomer`),
  KEY `idRoomPromotion` (`idRoomPromotion`),
  KEY `idEmployee` (`idEmployee`),
  KEY `idBill` (`idBill`),
  CONSTRAINT `bookingroom_ibfk_1` FOREIGN KEY (`idRoom`) REFERENCES `room` (`id`),
  CONSTRAINT `bookingroom_ibfk_2` FOREIGN KEY (`idCustomer`) REFERENCES `users` (`id`),
  CONSTRAINT `bookingroom_ibfk_3` FOREIGN KEY (`idRoomPromotion`) REFERENCES `roompromotion` (`id`),
  CONSTRAINT `bookingroom_ibfk_4` FOREIGN KEY (`idEmployee`) REFERENCES `users` (`id`),
  CONSTRAINT `bookingroom_ibfk_5` FOREIGN KEY (`idBill`) REFERENCES `bill` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookingroom`
--

LOCK TABLES `bookingroom` WRITE;
/*!40000 ALTER TABLE `bookingroom` DISABLE KEYS */;
INSERT INTO `bookingroom` VALUES (27,'2026-01-09 14:00:00','2026-01-12 12:00:00','2026-01-09 02:44:02',NULL,25,NULL,1,NULL,34,1),(28,'2026-01-09 14:00:00','2026-01-11 12:00:00',NULL,NULL,43,NULL,2,NULL,35,1),(29,'2026-01-09 14:00:00','2026-01-11 12:00:00',NULL,NULL,43,NULL,4,NULL,35,1),(30,'2026-01-09 14:00:00','2026-01-11 12:00:00',NULL,NULL,43,NULL,20,NULL,35,1),(31,'2026-01-09 14:00:00','2026-01-11 12:00:00','2026-01-09 11:07:18',NULL,43,NULL,33,NULL,36,1),(32,'2026-01-09 14:00:00','2026-01-10 12:00:00','2026-01-09 03:21:23',NULL,14,NULL,5,NULL,37,1),(33,'2026-01-07 14:00:00','2026-01-09 12:00:00','2026-01-09 03:24:53','2026-01-09 03:25:55',14,NULL,5,NULL,38,2),(34,'2026-01-07 14:00:00','2026-01-09 12:00:00','2026-01-09 14:00:00',NULL,44,NULL,5,NULL,39,1),(35,'2026-01-13 14:00:00','2026-01-14 12:00:00',NULL,NULL,25,NULL,2,NULL,40,1),(36,'2026-01-18 14:00:00','2026-01-19 12:00:00','2026-01-10 09:58:56',NULL,25,NULL,1,NULL,41,1);
/*!40000 ALTER TABLE `bookingroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bookingservice`
--

DROP TABLE IF EXISTS `bookingservice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookingservice` (
  `id` int NOT NULL AUTO_INCREMENT,
  `createdTime` datetime DEFAULT NULL,
  `completedTime` datetime DEFAULT NULL,
  `idBookingRoom` int NOT NULL,
  `idService` int NOT NULL,
  `idServicePromotion` int NOT NULL,
  `status` int NOT NULL DEFAULT '0',
  `quantity` int NOT NULL,
  `price` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idBookingRoom` (`idBookingRoom`),
  KEY `idService` (`idService`),
  CONSTRAINT `bookingservice_ibfk_1` FOREIGN KEY (`idBookingRoom`) REFERENCES `bookingroom` (`id`),
  CONSTRAINT `bookingservice_ibfk_2` FOREIGN KEY (`idService`) REFERENCES `service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookingservice`
--

LOCK TABLES `bookingservice` WRITE;
/*!40000 ALTER TABLE `bookingservice` DISABLE KEYS */;
/*!40000 ALTER TABLE `bookingservice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customeridentification`
--

DROP TABLE IF EXISTS `customeridentification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customeridentification` (
  `id` int NOT NULL AUTO_INCREMENT,
  `idUser` int NOT NULL,
  `identificationImage` varchar(255) NOT NULL,
  `expiryDate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idUser` (`idUser`),
  CONSTRAINT `fk_customeridentification_user` FOREIGN KEY (`idUser`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customeridentification`
--

LOCK TABLES `customeridentification` WRITE;
/*!40000 ALTER TABLE `customeridentification` DISABLE KEYS */;
INSERT INTO `customeridentification` VALUES (8,25,'https://res.cloudinary.com/dtwymruih/image/upload/v1768013940/hotel_images/axo0ho4ytax6ypa9zld4.webp',NULL),(9,14,'https://res.cloudinary.com/dtwymruih/image/upload/v1767929095/hotel_images/dozsyr7bympr1be98zum.webp','2026-02-08 03:25:56'),(10,44,'https://res.cloudinary.com/dtwymruih/image/upload/v1767929444/hotel_images/nxd0nbytfgbthyuf0jrz.jpg',NULL),(11,43,'https://res.cloudinary.com/dtwymruih/image/upload/v1767931645/hotel_images/ni2xvu6pp4kppklaohvp.jpg',NULL);
/*!40000 ALTER TABLE `customeridentification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hotel`
--

DROP TABLE IF EXISTS `hotel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hotel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hotel`
--

LOCK TABLES `hotel` WRITE;
/*!40000 ALTER TABLE `hotel` DISABLE KEYS */;
INSERT INTO `hotel` VALUES (1,'Grand Ocean Hotel','20 Phan Chu Trinh, Hoàn Kiếm, Hà Nội','Khách sạn 5 gần trung tâm thành phố');
/*!40000 ALTER TABLE `hotel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `locations`
--

DROP TABLE IF EXISTS `locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `locations` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `thumbnail` varchar(255) DEFAULT NULL,
  `websiteUrl` varchar(255) DEFAULT NULL,
  `idHotel` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_locations_name` (`name`),
  KEY `fk_locations_hotels` (`idHotel`),
  CONSTRAINT `fk_locations_hotels` FOREIGN KEY (`idHotel`) REFERENCES `hotel` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `locations`
--

LOCK TABLES `locations` WRITE;
/*!40000 ALTER TABLE `locations` DISABLE KEYS */;
INSERT INTO `locations` VALUES (1,'Hồ Hoàn Kiếm (Hồ Gươm)','Trái tim của thủ đô Hà Nội, nổi tiếng với Tháp Rùa cổ kính và Đền Ngọc Sơn. Đây là nơi lý tưởng để đi dạo, ngắm cảnh và cảm nhận nhịp sống người Tràng An.','https://tse2.mm.bing.net/th/id/OIP.Aqyq8xSzgdnnRr68rsxJrAHaEK?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3','https://www.vntrip.vn/cam-nang/ho-hoan-kiem-10227',1),(2,'Lăng Chủ tịch Hồ Chí Minh','Nơi an nghỉ vĩnh hằng của Chủ tịch Hồ Chí Minh. Quần thể lăng bao gồm cả Quảng trường Ba Đình lịch sử, Phủ Chủ tịch và Chùa Một Cột.','https://tse4.mm.bing.net/th/id/OIP.Dk7cROYDKnqUgWAhyJcPPQHaFj?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3','https://vinwonders.com/vi/wonderpedia/news/lang-chu-tich-ho-chi-minh/',1),(3,'Văn Miếu - Quốc Tử Giám','Trường đại học đầu tiên của Việt Nam, nơi lưu giữ những giá trị văn hóa, lịch sử và truyền thống hiếu học ngàn đời của dân tộc.','https://images.vietnamtourism.gov.vn/vn/images/2022/thang_5/van_mieu.png','https://vinwonders.com/vi/wonderpedia/news/van-mieu-quoc-tu-giam/',1),(4,'Hoàng Thành Thăng Long','Quần thể di tích gắn với lịch sử kinh thành Thăng Long - Hà Nội, được UNESCO công nhận là Di sản Văn hóa Thế giới.','https://tse1.mm.bing.net/th/id/OIP.pi4kZdFwA8jNX4l4jwiz2QHaFj?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3','https://vinwonders.com/vi/wonderpedia/news/hoang-thanh-thang-long/',1),(5,'Nhà Thờ Lớn Hà Nội','Công trình kiến trúc Gothic đặc sắc nằm giữa lòng phố cổ. Đây là nơi sinh hoạt tín ngưỡng của Tổng giáo phận Hà Nội và là điểm check-in nổi tiếng.','https://th.bing.com/th/id/R.98561a4f3795dd1ec630e225e3c16999?rik=EohNP1VWiGMzkg&pid=ImgRaw&r=0','https://www.traveloka.com/vi-vn/explore/destination/nha-tho-lon-ha-noi-acc/158921',1),(6,'Chùa Trấn Quốc','Ngôi chùa cổ nhất Hà Nội với lịch sử hơn 1500 năm, nằm trên một hòn đảo nhỏ phía Đông Hồ Tây. Nơi đây nổi tiếng với kiến trúc tháp lục độ đài sen tuyệt đẹp và không gian thanh tịnh.','https://static.vinwonders.com/production/chua-tran-quoc-top-banner.jpg','https://www.vntrip.vn/cam-nang/chua-tran-quoc-48887',1),(7,'Nhà Hát Lớn Hà Nội','Công trình kiến trúc Pháp kinh điển được xây dựng từ năm 1901. Đây là thánh đường nghệ thuật của thủ đô, thường xuyên diễn ra các buổi hòa nhạc và biểu diễn nghệ thuật tầm cỡ.','https://tse1.mm.bing.net/th/id/OIP.BAPn2IiRqhnZ2v4Y9ASZ0QHaE7?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3','https://vinwonders.com/vi/wonderpedia/news/nha-hat-lon-ha-noi/',1),(8,'Di tích Nhà tù Hỏa Lò','Được mệnh danh là \"Hanoi Hilton\" thời chiến, nơi đây lưu giữ những chứng tích lịch sử hào hùng và bi tráng. Điểm đến thu hút đông đảo du khách quốc tế muốn tìm hiểu về lịch sử chiến tranh Việt Nam.','https://datviettour.com.vn/uploads/images/tin-tuc-SEO/mien-bac/Ha-noi/danh-thang/nha-tu-hoa-lo.jpg','https://vinpearl.com/vi/nha-tu-hoa-lo',1),(9,'Bảo tàng Dân tộc học Việt Nam','Nơi trưng bày và lưu giữ những giá trị văn hóa của 54 dân tộc anh em. Điểm nhấn là khu trưng bày ngoài trời với các ngôi nhà kiến trúc độc đáo như nhà Rông, nhà Dài, nhà Trình Tường.','https://static.vinwonders.com/production/bao-tang-dan-toc-hoc-1.jpg','https://www.ivivu.com/blog/2023/10/bao-tang-dan-toc-hoc-viet-nam-diem-hoi-tu-tinh-hoa-van-hoa-54-dan-toc-anh-em/',1),(10,'Chợ Đồng Xuân','Khu chợ đầu mối lớn nhất và lâu đời nhất trong khu phố cổ Hà Nội. Nơi giao thương sầm uất, du khách có thể tìm thấy đủ loại hàng hóa từ vải vóc, quà lưu niệm đến ẩm thực đường phố đặc trưng.','https://tse2.mm.bing.net/th/id/OIP.QHj-rXo8woDPnI5ppooU-gHaEo?cb=ucfimg2&ucfimg=1&rs=1&pid=ImgDetMain&o=7&rm=3','https://vinwonders.com/vi/wonderpedia/news/kham-pha-cho-dong-xuan/',1);
/*!40000 ALTER TABLE `locations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentmethod`
--

DROP TABLE IF EXISTS `paymentmethod`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paymentmethod` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentmethod`
--

LOCK TABLES `paymentmethod` WRITE;
/*!40000 ALTER TABLE `paymentmethod` DISABLE KEYS */;
INSERT INTO `paymentmethod` VALUES (1,'QR Banking','Thanh toán bằng chuyển khoản'),(2,'Cash','Thanh toán tiền mặt');
/*!40000 ALTER TABLE `paymentmethod` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `paymentstatus`
--

DROP TABLE IF EXISTS `paymentstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `paymentstatus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `paymentstatus`
--

LOCK TABLES `paymentstatus` WRITE;
/*!40000 ALTER TABLE `paymentstatus` DISABLE KEYS */;
INSERT INTO `paymentstatus` VALUES (1,'Pending','Chưa thanh toán'),(2,'Completed','Đã thanh toán'),(3,'Canceled','Đã hủy'),(4,'Checked-in','Đã check-in');
/*!40000 ALTER TABLE `paymentstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `promotion`
--

DROP TABLE IF EXISTS `promotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `promotion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `discount` float NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `isActive` tinyint(1) NOT NULL,
  `isManuallyDisabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `promotion`
--

LOCK TABLES `promotion` WRITE;
/*!40000 ALTER TABLE `promotion` DISABLE KEYS */;
INSERT INTO `promotion` VALUES (1,'Winter Sale','Giảm giá 20% cho tất cả phòng Deluxe','https://res.cloudinary.com/dtwymruih/image/upload/v1767893200/hotel_images/u3bri3xm4tad2deydrcc.jpg',20,'2025-05-28 00:00:00','2026-02-27 00:00:00',1,0),(10,'TET SALE','','https://res.cloudinary.com/dtwymruih/image/upload/v1767893324/hotel_images/bbo9gfq9zib0scc9htgm.jpg',5,'2026-01-01 00:00:00','2026-02-01 00:00:00',1,0),(11,'TET SALE','','https://res.cloudinary.com/dtwymruih/image/upload/v1767893371/hotel_images/jjhryd5hbtcgdb2dxs6r.jpg',9.9,'2025-12-30 00:00:00','2026-01-31 00:00:00',1,0),(12,'TET SALE','','https://res.cloudinary.com/dtwymruih/image/upload/v1767893445/hotel_images/byap1yhh53ucztuwnmxc.jpg',20,'2026-01-01 00:00:00','2026-02-01 00:00:00',1,0),(13,'SUMMER SALE','','https://res.cloudinary.com/dtwymruih/image/upload/v1767893538/hotel_images/btahnpfqxb9hkchsd3hd.jpg',20,'2025-04-01 00:00:00','2025-04-20 00:00:00',0,0),(14,'SUMMER SALE','','https://res.cloudinary.com/dtwymruih/image/upload/v1767893602/hotel_images/shyzbiwbepjqorvl9u2o.jpg',15,'2026-01-10 00:00:00','2026-01-20 00:00:00',1,0);
/*!40000 ALTER TABLE `promotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `star` int DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `day` datetime NOT NULL,
  `idCustomer` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idCustomer` (`idCustomer`),
  CONSTRAINT `review_ibfk_1` FOREIGN KEY (`idCustomer`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (6,'khach san dep de, thoang mat',5,'HOTEL','2026-01-09 02:43:45',25),(7,'phong oc dep de, nhung co 1 chut gi do hoi bi',3,'ROOM','2026-01-09 02:44:13',25),(8,'khong ra gi',2,'HOTEL','2026-01-09 02:44:26',25);
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviewimage`
--

DROP TABLE IF EXISTS `reviewimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviewimage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `src` varchar(255) NOT NULL,
  `idReview` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idReview` (`idReview`),
  CONSTRAINT `reviewimage_ibfk_1` FOREIGN KEY (`idReview`) REFERENCES `review` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviewimage`
--

LOCK TABLES `reviewimage` WRITE;
/*!40000 ALTER TABLE `reviewimage` DISABLE KEYS */;
/*!40000 ALTER TABLE `reviewimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'Admin','Quản trị hệ thống + khách sạn'),(2,'Staff','Nhân viên dọn phòng'),(3,'Customer','Khách hàng'),(4,'Housekeeper','Nhân viên dọn phòng'),(5,'test','Khách hàng');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `room`
--

DROP TABLE IF EXISTS `room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `room` (
  `id` int NOT NULL AUTO_INCREMENT,
  `roomNumber` int NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `idHotel` int NOT NULL,
  `idRoomStatus` int NOT NULL,
  `idRoomType` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idRoomStatus` (`idRoomStatus`),
  KEY `idRoomType` (`idRoomType`),
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`idRoomStatus`) REFERENCES `roomstatus` (`id`),
  CONSTRAINT `room_ibfk_2` FOREIGN KEY (`idRoomType`) REFERENCES `roomtype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `room`
--

LOCK TABLES `room` WRITE;
/*!40000 ALTER TABLE `room` DISABLE KEYS */;
INSERT INTO `room` VALUES (1,101,'Phòng tầng 1',1,2,1),(2,102,'Phòng tầng 1',1,1,1),(3,201,'Phòng tầng 2 view biển',1,2,2),(4,301,'Phòng tầng 3 dành cho gia đình',1,1,3),(5,999,'TEST TEST',1,2,9),(9,401,'',1,1,4),(12,402,'',1,1,4),(13,403,'',1,1,4),(14,501,'',1,1,5),(15,502,'',1,1,5),(16,503,'',1,1,5),(18,202,'',1,1,2),(19,203,'',1,1,2),(20,302,'',1,1,3),(21,303,'',1,1,3),(22,601,'',1,1,6),(23,701,'',1,1,7),(24,702,'',1,1,7),(25,703,'',1,1,7),(26,801,'',1,1,8),(27,802,'',1,1,8),(28,803,'',1,1,8),(29,602,'',1,1,6),(31,901,'',1,1,13),(32,902,'',1,1,13),(33,1001,'',1,2,14);
/*!40000 ALTER TABLE `room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roomimage`
--

DROP TABLE IF EXISTS `roomimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roomimage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `src` varchar(255) NOT NULL,
  `idRoom` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idRoom` (`idRoom`),
  CONSTRAINT `roomimage_ibfk_1` FOREIGN KEY (`idRoom`) REFERENCES `room` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomimage`
--

LOCK TABLES `roomimage` WRITE;
/*!40000 ALTER TABLE `roomimage` DISABLE KEYS */;
/*!40000 ALTER TABLE `roomimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roompromotion`
--

DROP TABLE IF EXISTS `roompromotion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roompromotion` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `idPromotion` int NOT NULL,
  `idRoomType` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idPromotion` (`idPromotion`),
  KEY `idRoomType` (`idRoomType`),
  CONSTRAINT `roompromotion_ibfk_1` FOREIGN KEY (`idPromotion`) REFERENCES `promotion` (`id`),
  CONSTRAINT `roompromotion_ibfk_2` FOREIGN KEY (`idRoomType`) REFERENCES `roomtype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roompromotion`
--

LOCK TABLES `roompromotion` WRITE;
/*!40000 ALTER TABLE `roompromotion` DISABLE KEYS */;
INSERT INTO `roompromotion` VALUES (10,'Giảm giá 20% cho tất cả phòng Deluxe',1,6),(12,'',10,1),(13,'',11,2),(14,'',12,7),(16,'',13,5),(17,'',14,8);
/*!40000 ALTER TABLE `roompromotion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roomstatus`
--

DROP TABLE IF EXISTS `roomstatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roomstatus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomstatus`
--

LOCK TABLES `roomstatus` WRITE;
/*!40000 ALTER TABLE `roomstatus` DISABLE KEYS */;
INSERT INTO `roomstatus` VALUES (1,'Available','Phòng trống sẵn sàng cho thuê'),(2,'Occupied','Phòng đang có khách'),(3,'Cleaning','Đang được dọn dẹp'),(4,'Maintenance','Đang bảo trì');
/*!40000 ALTER TABLE `roomstatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roomtype`
--

DROP TABLE IF EXISTS `roomtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roomtype` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `bedCount` int NOT NULL,
  `maxOccupancy` int NOT NULL,
  `price` float NOT NULL,
  `area` float NOT NULL,
  `isPrivateBathroom` tinyint(1) NOT NULL,
  `isFreeToiletries` tinyint(1) NOT NULL,
  `isAirConditioning` tinyint(1) NOT NULL,
  `isSoundproofing` tinyint(1) NOT NULL,
  `isTV` tinyint(1) NOT NULL,
  `isMiniBar` tinyint(1) NOT NULL,
  `isWorkDesk` tinyint(1) NOT NULL,
  `isSeatingArea` tinyint(1) NOT NULL,
  `isSafetyFeatures` tinyint(1) NOT NULL,
  `isSmoking` tinyint(1) NOT NULL,
  `isDeleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roomtype`
--

LOCK TABLES `roomtype` WRITE;
/*!40000 ALTER TABLE `roomtype` DISABLE KEYS */;
INSERT INTO `roomtype` VALUES (1,'Suite Junior','Suite có phòng tắm riêng với vòi sen, chậu rửa vệ sinh (bidet), máy sấy tóc, đồ vệ sinh cá nhân và áo choàng tắm miễn phí. Phòng trang bị điều hòa, TV màn hình phẳng, minibar, ấm đun nước điện, yukata và 1 giường.',1,2,2500000,18,1,1,1,0,1,0,1,1,1,0,0),(2,'Suite Executive','Phòng ngủ giường lớn và 1 phòng tắm riêng với bồn và vòi sen. Trang bị tường cách âm, minibar, khu ghế ngồi, ấm điện, TV màn hình phẳng. Phòng có 1 giường, không gian thoải mái và tiện nghi.',1,2,3200000,20,1,1,1,1,1,1,1,1,1,0,0),(3,'Suite Có Giường Cỡ King','Suite điều hòa gồm 1 phòng ngủ và 1 phòng tắm với bồn và vòi sen. Trang bị TV màn hình phẳng, tường cách âm, minibar, khu ghế ngồi và view thành phố. Phòng có 1 giường lớn.',1,2,3500000,35,1,1,1,1,1,0,1,1,1,1,0),(4,'Suite Gia Đình Có Ban Công','Phòng gia đình điều hòa gồm 2 giường và phòng tắm riêng với vòi sen, bệ rửa vệ sinh, máy sấy tóc. Trang bị TV màn hình phẳng, tường cách âm, minibar, khu ghế ngồi và view thành phố. Có đồ vệ sinh và áo choàng tắm miễn phí.',2,4,4000000,35,1,1,1,1,1,1,0,1,1,0,0),(5,'Suite Gia Đình (2 phòng ngủ)','Suite điều hòa gồm 2 phòng ngủ và 2 phòng tắm với bồn và vòi sen. Trang bị tường cách âm, minibar, khu ghế ngồi, TV màn hình phẳng và view thành phố. Phòng có 3 giường tiện nghi',3,5,4800000,46,1,1,1,1,1,1,1,1,1,1,1),(6,'Suite Hạng Tổng Thống','Suite rộng rãi điều hòa gồm 1 phòng ngủ và 1 phòng tắm với bồn và đồ vệ sinh miễn phí. Trang bị TV màn hình phẳng, tường cách âm, minibar, khu ghế ngồi và view phố. Phòng có 1 giường đôi cực lớn.',1,2,8000000,65,1,1,1,1,1,1,1,1,1,1,0),(7,'Phòng 2 Giường Đơn Có Bồn Tắm','Phòng twin điều hòa với 2 giường và phòng tắm riêng gồm bồn, vòi sen, bệ rửa vệ sinh. Có TV màn hình phẳng, tường cách âm, minibar, khu ghế ngồi, ấm điện, yukata, đồ vệ sinh và áo choàng tắm miễn phí, không gian tiện nghi.',2,2,2800000,20,1,1,1,0,1,0,1,1,1,0,0),(8,'Suite Nhìn Ra Thành Phố','Suite điều hòa với 1 giường đôi cực lớn và phòng tắm gồm bồn, vòi sen, bệ rửa vệ sinh. Có TV màn hình phẳng, tường cách âm, minibar, khu ghế ngồi, view vườn, đồ vệ sinh và áo choàng tắm miễn phí, không gian tiện nghi và thoải mái.',1,2,3500000,35,1,1,1,0,1,1,1,1,1,1,0),(9,'VIP','VIP',4,6,1000,80,1,1,1,1,1,1,1,1,1,1,0),(13,'Phòng Tiệc Cưới','Phòng tổ chức làm tiệc cưới sang trọng.',0,100,10000000,199,0,0,1,1,1,1,0,1,1,1,0),(14,'Phòng Họp Lớn','Phòng họp lớn, đầy đủ trang thiết bị.',0,20,5000000,100,0,0,1,1,1,0,1,0,0,0,0);
/*!40000 ALTER TABLE `roomtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `price` float NOT NULL,
  `isAvaiable` int NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `idServiceCategory` int NOT NULL,
  `idHotel` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idServiceCategory` (`idServiceCategory`),
  KEY `idHotel` (`idHotel`),
  CONSTRAINT `fk_service_category` FOREIGN KEY (`idServiceCategory`) REFERENCES `servicecategory` (`id`),
  CONSTRAINT `fk_service_hotel` FOREIGN KEY (`idHotel`) REFERENCES `hotel` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (3,'Miễn phí bữa sáng buffet','Bao gồm buffet mỗi ngày cho 2 khách, thực đơn Âu - Á phong phú.',0,1,'Suất',NULL,3,1),(4,'Free coffee & tea sáng','\'Cà phê rang xay và trà thảo mộc, phục vụ từ 6:00 - 10:00.',0,1,'Cốc',NULL,3,1),(5,'Đỗ xe miễn phí tại chỗ','Bãi xe trong khuôn viên, bảo vệ 24/7, ra/vào nhiều lần.',0,1,'Suất',50,4,1),(6,'Miễn phí gửi xe qua đêm','Chỗ để ô tô và xe máy tiêu chuẩn, có mái che.',0,1,'Suất',50,4,1),(7,'Phòng gym miễn phí 24/7','Máy chạy bộ, tạ đơn, khu functional; khăn và nước suối',0,1,'',NULL,5,1),(8,'Lớp HIIT buổi sáng miễn phí','Hướng dẫn viên nội bộ, lớp 30 phút mỗi ngày 7:00.',0,1,'Suất',NULL,5,1),(9,'Vào hồ bơi miễn phí','Hồ bơi ngoài trời, khăn tắm và ghế nằm kèm theo.',0,1,'Lượt',NULL,6,1),(10,'Miễn phí trông giữ cơ bản','Khu vui chơi riêng, thảm nằm và bát ăn cho thú cưng.',0,1,'Lượt',NULL,8,1),(11,'Miễn phí vệ sinh nhẹ cho thú cưng','Khăn ướt, lược chải và góc tắm nhanh cho pet.',0,1,'Lượt',NULL,8,1),(15,'Dịch Vụ Làm Móng & Chăm Sóc Da','Thư giãn với dịch vụ làm móng và chăm sóc da tay, da chân cao cấp. Sử dụng sản phẩm hữu cơ, dụng cụ tiệt trùng an toàn, kết hợp massage nhẹ giúp phục hồi năng lượng và mang lại cảm giác thư thái tuyệt đối.',250000,1,'Lần',NULL,1,1),(16,'Phòng Xông Hơi Đá Muối','Phòng xông hơi hiện đại kết hợp công nghệ hồng ngoại và đá muối Himalaya giúp thanh lọc cơ thể, giảm căng thẳng và cải thiện tuần hoàn máu. Dịch vụ lý tưởng để thư giãn sau một ngày dài hoạt động.',400000,1,'Lần',NULL,1,1);
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `servicecategory`
--

DROP TABLE IF EXISTS `servicecategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `servicecategory` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `servicecategory`
--

LOCK TABLES `servicecategory` WRITE;
/*!40000 ALTER TABLE `servicecategory` DISABLE KEYS */;
INSERT INTO `servicecategory` VALUES (1,'Spa','Dịch vụ massage, xông hơi'),(2,'Food','Đồ ăn, thức uống'),(3,'Bữa sáng','Miễn phí bữa sáng'),(4,'Chỗ để xe','Chỗ để xe miễn phí'),(5,'Phòng gym','Phòng gym miễn phí'),(6,'Hồ bơi','Hồ bơi miễn phí'),(8,'Thú cưng','Chăm sóc thú cưng miễn phí'),(9,'Giải Trí','Các dịch vụ giải trí bên lề của khách sạn');
/*!40000 ALTER TABLE `servicecategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serviceimage`
--

DROP TABLE IF EXISTS `serviceimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `serviceimage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `src` varchar(255) NOT NULL,
  `idService` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idService` (`idService`),
  CONSTRAINT `serviceimage_ibfk_1` FOREIGN KEY (`idService`) REFERENCES `service` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serviceimage`
--

LOCK TABLES `serviceimage` WRITE;
/*!40000 ALTER TABLE `serviceimage` DISABLE KEYS */;
INSERT INTO `serviceimage` VALUES (1,'Ảnh cho service Miễn phí bữa sáng buffet','https://res.cloudinary.com/dtwymruih/image/upload/v1762745276/hotel_images/yf777j7pcancnfd8bjpm.jpg',3),(2,'Ảnh cho service Free coffee & tea sáng','https://res.cloudinary.com/dtwymruih/image/upload/v1762745535/hotel_images/kddcg83xqelke9g2epw4.webp',4),(3,'Ảnh cho service Đỗ xe miễn phí tại chỗ','https://res.cloudinary.com/dtwymruih/image/upload/v1762745966/hotel_images/sbv4dpzsvvsfqjmrzp7p.png',5),(4,'Ảnh cho service Miễn phí gửi xe qua đêm','https://res.cloudinary.com/dtwymruih/image/upload/v1762746057/hotel_images/fx2qrz9sepcxpyhwfrus.jpg',6),(5,'Ảnh cho service Phòng gym miễn phí 24/7','https://res.cloudinary.com/dtwymruih/image/upload/v1762746261/hotel_images/tyjbg1jypog219lmntmf.webp',7),(6,'Ảnh cho service Lớp HIIT buổi sáng miễn phí','https://res.cloudinary.com/dtwymruih/image/upload/v1762746775/hotel_images/lcee7cfwmxyk0oupc9ik.jpg',8),(7,'Ảnh cho service Vào hồ bơi miễn phí','https://res.cloudinary.com/dtwymruih/image/upload/v1762748406/hotel_images/dfbfv7geom3rufju338z.jpg',9),(8,'Ảnh cho service Miễn phí trông giữ cơ bản','https://res.cloudinary.com/dtwymruih/image/upload/v1762748648/hotel_images/aiahdomyfp6ijowawpgq.png',10),(9,'Ảnh cho service Miễn phí vệ sinh nhẹ cho thú cưng','https://res.cloudinary.com/dtwymruih/image/upload/v1762748728/hotel_images/zoxtllpv4wnbmwdyv1fi.png',11),(13,'Ảnh cho service Dịch Vụ Làm Móng & Chăm Sóc Da','https://res.cloudinary.com/dtwymruih/image/upload/v1762938754/hotel_images/zhbga9bumpprsgmxi9vf.png',15),(14,'Ảnh cho service Phòng Xông Hơi Đá Muối','https://res.cloudinary.com/dtwymruih/image/upload/v1762938918/hotel_images/izplpqbrpgi4cx68dwh2.png',16);
/*!40000 ALTER TABLE `serviceimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift`
--

DROP TABLE IF EXISTS `shift`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shift` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `startTime` time NOT NULL,
  `endTime` time NOT NULL,
  `details` varchar(255) DEFAULT NULL,
  `isActive` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift`
--

LOCK TABLES `shift` WRITE;
/*!40000 ALTER TABLE `shift` DISABLE KEYS */;
INSERT INTO `shift` VALUES (2,'Ca sáng','08:30:00','17:00:00','Nghỉ trưa 30p',1),(3,'Ca chiều','16:30:00','22:00:00','Hỗ trợ ăn tối',1),(4,'Ca tối','22:00:00','06:00:00','Hỗ trợ tiền ca đêm',1);
/*!40000 ALTER TABLE `shift` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shifting`
--

DROP TABLE IF EXISTS `shifting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `shifting` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `day` date NOT NULL,
  `idEmployee` int NOT NULL,
  `idShift` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idEmployee` (`idEmployee`),
  KEY `idShift` (`idShift`),
  CONSTRAINT `shifting_ibfk_1` FOREIGN KEY (`idEmployee`) REFERENCES `users` (`id`),
  CONSTRAINT `shifting_ibfk_2` FOREIGN KEY (`idShift`) REFERENCES `shift` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shifting`
--

LOCK TABLES `shifting` WRITE;
/*!40000 ALTER TABLE `shifting` DISABLE KEYS */;
INSERT INTO `shifting` VALUES (13,'','2026-01-10',14,2),(14,'','2026-01-10',26,3);
/*!40000 ALTER TABLE `shifting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `typeimage`
--

DROP TABLE IF EXISTS `typeimage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `typeimage` (
  `id` int NOT NULL AUTO_INCREMENT,
  `details` varchar(255) DEFAULT NULL,
  `src` varchar(255) NOT NULL,
  `idRoomType` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idRoomType` (`idRoomType`),
  CONSTRAINT `typeimage_ibfk_1` FOREIGN KEY (`idRoomType`) REFERENCES `roomtype` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typeimage`
--

LOCK TABLES `typeimage` WRITE;
/*!40000 ALTER TABLE `typeimage` DISABLE KEYS */;
INSERT INTO `typeimage` VALUES (10,'Ảnh mới của loại phòng Suite Junior','https://res.cloudinary.com/dtwymruih/image/upload/v1762761153/hotel_images/qscvqhx4o3vkp1cih8y6.jpg',1),(11,'Ảnh mới của loại phòng Suite Junior','https://res.cloudinary.com/dtwymruih/image/upload/v1762761154/hotel_images/dbz8kvl8npjlea6qzhkn.jpg',1),(12,'Ảnh mới của loại phòng Suite Junior','https://res.cloudinary.com/dtwymruih/image/upload/v1762761156/hotel_images/of1o6qgspwrlhb8tetci.jpg',1),(13,'Ảnh mới của loại phòng Suite Junior','https://res.cloudinary.com/dtwymruih/image/upload/v1762761158/hotel_images/j5oinklvxd53ddlyrcug.jpg',1),(14,'Ảnh mới của loại phòng Suite Executive','https://res.cloudinary.com/dtwymruih/image/upload/v1762761593/hotel_images/h4kgxijqrfe5iyxkjs4x.jpg',2),(15,'Ảnh mới của loại phòng Suite Executive','https://res.cloudinary.com/dtwymruih/image/upload/v1762761595/hotel_images/iqdov2yo46padkyq2tga.jpg',2),(16,'Ảnh mới của loại phòng Suite Executive','https://res.cloudinary.com/dtwymruih/image/upload/v1762761596/hotel_images/iif1dncql294zjmyzm03.jpg',2),(17,'Ảnh mới của loại phòng Suite Executive','https://res.cloudinary.com/dtwymruih/image/upload/v1762761598/hotel_images/ezks7acvwcztyibotndt.jpg',2),(18,'Ảnh mới của loại phòng Suite Executive','https://res.cloudinary.com/dtwymruih/image/upload/v1762761599/hotel_images/lopdjyfxdezakbre9ed7.jpg',2),(19,'Ảnh mới của loại phòng Suite Executive','https://res.cloudinary.com/dtwymruih/image/upload/v1762761601/hotel_images/o0zuwnfgoukaxngv4ow3.jpg',2),(20,'Ảnh mới của loại phòng Suite Có Giường Cỡ King','https://res.cloudinary.com/dtwymruih/image/upload/v1762761829/hotel_images/mgkzbsingq0vyfgehzy0.jpg',3),(21,'Ảnh mới của loại phòng Suite Có Giường Cỡ King','https://res.cloudinary.com/dtwymruih/image/upload/v1762761831/hotel_images/bhjl7pp5ldz0uswbisu5.jpg',3),(22,'Ảnh mới của loại phòng Suite Có Giường Cỡ King','https://res.cloudinary.com/dtwymruih/image/upload/v1762761832/hotel_images/vezbxtj0esxxi5xf7lze.jpg',3),(23,'Ảnh mới của loại phòng Suite Có Giường Cỡ King','https://res.cloudinary.com/dtwymruih/image/upload/v1762761834/hotel_images/quf2vihmioycvmddaftl.jpg',3),(24,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762077/hotel_images/gbjwkzcgamlntxn3drkr.jpg',4),(25,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762079/hotel_images/xcitzzlmb2opbxnbwoqg.jpg',4),(26,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762081/hotel_images/gam53d7yvhqguk7d66nu.jpg',4),(27,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762083/hotel_images/yaolroqbt768agaxsjrg.jpg',4),(28,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762085/hotel_images/ab7cpvyqdpveranmwpaz.jpg',4),(29,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762087/hotel_images/ptzpuznt6qahjxjznkel.jpg',4),(30,'Ảnh mới của loại phòng Suite Gia Đình Có Ban Công','https://res.cloudinary.com/dtwymruih/image/upload/v1762762089/hotel_images/znxeaozyjbb65ptm1xec.jpg',4),(31,'Ảnh mới của loại phòng Suite Gia Đình (2 phòng ngủ)','https://res.cloudinary.com/dtwymruih/image/upload/v1762762335/hotel_images/eocyfejgosb3tw4c0a2q.jpg',5),(32,'Ảnh mới của loại phòng Suite Gia Đình (2 phòng ngủ)','https://res.cloudinary.com/dtwymruih/image/upload/v1762762337/hotel_images/cnr3acjx5bvr1dowxttj.jpg',5),(33,'Ảnh mới của loại phòng Suite Gia Đình (2 phòng ngủ)','https://res.cloudinary.com/dtwymruih/image/upload/v1762762338/hotel_images/e6h0p6e09egckzo5m6fg.jpg',5),(34,'Ảnh mới của loại phòng Suite Gia Đình (2 phòng ngủ)','https://res.cloudinary.com/dtwymruih/image/upload/v1762762340/hotel_images/ipjlhtrzcxvtfrpf4xqc.jpg',5),(35,'Ảnh mới của loại phòng Suite Hạng Tổng Thống','https://res.cloudinary.com/dtwymruih/image/upload/v1762762535/hotel_images/e5sn5squondfwjkjz5bn.jpg',6),(36,'Ảnh mới của loại phòng Suite Hạng Tổng Thống','https://res.cloudinary.com/dtwymruih/image/upload/v1762762536/hotel_images/dk2zcqu5juy7gvc62dn9.jpg',6),(37,'Ảnh mới của loại phòng Suite Hạng Tổng Thống','https://res.cloudinary.com/dtwymruih/image/upload/v1762762538/hotel_images/tgvjtvxswixjn5f3c6to.jpg',6),(38,'Ảnh mới của loại phòng Suite Hạng Tổng Thống','https://res.cloudinary.com/dtwymruih/image/upload/v1762762540/hotel_images/j8eidsw2c6xt0s2avjgt.jpg',6),(39,'Ảnh mới của loại phòng Suite Hạng Tổng Thống','https://res.cloudinary.com/dtwymruih/image/upload/v1762762542/hotel_images/yjc0lx4gfu5bbdp2zubm.jpg',6),(40,'Ảnh mới của loại phòng Suite Hạng Tổng Thống','https://res.cloudinary.com/dtwymruih/image/upload/v1762762543/hotel_images/f6hibjtujlgclaqjtwlu.jpg',6),(41,'Ảnh mới của loại phòng Phòng 2 Giường Đơn Có Bồn Tắm','https://res.cloudinary.com/dtwymruih/image/upload/v1762762712/hotel_images/uhqkq3htyu7zb1a3cjen.jpg',7),(42,'Ảnh mới của loại phòng Phòng 2 Giường Đơn Có Bồn Tắm','https://res.cloudinary.com/dtwymruih/image/upload/v1762762713/hotel_images/yq0fzs9i5u8dvysvlhhn.jpg',7),(43,'Ảnh mới của loại phòng Phòng 2 Giường Đơn Có Bồn Tắm','https://res.cloudinary.com/dtwymruih/image/upload/v1762762715/hotel_images/tyxit6pef2pddkvzk7dy.jpg',7),(44,'Ảnh mới của loại phòng Phòng 2 Giường Đơn Có Bồn Tắm','https://res.cloudinary.com/dtwymruih/image/upload/v1762762716/hotel_images/hjch723hyadxyp5kfi9u.jpg',7),(45,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762905/hotel_images/olkot02bje0zy9cyd0pk.jpg',8),(46,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762907/hotel_images/ajmtiuv9ixfog11lu2xb.jpg',8),(47,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762909/hotel_images/agzuii5sw8lrazdxiept.jpg',8),(48,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762910/hotel_images/uag84nolztabftfkpm5u.jpg',8),(49,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762912/hotel_images/kaqjfzrwbx8u0xnsg3mq.jpg',8),(50,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762914/hotel_images/qqlp0fqy8ydbrheeezfn.jpg',8),(51,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762916/hotel_images/pjqtnowpnwsqghswspj6.jpg',8),(52,'Ảnh mới của loại phòng Suite Nhìn Ra Thành Phố','https://res.cloudinary.com/dtwymruih/image/upload/v1762762917/hotel_images/erl3mrqxhebfz9vmcfjb.jpg',8),(56,'Ảnh của loại phòng Phòng Tiệc Cưới ','https://res.cloudinary.com/dtwymruih/image/upload/v1767892587/hotel_images/bkapkbfmgnlib9to2uxw.jpg',13),(57,'Ảnh của loại phòng Phòng Tiệc Cưới ','https://res.cloudinary.com/dtwymruih/image/upload/v1767892590/hotel_images/fr1opowyhevbsxvdfwgl.jpg',13),(58,'Ảnh của loại phòng Phòng Họp Lớn','https://res.cloudinary.com/dtwymruih/image/upload/v1767892789/hotel_images/wsp8weujbcvgem1l9wbk.webp',14),(59,'Ảnh của loại phòng Phòng Họp Lớn','https://res.cloudinary.com/dtwymruih/image/upload/v1767892791/hotel_images/nbirmb0piznivb01fy6o.webp',14);
/*!40000 ALTER TABLE `typeimage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `identification` varchar(12) NOT NULL,
  `phone` varchar(10) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `idAccount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idAccount` (`idAccount`),
  CONSTRAINT `users_ibfk_1` FOREIGN KEY (`idAccount`) REFERENCES `account` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (7,'admin','001203040506','0987765445','Nam','Hà Nội','2025-10-31',7),(8,'Vu Minh Quan','009887677760','0867989031','Nam','Hanoi','2003-03-30',8),(14,'Ngô Lỗi','001203015236','0912471495','Nam','HN','2003-11-10',19),(25,'Quan','001204056857','0987765556','Nam','HN','2003-12-16',20),(26,'Nhân Viên','009876567655','0987787665','Nam','Hà Nội','2003-03-11',21),(27,'Hai','009876765656','0987765500','Nam','HN','2003-12-17',23),(33,'Grindelwall','012345678977','0111111111',NULL,NULL,NULL,NULL),(34,'matilda','030203882747','0123455666',NULL,NULL,NULL,NULL),(35,'Hải','000000000000','0000000000',NULL,NULL,NULL,NULL),(43,'Minh Quân','123456789987','0987789776',NULL,NULL,NULL,NULL),(44,'Linh Linh','001203015217','0943298199',NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'railway'
--

--
-- Dumping routines for database 'railway'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-01-11 23:36:46
