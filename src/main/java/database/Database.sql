-- ****************** SqlDBM: MySQL ******************;
-- ***************************************************;

use `slack-clone-db`;

-- ************************************** `Client`
CREATE TABLE `Client`
(
    `username` varchar(45) NOT NULL ,
    `password` varchar(45) NOT NULL ,
    PRIMARY KEY (`username`)
);

-- ************************************** `Channel`

CREATE TABLE `Channel`
(
 `name`     varchar(45) NOT NULL ,
 `idAdmin`  varchar(45) NOT NULL ,
 `description`     varchar(45) NOT NULL ,
 `isPublic` binary NOT NULL ,

PRIMARY KEY (`name`),
KEY `FK_26` (`idAdmin`),
CONSTRAINT `FK_24` FOREIGN KEY `FK_26` (`idAdmin`) REFERENCES `Client` (`username`)
);

-- ************************************** `Message`

CREATE TABLE `Message`
(
 `id`        integer NOT NULL AUTO_INCREMENT,
 `content`   varchar(255) NOT NULL ,
 `idChannel` varchar(45) NOT NULL ,
 `username` varchar(45) NOT NULL,
 `date`      datetime NOT NULL ,

PRIMARY KEY (`id`),
KEY `FK_22` (`idChannel`),
CONSTRAINT `FK_20` FOREIGN KEY `FK_22` (`idChannel`) REFERENCES `Channel` (`name`),
KEY `FK_33` (`username`),
CONSTRAINT `FK_31` FOREIGN KEY `FK_33` (`username`) REFERENCES `Client` (`username`)
);

# -- ************************************** `Client_Channel`
# CREATE TABLE `Client_Channel_Message`
# (
#  `idChannel` varchar(45) NOT NULL ,
#  `username`  varchar(45) NOT NULL ,
#  `idMessage` integer NOT NULL,
#
# KEY `FK_30` (`idChannel`),
# CONSTRAINT `FK_28` FOREIGN KEY `FK_30` (`idChannel`) REFERENCES `Channel` (`name`),
# KEY `FK_33` (`username`),
# CONSTRAINT `FK_31` FOREIGN KEY `FK_33` (`username`) REFERENCES `Client` (`username`),
# KEY `FK_36` (`idMessage`),
# CONSTRAINT  `FK_34` FOREIGN KEY `FK_36` (`idMessage`) REFERENCES `Message` (`id`)
# );





