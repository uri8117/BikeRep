package cat.uvic.teknos.gt3.file.jbdc.models;

import cat.uvic.teknos.gt3.domain.models.Car;

public class CarData implements cat.uvic.teknos.gt3.domain.models.CarData {
    private int id;
    private int horsePower;
    private int weight;
    private Car car;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getHorsepower() {
        return horsePower;
    }

    @Override
    public void setHorsepower(int horsePower) {
        this.horsePower = horsePower;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public Car getCar() {
        return car;
    }

    @Override
    public void setCar(Car car) {
        this.car = car;
    }
}
