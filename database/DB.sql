
DROP DATABASE IF EXISTS GT3_REP_MYSQL;
CREATE DATABASE IF NOT EXISTS GT3_REP_MYSQL;
USE GT3_REP_MYSQL;


create table BRAND (
                       ID_BRAND integer not null auto_increment,
                       BRAND_NAME varchar(255) not null,
                       primary key (ID_BRAND)
) engine=InnoDB;

create table BRAND_DATA (
                            ID_BRAND integer not null,
                            CONTACT_INFO varchar(255),
                            COUNTRY_OF_ORIGIN varchar(255),
                            primary key (ID_BRAND)
) engine=InnoDB;

create table CAR (
                     ID integer not null auto_increment,
                     ID_BRAND integer not null,
                     MODEL_NAME varchar(255) not null,
                     primary key (ID)
) engine=InnoDB;

create table CAR_DATA (
                          HORSEPOWER integer not null,
                          ID_CAR integer not null,
                          WEIGHT integer not null,
                          primary key (ID_CAR)
) engine=InnoDB;

create table CAR_DRIVER (
                            ID_CAR integer not null,
                            ID_DRIVER integer not null,
                            primary key (ID_CAR, ID_DRIVER)
) engine=InnoDB;

create table CIRCUIT (
                         ID_CIRCUIT integer not null auto_increment,
                         LENGTH_KM float(53) not null,
                         CIRCUIT_NAME varchar(255) not null,
                         COUNTRY varchar(255) not null,
                         primary key (ID_CIRCUIT)
) engine=InnoDB;

create table DRIVER (
                        BIRTHDATE date not null,
                        ID_DRIVER integer not null auto_increment,
                        FIRST_NAME varchar(255) not null,
                        LAST_NAME varchar(255) not null,
                        NATIONALITY varchar(255) not null,
                        primary key (ID_DRIVER)
) engine=InnoDB;

create table RACE (
                      ID_CIRCUIT integer not null,
                      ID_RACE integer not null auto_increment,
                      RACE_DATE date not null,
                      RACE_NAME varchar(255) not null,
                      primary key (ID_RACE)
) engine=InnoDB;

create table RACE_DRIVER (
                             ID_DRIVER integer not null,
                             ID_RACE integer not null,
                             POSITION integer not null,
                             primary key (ID_DRIVER, ID_RACE)
) engine=InnoDB;

alter table BRAND_DATA
    add constraint FKeao3a9ostfdk771sjmv6nebwr
        foreign key (ID_BRAND)
            references BRAND (ID_BRAND);

alter table CAR
    add constraint FKt26bsptcolkb853pi14kb7pu6
        foreign key (ID_BRAND)
            references BRAND (ID_BRAND);

alter table CAR_DATA
    add constraint FKibluw7rmj7bna2c2l39pem3oe
        foreign key (ID_CAR)
            references CAR (ID);

alter table CAR_DRIVER
    add constraint FKjnxt3axv1b4fipw7kxm3jm7c
        foreign key (ID_CAR)
            references CAR (ID);

alter table CAR_DRIVER
    add constraint FK5yq0xb8xdjgggffoaiaby3f0d
        foreign key (ID_DRIVER)
            references DRIVER (ID_DRIVER);

alter table RACE
    add constraint FKrkbx7mddd01aicx3wjhgn6ox2
        foreign key (ID_CIRCUIT)
            references CIRCUIT (ID_CIRCUIT);

alter table RACE_DRIVER
    add constraint FKgomsqb8wj9h45t68aavmg4fh0
        foreign key (ID_DRIVER)
            references DRIVER (ID_DRIVER);

alter table RACE_DRIVER
    add constraint FKdm061m7s48ips3ma4l42amfg4
        foreign key (ID_RACE)
            references RACE (ID_RACE);

-- Inserts for the BRAND table
INSERT INTO BRAND (BRAND_NAME) VALUES ('Ferrari');
INSERT INTO BRAND (BRAND_NAME) VALUES ('Porsche');
INSERT INTO BRAND (BRAND_NAME) VALUES ('Lamborghini');
INSERT INTO BRAND (BRAND_NAME) VALUES ('Mercedes');
INSERT INTO BRAND (BRAND_NAME) VALUES ('Audi');


-- Inserts for the CAR table
INSERT INTO CAR (ID_BRAND, MODEL_NAME) VALUES (1, '488 GT3'); -- Ferrari 488 GT3
INSERT INTO CAR (ID_BRAND, MODEL_NAME) VALUES (2, '911 GT3 R'); -- Porsche 911 GT3 R
INSERT INTO CAR (ID_BRAND, MODEL_NAME) VALUES (3, 'Huracan GT3'); -- Lamborghini Huracan GT3
INSERT INTO CAR (ID_BRAND, MODEL_NAME) VALUES (4, 'AMG GT3'); -- Mercedes AMG GT3
INSERT INTO CAR (ID_BRAND, MODEL_NAME) VALUES (5, 'R8 LMS'); -- Audi R8 LMS


-- Inserts for the DRIVER table
INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES ('Lewis', 'Hamilton', 'British', '1985-01-07');
INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES ('Sebastian', 'Vettel', 'German', '1987-07-03');
INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES ('Max', 'Verstappen', 'Dutch', '1997-09-30');
INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES ('Charles', 'Leclerc', 'Monacan', '1997-10-16');
INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES ('Valtteri', 'Bottas', 'Finnish', '1989-08-28');


-- Inserts for the BRAND_DATA table
INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES (1, 'Italy', 'Maranello, Italy - info@ferrari.com');
INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES (2, 'Germany', 'Stuttgart, Germany - info@porsche.com');
INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES (3, 'Italy', 'Sant Agata Bolognese, Italy - info@lamborghini.com');
INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES (4, 'Germany', 'Affalterbach, Germany - info@mercedes-benz.com');
INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES (5, 'Germany', 'Ingolstadt, Germany - info@audi.com');


INSERT INTO CAR_DATA (ID_CAR, HORSEPOWER, WEIGHT) VALUES (1, 600, 1230);
INSERT INTO CAR_DATA (ID_CAR, HORSEPOWER, WEIGHT) VALUES (2, 500, 1250);
INSERT INTO CAR_DATA (ID_CAR, HORSEPOWER, WEIGHT) VALUES (3, 580, 1240);
INSERT INTO CAR_DATA (ID_CAR, HORSEPOWER, WEIGHT) VALUES (4, 550, 1260);
INSERT INTO CAR_DATA (ID_CAR, HORSEPOWER, WEIGHT) VALUES (5, 560, 1255);


-- Inserts for the CIRCUIT table
INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES ('Monza', 'Italy', 5.793);
INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES ('Silverstone', 'UK', 5.891);
INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES ('Nurburgring', 'Germany', 5.148);
INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES ('Spa-Francorchamps', 'Belgium', 7.004);
INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES ('Le Mans', 'France', 13.626);


-- Inserts for the RACE table
INSERT INTO RACE (ID_CIRCUIT, RACE_NAME, RACE_DATE) VALUES (1, 'Italian GP', '2024-05-01');
INSERT INTO RACE (ID_CIRCUIT, RACE_NAME, RACE_DATE) VALUES (2, 'British GP', '2024-06-01');
INSERT INTO RACE (ID_CIRCUIT, RACE_NAME, RACE_DATE) VALUES (3, 'German GP', '2024-07-01');