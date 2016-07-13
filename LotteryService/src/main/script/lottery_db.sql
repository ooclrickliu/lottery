
CREATE DATABASE IF NOT EXISTS `lottery` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE `lottery`;

GRANT ALL PRIVILEGES ON lottery.* to 'lottery'@'%' IDENTIFIED BY 'lottery#123';
GRANT ALL PRIVILEGES ON lottery.* to 'lottery'@'localhost' IDENTIFIED BY 'lottery#123';