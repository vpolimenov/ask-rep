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
  `datecreated` datetime NOT NULL,
  PRIMARY KEY (`fileID`),
  UNIQUE KEY `fileID_UNIQUE` (`fileID`),
  KEY `fk_files_folders_idx` (`folderID`),
  KEY `fk_files_repositories_idx` (`repositoryID`),
  CONSTRAINT `fk_files_folders` FOREIGN KEY (`folderID`) REFERENCES `folders` (`folderID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_files_repositories` FOREIGN KEY (`repositoryID`) REFERENCES `repositories` (`repositoryID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `files`
--

LOCK TABLES `files` WRITE;
/*!40000 ALTER TABLE `files` DISABLE KEYS */;
INSERT INTO `files` VALUES (3,'file1','.java',NULL,6,1,'0000-00-00 00:00:00'),(4,'file2','.c',NULL,NULL,1,'2014-04-12 00:00:00');
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
  `datecreated` datetime NOT NULL,
  `dateupdated` datetime NOT NULL,
  PRIMARY KEY (`folderID`),
  UNIQUE KEY `folderID_UNIQUE` (`folderID`),
  KEY `fk_folders_repositories_idx` (`repositoryID`),
  KEY `fk_folders_folders_idx` (`parentFolderID`),
  CONSTRAINT `fk_folders_folders` FOREIGN KEY (`parentFolderID`) REFERENCES `folders` (`folderID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_folders_repositories` FOREIGN KEY (`repositoryID`) REFERENCES `repositories` (`repositoryID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folders`
--

LOCK TABLES `folders` WRITE;
/*!40000 ALTER TABLE `folders` DISABLE KEYS */;
INSERT INTO `folders` VALUES (6,NULL,'Folder 1',1,'0000-00-00 00:00:00','0000-00-00 00:00:00'),(7,NULL,'Folder 2',1,'0000-00-00 00:00:00','0000-00-00 00:00:00'),(8,6,'Folder 3',1,'0000-00-00 00:00:00','0000-00-00 00:00:00');
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
  `datecreated` datetime DEFAULT NULL,
  `dateupdated` datetime DEFAULT NULL,
  PRIMARY KEY (`repositoryID`),
  UNIQUE KEY `repositoryID_UNIQUE` (`repositoryID`),
  KEY `fk_repositories_users_idx` (`userID`),
  CONSTRAINT `fk_repositories_users` FOREIGN KEY (`userID`) REFERENCES `users` (`userID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repositories`
--

LOCK TABLES `repositories` WRITE;
/*!40000 ALTER TABLE `repositories` DISABLE KEYS */;
INSERT INTO `repositories` VALUES (1,'personal rep',5,'2014-11-22 15:32:06','2014-11-22 15:32:06'),(2,'personal rep',5,'2014-11-22 15:32:47','2014-11-22 15:32:47'),(3,'my personal rep',5,'2014-11-24 16:23:19','2014-11-24 16:23:19'),(4,'personal rep',5,'2014-11-24 16:48:15','2014-11-24 16:48:15'),(5,'personal rep',5,'2014-11-24 16:55:19','2014-11-24 16:55:19'),(6,'persoanl hub',5,'2014-11-24 16:56:34','2014-11-24 16:56:34'),(7,'personal hub',5,'2014-11-24 16:59:08','2014-11-24 16:59:08'),(8,'personal hub',5,'2014-11-24 17:02:10','2014-11-24 17:02:10'),(9,'personal hub',5,'2014-11-24 17:03:03','2014-11-24 17:03:03'),(10,'personal repository',5,'2014-11-24 17:07:12','2014-11-24 17:07:12'),(11,'personal rep',5,'2014-11-24 17:09:34','2014-11-24 17:09:34'),(12,'persoanl rep',5,'2014-11-24 17:12:09','2014-11-24 17:12:09'),(13,'personal',5,'2014-11-24 17:18:08','2014-11-24 17:18:08'),(14,'ssfdd',5,'2014-11-24 17:25:18','2014-11-24 17:25:18'),(15,'erfds',5,'2014-11-24 17:28:40','2014-11-24 17:28:40');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (4,'dn14500@my.bristol.ac.uk','dn14500@my.bristol.ac.uk'),(5,'dhiraj.narwani','dhiraj.narwani@gmail.com');
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

-- Dump completed on 2014-11-24 21:12:58
