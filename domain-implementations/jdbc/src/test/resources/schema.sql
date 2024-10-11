DROP DATABASE IF EXISTS GT3_REP;
CREATE DATABASE IF NOT EXISTS GT3_REP;
USE GT3_REP;

-- Table to store car brands
CREATE TABLE IF NOT EXISTS BRAND (
                                     ID_BRAND INT PRIMARY KEY AUTO_INCREMENT,
                                     BRAND_NAME VARCHAR(50) NOT NULL
    );

-- Table to store information about cars
CREATE TABLE IF NOT EXISTS CAR (
                                   ID_CAR INT PRIMARY KEY AUTO_INCREMENT,
                                   ID_BRAND INT NOT NULL,
                                   MODEL_NAME VARCHAR(50) NOT NULL,
    FOREIGN KEY (ID_BRAND) REFERENCES BRAND(ID_BRAND) ON DELETE CASCADE
    );

-- Table to store information about drivers
CREATE TABLE IF NOT EXISTS DRIVER (
                                      ID_DRIVER INT PRIMARY KEY AUTO_INCREMENT,
                                      FIRST_NAME VARCHAR(50) NOT NULL,
    LAST_NAME VARCHAR(50) NOT NULL,
    NATIONALITY VARCHAR(50) NOT NULL,
    BIRTHDATE DATE NOT NULL
    );

-- Table to store specific data about drivers (one-to-one relationship)
CREATE TABLE IF NOT EXISTS BRAND_DATA (
                                          ID_BRAND INT PRIMARY KEY,
                                          COUNTRY_OF_ORIGIN VARCHAR(100),
    CONTACT_INFO VARCHAR(255),
    FOREIGN KEY (ID_BRAND) REFERENCES BRAND(ID_BRAND) ON DELETE CASCADE
    );

-- Table to store specific data about cars (one-to-one relationship)
CREATE TABLE IF NOT EXISTS CAR_DATA (
                                        ID_CAR INT PRIMARY KEY,
                                        HORSEPOWER INT NOT NULL,
                                        WEIGHT INT NOT NULL,
                                        FOREIGN KEY (ID_CAR) REFERENCES CAR(ID_CAR) ON DELETE CASCADE
    );

-- Table for the many-to-many relationship between drivers and cars
CREATE TABLE IF NOT EXISTS CAR_DRIVER (
                                          ID_CAR INT NOT NULL,
                                          ID_DRIVER INT NOT NULL,
                                          PRIMARY KEY (ID_CAR, ID_DRIVER),
    FOREIGN KEY (ID_CAR) REFERENCES CAR(ID_CAR) ON DELETE CASCADE,
    FOREIGN KEY (ID_DRIVER) REFERENCES DRIVER(ID_DRIVER) ON DELETE CASCADE
    );

-- Table to store information about racing circuits
CREATE TABLE IF NOT EXISTS CIRCUIT (
                                       ID_CIRCUIT INT PRIMARY KEY AUTO_INCREMENT,
                                       CIRCUIT_NAME VARCHAR(50) NOT NULL,
    COUNTRY VARCHAR(50) NOT NULL,
    LENGTH_KM FLOAT(5, 2) NOT NULL
    );

-- Table to store information about races
CREATE TABLE IF NOT EXISTS RACE (
                                    ID_RACE INT PRIMARY KEY AUTO_INCREMENT,
                                    ID_CIRCUIT INT NOT NULL,
                                    RACE_NAME VARCHAR(50) NOT NULL,
    RACE_DATE DATE NOT NULL,
    FOREIGN KEY (ID_CIRCUIT) REFERENCES CIRCUIT(ID_CIRCUIT) ON DELETE CASCADE
    );

-- Table for the many-to-many relationship between races and drivers with additional attributes
CREATE TABLE IF NOT EXISTS RACE_DRIVER (
                                           ID_RACE INT NOT NULL,
                                           ID_DRIVER INT NOT NULL,
                                           POSITION INT NOT NULL,
                                           PRIMARY KEY (ID_RACE, ID_DRIVER),
    FOREIGN KEY (ID_RACE) REFERENCES RACE(ID_RACE) ON DELETE CASCADE,
    FOREIGN KEY (ID_DRIVER) REFERENCES DRIVER(ID_DRIVER) ON DELETE CASCADE
    );