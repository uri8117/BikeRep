package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "BRAND")
public class Brand implements cat.uvic.teknos.gt3.domain.models.Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_BRAND")
    private int id;

    @Column(name = "BRAND_NAME", nullable = false)
    private String brandName;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Car.class)
    private Set<cat.uvic.teknos.gt3.domain.models.Car> cars = new HashSet<>();

    @OneToOne(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = BrandData.class)
    private cat.uvic.teknos.gt3.domain.models.BrandData brandData;

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
    public String getBrandName() {
        return brandName;
    }

    @Override
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    @Override
    public cat.uvic.teknos.gt3.domain.models.BrandData getBrandData() {
        return brandData;
    }

    @Override
    public void setBrandData(cat.uvic.teknos.gt3.domain.models.BrandData brandData) {
        this.brandData = brandData;
        if (brandData != null) {
            brandData.setBrand(this);
        }
    }

    @Override
    public Set<cat.uvic.teknos.gt3.domain.models.Car> getCars() {
        return cars;
    }

    @Override
    public void setCars(Set<cat.uvic.teknos.gt3.domain.models.Car> cars) {
        this.cars = cars;
    }
}
