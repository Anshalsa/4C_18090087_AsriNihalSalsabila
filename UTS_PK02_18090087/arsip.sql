-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 23, 2020 at 02:48 AM
-- Server version: 10.1.36-MariaDB
-- PHP Version: 5.6.38

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `arsip`
--

-- --------------------------------------------------------

--
-- Table structure for table `tbdok`
--

CREATE TABLE `tbdok` (
  `no` int(11) NOT NULL,
  `kode_dokumen` int(10) NOT NULL,
  `nama_dokumen` varchar(30) NOT NULL,
  `kategori_dokumen` varchar(10) NOT NULL,
  `lokasi_dokumen` varchar(50) NOT NULL,
  `deskripsi_dokumen` text NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tbdok`
--

INSERT INTO `tbdok` (`no`, `kode_dokumen`, `nama_dokumen`, `kategori_dokumen`, `lokasi_dokumen`, `deskripsi_dokumen`, `tanggal`) VALUES
(3, 1212, 'kyungsoo', 'Pribadi', 'C:UsersAnshalsaDocumentsasis data18090087.txt', 'kepo wkwk', '2020-01-12'),
(4, 123, 'cacaa', 'Resmi', 'C:UsersAnshalsaDocuments4C - 18090087.docx', 'hhhhhhhhhhhhhhhhhhhhhhhhhhh', '2020-02-02'),
(5, 111, 'UTS', 'Resmi', 'C:UsersAnshalsaDocumentsLAPORAN WP.docx', 'UTS OYYYY', '2020-04-20');

-- --------------------------------------------------------

--
-- Table structure for table `tb_admin`
--

CREATE TABLE `tb_admin` (
  `id` int(5) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `last_login` datetime DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tb_admin`
--

INSERT INTO `tb_admin` (`id`, `username`, `password`, `last_login`) VALUES
(1, 'admin', '21232f297a57a5a743894a0e4a801fc3', '2020-04-18 01:15:51');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `tbdok`
--
ALTER TABLE `tbdok`
  ADD PRIMARY KEY (`no`);

--
-- Indexes for table `tb_admin`
--
ALTER TABLE `tb_admin`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `tbdok`
--
ALTER TABLE `tbdok`
  MODIFY `no` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `tb_admin`
--
ALTER TABLE `tb_admin`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
