package cat.uvic.teknos.gt3.services.utils;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.User;
import cat.uvic.teknos.gt3.domainimplementation.entities.BikeEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.BrandEntity;
import cat.uvic.teknos.gt3.domainimplementation.entities.UserEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule addressTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(Bike.class, BikeEntity.class)
                .addAbstractTypeMapping(Brand.class, BrandEntity.class)
                .addAbstractTypeMapping(User.class, UserEntity.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(addressTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}