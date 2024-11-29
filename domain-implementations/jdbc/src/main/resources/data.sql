-- Inserir dades per a la taula 'brands'
INSERT INTO brands (name, country) VALUES
                                       ('Yamaha', 'Japan'),
                                       ('Honda', 'Japan'),
                                       ('Ducati', 'Italy');

-- Inserir dades per a la taula 'bike_data'
INSERT INTO bike_data (engine_capacity, weight) VALUES
                                                    (600.0, 150.5),
                                                    (1000.0, 190.3),
                                                    (800.0, 175.0);

-- Inserir dades per a la taula 'bikes'
INSERT INTO bikes (model, year, brand_id) VALUES
                                              ('YZF-R6', 2020, 1),  -- Model de Yamaha, BikeData 1, Brand Yamaha
                                              ('CBR1000RR', 2021, 2),  -- Model de Honda, BikeData 2, Brand Honda
                                              ('Panigale V4', 2022, 3);  -- Model de Ducati, BikeData 3, Brand Ducati

-- Inserir dades per a la taula 'users'
INSERT INTO users (name, email) VALUES
                                    ('John Doe', 'john.doe@example.com'),
                                    ('Jane Smith', 'jane.smith@example.com');

-- Assignar usuaris a bicicletes
INSERT INTO bike_user (bike_id, user_id) VALUES
                                             (1, 1),  -- John Doe té la bicicleta Yamaha YZF-R6
                                             (2, 2);  -- Jane Smith té la bicicleta Honda CBR1000RR
