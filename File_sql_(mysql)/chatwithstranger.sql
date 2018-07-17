-- phpMyAdmin SQL Dump
-- version 4.8.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 17, 2018 at 07:34 AM
-- Server version: 10.1.33-MariaDB
-- PHP Version: 7.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chatwithstranger`
--

-- --------------------------------------------------------

--
-- Table structure for table `tblcontacts`
--

CREATE TABLE `tblcontacts` (
  `IDCONTACT` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblcontacts`
--

INSERT INTO `tblcontacts` (`IDCONTACT`, `IDUSER`) VALUES
(20, 89),
(21, 90);

-- --------------------------------------------------------

--
-- Table structure for table `tblconversation`
--

CREATE TABLE `tblconversation` (
  `IDCONVERSATION` int(11) NOT NULL,
  `TITLE` text NOT NULL,
  `CREATED_AT` text NOT NULL,
  `IDUSER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblmessages`
--

CREATE TABLE `tblmessages` (
  `IDMESSAGES` int(11) NOT NULL,
  `IDCONVERSATION` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `TEXT` text,
  `PHOTO` text,
  `VIDEO` text,
  `GIF` text,
  `EMOTION` text,
  `CREATED_AT` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblparticipants`
--

CREATE TABLE `tblparticipants` (
  `IDPARTICIPANTS` int(11) NOT NULL,
  `IDCONVERSATION` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblreports`
--

CREATE TABLE `tblreports` (
  `IDREPORT` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblreports`
--

INSERT INTO `tblreports` (`IDREPORT`, `IDUSER`) VALUES
(9, 89),
(10, 90);

-- --------------------------------------------------------

--
-- Table structure for table `tblrequestfriends`
--

CREATE TABLE `tblrequestfriends` (
  `IDREQUESTFRIEND` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblrequestfriends`
--

INSERT INTO `tblrequestfriends` (`IDREQUESTFRIEND`, `IDUSER`) VALUES
(16, 89),
(17, 90);

-- --------------------------------------------------------

--
-- Table structure for table `tbluserreport`
--

CREATE TABLE `tbluserreport` (
  `IDUSERREPORT` int(11) NOT NULL,
  `IDREPORT` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `CREATED_AT` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tbluserrequestfriend`
--

CREATE TABLE `tbluserrequestfriend` (
  `IDUSERREQUESTFRIEND` int(11) NOT NULL,
  `IDREQUESTFRIEND` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `CREATED_AT` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tblusers`
--

CREATE TABLE `tblusers` (
  `IDUSER` int(11) NOT NULL,
  `USER` varchar(30) NOT NULL,
  `PASSWORD` text NOT NULL,
  `EMAIL` text,
  `FULLNAME` text NOT NULL,
  `PHONE` varchar(12) DEFAULT NULL,
  `GENDER` int(11) NOT NULL,
  `IS_ACTIVE` int(11) NOT NULL,
  `CREATED_AT` text NOT NULL,
  `IMAGE` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tblusers`
--

INSERT INTO `tblusers` (`IDUSER`, `USER`, `PASSWORD`, `EMAIL`, `FULLNAME`, `PHONE`, `GENDER`, `IS_ACTIVE`, `CREATED_AT`, `IMAGE`) VALUES
(89, 'trung', '123', '', 'lê quang trung', '', 1, 0, 'Tue Jul 17 12:31:31 GMT+07:00 2018', '/images/avatar/defaultMale.png'),
(90, 'linh', '123', '', 'nguyễn duy linh', '', 1, 1, 'Tue Jul 17 12:32:05 GMT+07:00 2018', '/images/avatar/defaultMale.png');

-- --------------------------------------------------------

--
-- Table structure for table `tbluserscontact`
--

CREATE TABLE `tbluserscontact` (
  `IDUSERCONTACT` int(11) NOT NULL,
  `IDCONTACT` int(11) NOT NULL,
  `IDUSER` int(11) NOT NULL,
  `CREATED_AT` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbluserscontact`
--

INSERT INTO `tbluserscontact` (`IDUSERCONTACT`, `IDCONTACT`, `IDUSER`, `CREATED_AT`) VALUES
(12, 20, 90, 'Tue Jul 17 2018 12:32:35 GMT+0700 (SE Asia Standard Time)'),
(13, 21, 89, 'Tue Jul 17 2018 12:32:35 GMT+0700 (SE Asia Standard Time)');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tblcontacts`
--
ALTER TABLE `tblcontacts`
  ADD PRIMARY KEY (`IDCONTACT`),
  ADD KEY `TBLContacts_fk0` (`IDUSER`);

--
-- Indexes for table `tblconversation`
--
ALTER TABLE `tblconversation`
  ADD PRIMARY KEY (`IDCONVERSATION`),
  ADD KEY `TBLConversation_fk0` (`IDUSER`);

--
-- Indexes for table `tblmessages`
--
ALTER TABLE `tblmessages`
  ADD PRIMARY KEY (`IDMESSAGES`),
  ADD KEY `TBLMessages_fk0` (`IDCONVERSATION`),
  ADD KEY `TBLMessages_fk1` (`IDUSER`);

--
-- Indexes for table `tblparticipants`
--
ALTER TABLE `tblparticipants`
  ADD PRIMARY KEY (`IDPARTICIPANTS`),
  ADD KEY `TBLParticipants_fk0` (`IDCONVERSATION`),
  ADD KEY `TBLParticipants_fk1` (`IDUSER`);

--
-- Indexes for table `tblreports`
--
ALTER TABLE `tblreports`
  ADD PRIMARY KEY (`IDREPORT`),
  ADD KEY `TBLReports_fk0` (`IDUSER`);

--
-- Indexes for table `tblrequestfriends`
--
ALTER TABLE `tblrequestfriends`
  ADD PRIMARY KEY (`IDREQUESTFRIEND`),
  ADD KEY `TBLRequestFriends_fk0` (`IDUSER`);

--
-- Indexes for table `tbluserreport`
--
ALTER TABLE `tbluserreport`
  ADD PRIMARY KEY (`IDUSERREPORT`),
  ADD KEY `TBLUserReport_fk0` (`IDREPORT`),
  ADD KEY `TBLUserReport_fk1` (`IDUSER`);

--
-- Indexes for table `tbluserrequestfriend`
--
ALTER TABLE `tbluserrequestfriend`
  ADD PRIMARY KEY (`IDUSERREQUESTFRIEND`),
  ADD KEY `TBLUserRequestFriend_fk0` (`IDREQUESTFRIEND`),
  ADD KEY `TBLUserRequestFriend_fk1` (`IDUSER`);

--
-- Indexes for table `tblusers`
--
ALTER TABLE `tblusers`
  ADD PRIMARY KEY (`IDUSER`);

--
-- Indexes for table `tbluserscontact`
--
ALTER TABLE `tbluserscontact`
  ADD PRIMARY KEY (`IDUSERCONTACT`),
  ADD KEY `TBLUsersContact_fk0` (`IDCONTACT`),
  ADD KEY `TBLUsersContact_fk1` (`IDUSER`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tblcontacts`
--
ALTER TABLE `tblcontacts`
  MODIFY `IDCONTACT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT for table `tblconversation`
--
ALTER TABLE `tblconversation`
  MODIFY `IDCONVERSATION` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `tblmessages`
--
ALTER TABLE `tblmessages`
  MODIFY `IDMESSAGES` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=374;

--
-- AUTO_INCREMENT for table `tblparticipants`
--
ALTER TABLE `tblparticipants`
  MODIFY `IDPARTICIPANTS` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `tblreports`
--
ALTER TABLE `tblreports`
  MODIFY `IDREPORT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `tblrequestfriends`
--
ALTER TABLE `tblrequestfriends`
  MODIFY `IDREQUESTFRIEND` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `tbluserreport`
--
ALTER TABLE `tbluserreport`
  MODIFY `IDUSERREPORT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `tbluserrequestfriend`
--
ALTER TABLE `tbluserrequestfriend`
  MODIFY `IDUSERREQUESTFRIEND` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `tblusers`
--
ALTER TABLE `tblusers`
  MODIFY `IDUSER` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=91;

--
-- AUTO_INCREMENT for table `tbluserscontact`
--
ALTER TABLE `tbluserscontact`
  MODIFY `IDUSERCONTACT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tblcontacts`
--
ALTER TABLE `tblcontacts`
  ADD CONSTRAINT `TBLContacts_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tblconversation`
--
ALTER TABLE `tblconversation`
  ADD CONSTRAINT `TBLConversation_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tblmessages`
--
ALTER TABLE `tblmessages`
  ADD CONSTRAINT `TBLMessages_fk0` FOREIGN KEY (`IDCONVERSATION`) REFERENCES `tblconversation` (`IDCONVERSATION`),
  ADD CONSTRAINT `TBLMessages_fk1` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tblparticipants`
--
ALTER TABLE `tblparticipants`
  ADD CONSTRAINT `TBLParticipants_fk0` FOREIGN KEY (`IDCONVERSATION`) REFERENCES `tblconversation` (`IDCONVERSATION`),
  ADD CONSTRAINT `TBLParticipants_fk1` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tblreports`
--
ALTER TABLE `tblreports`
  ADD CONSTRAINT `TBLReports_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tblrequestfriends`
--
ALTER TABLE `tblrequestfriends`
  ADD CONSTRAINT `TBLRequestFriends_fk0` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tbluserreport`
--
ALTER TABLE `tbluserreport`
  ADD CONSTRAINT `TBLUserReport_fk0` FOREIGN KEY (`IDREPORT`) REFERENCES `tblreports` (`IDREPORT`),
  ADD CONSTRAINT `TBLUserReport_fk1` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tbluserrequestfriend`
--
ALTER TABLE `tbluserrequestfriend`
  ADD CONSTRAINT `TBLUserRequestFriend_fk0` FOREIGN KEY (`IDREQUESTFRIEND`) REFERENCES `tblrequestfriends` (`IDREQUESTFRIEND`),
  ADD CONSTRAINT `TBLUserRequestFriend_fk1` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);

--
-- Constraints for table `tbluserscontact`
--
ALTER TABLE `tbluserscontact`
  ADD CONSTRAINT `TBLUsersContact_fk0` FOREIGN KEY (`IDCONTACT`) REFERENCES `tblcontacts` (`IDCONTACT`),
  ADD CONSTRAINT `TBLUsersContact_fk1` FOREIGN KEY (`IDUSER`) REFERENCES `tblusers` (`IDUSER`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
