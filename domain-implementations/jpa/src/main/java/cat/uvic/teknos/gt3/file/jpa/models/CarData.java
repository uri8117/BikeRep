package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CAR_DATA")
public class CarData implements cat.uvic.teknos.gt3.domain.models.CarData {
    @Id
    @Column(name = "CAR_ID")
    private Integer id;

    @Column(name = "HORSEPOWER", nullable = false)
    private int horsepower;

    @Column(name = "WEIGHT", nullable = false)
    private int weight;

    @OneToOne
    @MapsId
    @JoinColumn(name = "ID_CAR")
    private Car car;

    // Getters and setters

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
        return horsepower;
    }

    @Override
    public void setHorsepower(int horsepower) {
        this.horsepower = horsepower;
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
    public cat.uvic.teknos.gt3.domain.models.Car getCar() {
        return car;
    }

    @Override
    public void setCar(cat.uvic.teknos.gt3.domain.models.Car car) {
        this.car = (Car) car;
    }
}
