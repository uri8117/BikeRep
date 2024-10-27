package cat.uvic.teknos.gt3.services.utils;

import cat.uvic.teknos.gt3.domain.models.Driver;
import cat.uvic.teknos.gt3.domain.models.Race;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class Mappers {
    private static final ObjectMapper mapper;

    static  {
        SimpleModule addressTypeMapping = new SimpleModule()
                .addAbstractTypeMapping(Race.class, cat.uvic.teknos.gt3.file.jbdc.models.Race.class)
                .addAbstractTypeMapping(Driver.class, cat.uvic.teknos.gt3.file.jbdc.models.Driver.class);
        mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .registerModule(addressTypeMapping);
    }

    public static ObjectMapper get() {
        return mapper;
    }
}