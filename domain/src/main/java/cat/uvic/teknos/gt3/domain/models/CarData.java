package cat.uvic.teknos.gt3.domain.models;

public interface CarData {
    int getId();
    void setId(int id);

    int getHorsepower();
    void setHorsepower(int horsepower);

    int getWeight();
    void setWeight(int weight);

    Car getCar();
    void setCar(Car car);
}
