package cat.uvic.teknos.gt3.domain.models;

import java.util.Set;

public interface User {
    Long getId();
    void setId(Long id);
    String getName();
    void setName(String name);
    String getEmail();
    void setEmail(String email);
    Set<Bike> getBikes();
    void setBikes(Set<Bike> bikes);
}

