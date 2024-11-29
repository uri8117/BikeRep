drop database if exists bike_rep;
CREATE DATABASE if not exists bike_rep;
use bike_rep;

-- Creació de la taula 'brands' per desar les marques de bicicletes
CREATE TABLE brands (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        country VARCHAR(255) NOT NULL
);

-- Creació de la taula 'bikes' per desar les bicicletes
CREATE TABLE bikes (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       model VARCHAR(255) NOT NULL,
                       year INT NOT NULL,
                       brand_id BIGINT NOT NULL,  -- Relació amb 'brands'
                       CONSTRAINT fk_bikes_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE CASCADE
);

-- Creació de la taula 'bike_data' per desar la informació tècnica de les bicicletes
CREATE TABLE bike_data (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           engine_capacity DOUBLE NOT NULL,
                           weight DOUBLE NOT NULL,
                           bike_id BIGINT,  -- Relació amb 'bikes'
                           CONSTRAINT fk_bike_data_bike FOREIGN KEY (bike_id) REFERENCES bikes(id) ON DELETE CASCADE
);

-- Creació de la taula 'users' per desar la informació dels usuaris
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL
);

-- Creació de la taula 'bike_user' per gestionar la relació molts-a-molts entre usuaris i bicicletes
CREATE TABLE bike_user (
                           bike_id BIGINT NOT NULL,
                           user_id BIGINT NOT NULL,
                           PRIMARY KEY (bike_id, user_id),
                           CONSTRAINT fk_bike_user_bike FOREIGN KEY (bike_id) REFERENCES bikes(id) ON DELETE CASCADE,
                           CONSTRAINT fk_bike_user_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


