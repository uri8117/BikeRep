package cat.uvic.teknos.gt3.domain.models;

import java.util.List;

public interface Brand {
    Long getId();
    void setId(Long id);
    String getName();
    void setName(String name);
    String getCountry();
    void setCountry(String country);
    List<Bike> getBikes();
    void setBikes(List<Bike> bikes);
}
