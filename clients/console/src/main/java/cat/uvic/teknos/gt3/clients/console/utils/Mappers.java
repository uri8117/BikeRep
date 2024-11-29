package cat.uvic.teknos.gt3.clients.console.utils;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule genreTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(Bike.class, cat.uvic.teknos.gt3.clients.console.dto.BikeDto.class)
                .addAbstractTypeMapping(BikeData.class, cat.uvic.teknos.gt3.clients.console.dto.BikeDataDto.class)
                .addAbstractTypeMapping(Brand.class, cat.uvic.teknos.gt3.clients.console.dto.BrandDto.class)
                .addAbstractTypeMapping(User.class, cat.uvic.teknos.gt3.clients.console.dto.UserDto.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(genreTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}