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
 `adminUsername`  varchar(45) NOT NULL ,
 `description`     varchar(45) NOT NULL ,
 `isPublic` binary NOT NULL ,

PRIMARY KEY (`name`),
KEY `FK_26` (`adminUsername`),
CONSTRAINT `FK_24` FOREIGN KEY `FK_26` (`adminUsername`) REFERENCES `Client` (`username`)
);

-- ************************************** `Message`

CREATE TABLE `Message`
(
 `id`        integer NOT NULL AUTO_INCREMENT,
 `content`   varchar(255) NOT NULL ,
 `channelName` varchar(45) NOT NULL ,
 `username` varchar(45) NOT NULL,
 `date`      datetime NOT NULL ,

PRIMARY KEY (`id`),
KEY `FK_22` (`channelName`),
CONSTRAINT `FK_20` FOREIGN KEY `FK_22` (`channelName`) REFERENCES `Channel` (`name`),
KEY `FK_33` (`username`),
CONSTRAINT `FK_31` FOREIGN KEY `FK_33` (`username`) REFERENCES `Client` (`username`)
);

# -- ************************************** `Client_Channel`
CREATE TABLE `Client_Channel`
(
 `channelName` varchar(45) NOT NULL ,
 `username`  varchar(45) NOT NULL ,

KEY `FK_30` (`channelName`),
CONSTRAINT `FK_28` FOREIGN KEY `FK_30` (`channelName`) REFERENCES `Channel` (`name`),
KEY `FK_37` (`username`),
CONSTRAINT `FK_38` FOREIGN KEY `FK_37` (`username`) REFERENCES `Client` (`username`)
);





