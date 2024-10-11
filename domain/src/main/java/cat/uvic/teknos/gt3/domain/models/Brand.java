package cat.uvic.teknos.gt3.domain.models;

import java.util.Set;

public interface Brand {
    int getId();
    void setId(int id);

    String getBrandName();
    void setBrandName(String brandName);

    BrandData getBrandData();
    void setBrandData(BrandData brandData);

    Set<Car> getCars();
    void setCars(Set<Car> cars);
}
