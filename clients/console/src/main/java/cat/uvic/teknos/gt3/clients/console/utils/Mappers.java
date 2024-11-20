package cat.uvic.teknos.gt3.clients.console.utils;

import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.Car;
import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.Race;
import cat.uvic.teknos.gt3.domain.models.Circuit;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule genreTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(Race.class, cat.uvic.teknos.gt3.clients.console.dto.RaceDto.class)
                .addAbstractTypeMapping(Car.class, cat.uvic.teknos.gt3.clients.console.dto.CarDto.class)
                .addAbstractTypeMapping(Brand.class, cat.uvic.teknos.gt3.clients.console.dto.BrandDto.class)
                .addAbstractTypeMapping(Driver.class, cat.uvic.teknos.gt3.clients.console.dto.DriverDto.class)
                .addAbstractTypeMapping(Circuit.class, cat.uvic.teknos.gt3.clients.console.dto.CircuitDto.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(genreTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}