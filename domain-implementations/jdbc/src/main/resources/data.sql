USE GT3_REP;
-- Inserts for the BRAND table
INSERT INTO BRAND (BRAND_NAME) VALUES
                                   ('Ferrari'), -- Ferrari brand
                                   ('Porsche'), -- Porsche brand
                                   ('Lamborghini'), -- Lamborghini brand
                                   ('Mercedes'), -- Mercedes brand
                                   ('Audi'); -- Audi brand

-- Inserts for the CAR table
INSERT INTO CAR (ID_BRAND, MODEL_NAME) VALUES
                                           (1, '488 GT3'), -- Ferrari 488 GT3
                                           (2, '911 GT3 R'), -- Porsche 911 GT3 R
                                           (3, 'Huracan GT3'), -- Lamborghini Huracan GT3
                                           (4, 'AMG GT3'), -- Mercedes AMG GT3
                                           (5, 'R8 LMS'); -- Audi R8 LMS

-- Inserts for the DRIVER table
INSERT INTO DRIVER (FIRST_NAME, LAST_NAME, NATIONALITY, BIRTHDATE) VALUES
                                                                       ('Lewis', 'Hamilton', 'British', '1985-01-07'), -- Lewis Hamilton, British driver
                                                                       ('Sebastian', 'Vettel', 'German', '1987-07-03'), -- Sebastian Vettel, German driver
                                                                       ('Max', 'Verstappen', 'Dutch', '1997-09-30'), -- Max Verstappen, Dutch driver
                                                                       ('Charles', 'Leclerc', 'Monacan', '1997-10-16'), -- Charles Leclerc, Monacan driver
                                                                       ('Valtteri', 'Bottas', 'Finnish', '1989-08-28'); -- Valtteri Bottas, Finnish driver

-- Inserts for the BRAND_DATA table
INSERT INTO BRAND_DATA (ID_BRAND, COUNTRY_OF_ORIGIN, CONTACT_INFO) VALUES
                                                                       (1, 'Italy', 'Maranello, Italy - info@ferrari.com'), -- Ferrari brand data
                                                                       (2, 'Germany', 'Stuttgart, Germany - info@porsche.com'), -- Porsche brand data
                                                                       (3, 'Italy', 'Sant\ Agata Bolognese, Italy - info@lamborghini.com'), -- Lamborghini brand data
(4, 'Germany', 'Affalterbach, Germany - info@mercedes-benz.com'), -- Mercedes brand data
(5, 'Germany', 'Ingolstadt, Germany - info@audi.com'); -- Audi brand data

-- Inserts for the CAR_DATA table
INSERT INTO CAR_DATA (ID_CAR, HORSEPOWER, WEIGHT) VALUES
(1, 600, 1230), -- Ferrari 488 GT3 data
(2, 500, 1250), -- Porsche 911 GT3 R data
(3, 580, 1240), -- Lamborghini Huracan GT3 data
(4, 550, 1260), -- Mercedes AMG GT3 data
(5, 560, 1255); -- Audi R8 LMS data

-- Inserts for the CIRCUIT table
INSERT INTO CIRCUIT (CIRCUIT_NAME, COUNTRY, LENGTH_KM) VALUES
('Monza', 'Italy', 5.793), -- Monza circuit in Italy
('Silverstone', 'UK', 5.891), -- Silverstone circuit in UK
('Nurburgring', 'Germany', 5.148), -- Nurburgring circuit in Germany
('Spa-Francorchamps', 'Belgium', 7.004), -- Spa-Francorchamps circuit in Belgium
('Le Mans', 'France', 13.626); -- Le Mans circuit in France

-- Inserts for the RACE table
INSERT INTO RACE (ID_CIRCUIT, RACE_NAME, RACE_DATE) VALUES
(1, 'Italian GP', '2024-05-01'), -- Italian Grand Prix at Monza
(2, 'British GP', '2024-06-01'), -- British Grand Prix at Silverstone
(3, 'German GP', '2024-07-01'); -- German Grand Prix at Nurburgring

