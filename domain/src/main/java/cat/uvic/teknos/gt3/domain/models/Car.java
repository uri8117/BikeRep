package cat.uvic.teknos.gt3.domain.models;

import java.util.Set;

public interface Car {
    int getId();
    void setId(int id);

    Brand getBrand();
    void setBrand(Brand brand);

    String getModelName();
    void setModelName(String modelName);

    CarData getCarData();
    void setCarData(CarData carData);

    Set<Driver> getDrivers();
    void setDrivers(Set<Driver> drivers);
}
