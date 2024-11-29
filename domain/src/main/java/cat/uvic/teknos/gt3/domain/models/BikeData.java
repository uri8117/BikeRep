package cat.uvic.teknos.gt3.domain.models;

public interface BikeData {
    Long getId();
    void setId(Long id);
    double getEngineCapacity();
    void setEngineCapacity(double engineCapacity);
    double getWeight();
    void setWeight(double weight);
    Bike getBike();
    void setBike(Bike bike);
}
