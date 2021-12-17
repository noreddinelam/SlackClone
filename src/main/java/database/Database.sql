-- ****************** SqlDBM: MySQL ******************;
-- ***************************************************;


-- ************************************** `Channel`

CREATE TABLE `Channel`
(
 `id`       integer NOT NULL ,
 `name`     varchar(45) NOT NULL ,
 `idAdmin`  integer NOT NULL ,
 `desc`     varchar(45) NOT NULL ,
 `isPublic` binary NOT NULL ,

PRIMARY KEY (`id`),
KEY `FK_26` (`idAdmin`),
CONSTRAINT `FK_24` FOREIGN KEY `FK_26` (`idAdmin`) REFERENCES `Client` (`username`)
);
-- ************************************** `Client`
CREATE TABLE `Client`
(
 `username` integer NOT NULL ,
 `password` varchar(45) NOT NULL ,

PRIMARY KEY (`username`)
);
-- ************************************** `Client_Channel`
CREATE TABLE `Client_Channel`
(
 `idChannel` integer NOT NULL ,
 `username`  integer NOT NULL ,

KEY `FK_30` (`idChannel`),
CONSTRAINT `FK_28` FOREIGN KEY `FK_30` (`idChannel`) REFERENCES `Channel` (`id`),
KEY `FK_33` (`username`),
CONSTRAINT `FK_31` FOREIGN KEY `FK_33` (`username`) REFERENCES `Client` (`username`)
);
-- ************************************** `Message`
CREATE TABLE `Message`
(
 `id`        integer NOT NULL ,
 `content`   varchar(255) NOT NULL ,
 `idChannel` integer NOT NULL ,
 `idUser`    integer NOT NULL ,
 `date`      datetime NOT NULL ,

PRIMARY KEY (`id`),
KEY `FK_19` (`idUser`),
CONSTRAINT `FK_17` FOREIGN KEY `FK_19` (`idUser`) REFERENCES `Client` (`username`),
KEY `FK_22` (`idChannel`),
CONSTRAINT `FK_20` FOREIGN KEY `FK_22` (`idChannel`) REFERENCES `Channel` (`id`)
);




