package cat.uvic.teknos.gt3.domain.models;

import java.util.Set;

public interface Bike {
    Long getId();
    void setId(Long id);
    String getModel();
    void setModel(String model);
    int getYear();
    void setYear(int year);
    BikeData getBikeData();
    void setBikeData(BikeData bikeData);
    Brand getBrand();
    void setBrand(Brand brand);
    Set<User> getUsers();
    void setUsers(Set<User> users);
}
