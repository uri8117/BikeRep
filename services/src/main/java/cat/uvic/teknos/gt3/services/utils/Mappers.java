package cat.uvic.teknos.gt3.services.utils;

import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.Race;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.Circuit;
import cat.uvic.teknos.gt3.domain.models.Car;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule addressTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(Race.class, cat.uvic.teknos.gt3.file.jbdc.models.Race.class)
                .addAbstractTypeMapping(Driver.class, cat.uvic.teknos.gt3.file.jbdc.models.Driver.class)
                .addAbstractTypeMapping(Brand.class, cat.uvic.teknos.gt3.file.jbdc.models.Brand.class)
                .addAbstractTypeMapping(Circuit.class, cat.uvic.teknos.gt3.file.jbdc.models.Circuit.class)
                .addAbstractTypeMapping(Car.class, cat.uvic.teknos.gt3.file.jbdc.models.Car.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(addressTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}