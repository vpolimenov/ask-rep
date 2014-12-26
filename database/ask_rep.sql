CREATE DATABASE  IF NOT EXISTS `ask_rep` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `ask_rep`;
-- MySQL dump 10.13  Distrib 5.5.40, for debian-linux-gnu (x86_64)
--
-- Host: 127.0.0.1    Database: ask_rep
-- ------------------------------------------------------
-- Server version	5.5.40-0ubuntu0.14.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `files`
--

DROP TABLE IF EXISTS `files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `files` (
  `fileID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `extension` varchar(10) NOT NULL,
  `fileContent` blob,
  `folderID` int(11) DEFAULT NULL,
  `repositoryID` int(11) NOT NULL,
  `datecreated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`fileID`),
  UNIQUE KEY `fileID_UNIQUE` (`fileID`),
  KEY `fk_files_folders_idx` (`folderID`),
  KEY `fk_files_repositories_idx` (`repositoryID`),
  CONSTRAINT `fk_files_folders` FOREIGN KEY (`folderID`) REFERENCES `folders` (`folderID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_files_repositories` FOREIGN KEY (`repositoryID`) REFERENCES `repositories` (`repositoryID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
/*!40000 ALTER TABLE `files` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `folders`
--

DROP TABLE IF EXISTS `folders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `folders` (
  `folderID` int(11) NOT NULL AUTO_INCREMENT,
  `parentFolderID` int(11) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `repositoryID` int(11) NOT NULL,
  `datecreated` timestamp NULL DEFAULT NULL,
  `dateupdated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`folderID`),
  UNIQUE KEY `folderID_UNIQUE` (`folderID`),
  KEY `fk_folders_repositories_idx` (`repositoryID`),
  KEY `fk_folders_folders_idx` (`parentFolderID`),
  CONSTRAINT `fk_folders_folders` FOREIGN KEY (`parentFolderID`) REFERENCES `folders` (`folderID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_folders_repositories` FOREIGN KEY (`repositoryID`) REFERENCES `repositories` (`repositoryID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folders`
--

LOCK TABLES `folders` WRITE;
/*!40000 ALTER TABLE `folders` DISABLE KEYS */;
INSERT INTO `folders` VALUES (1,NULL,'folder 1',3,'2014-12-26 08:56:01','2014-12-26 08:56:01'),(2,NULL,'folder 2',3,'2014-12-26 08:56:14','2014-12-26 08:56:14'),(3,NULL,'folder 1',4,'2014-12-26 09:42:18','2014-12-26 09:42:32'),(4,NULL,'folder 2',4,'2014-12-26 09:42:25','2014-12-26 09:42:25'),(5,3,'folder 3',4,'2014-12-26 09:42:32','2014-12-26 09:42:32'),(6,NULL,'folder 1',5,'2014-12-26 11:22:35','2014-12-26 11:22:53'),(7,6,'folder 3',5,'2014-12-26 11:22:53','2014-12-26 11:22:53'),(8,NULL,'folder 2',5,'2014-12-26 11:31:52','2014-12-26 11:31:52'),(9,NULL,'folder 4',5,'2014-12-26 11:35:54','2014-12-26 11:35:54'),(10,NULL,'folder 5',5,'2014-12-26 11:37:48','2014-12-26 11:37:48'),(11,NULL,'folder 6',5,'2014-12-26 11:41:29','2014-12-26 11:41:42'),(12,11,'sub folder',5,'2014-12-26 11:41:42','2014-12-26 11:41:42');
/*!40000 ALTER TABLE `folders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repositories`
--

DROP TABLE IF EXISTS `repositories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repositories` (
  `repositoryID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `userID` int(11) NOT NULL,
  `datecreated` timestamp NULL DEFAULT NULL,
  `dateupdated` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`repositoryID`),
  UNIQUE KEY `repositoryID_UNIQUE` (`repositoryID`),
  KEY `fk_repositories_users_idx` (`userID`),
  CONSTRAINT `fk_repositories_users` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repositories`
--

LOCK TABLES `repositories` WRITE;
/*!40000 ALTER TABLE `repositories` DISABLE KEYS */;
INSERT INTO `repositories` VALUES (3,'personal',5,'2014-12-26 08:55:55','2014-12-26 08:56:14'),(4,'personal 2',5,'2014-12-26 09:42:14','2014-12-26 09:42:32'),(5,'my rep',4,'2014-12-26 11:22:30','2014-12-26 11:41:42');
/*!40000 ALTER TABLE `repositories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `userID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(1000) NOT NULL,
  PRIMARY KEY (`userID`),
  UNIQUE KEY `userID_UNIQUE` (`userID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (4,'dn14500@my.bristol.ac.uk','dn14500@my.bristol.ac.uk'),(5,'dhiraj.narwani','dhiraj.narwani@gmail.com'),(6,'test3424@example.com','test3424@example.com');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-12-26 11:43:50
