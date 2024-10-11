package cat.uvic.teknos.gt3.file.jpa.models;

import jakarta.persistence.*;

@Entity
@Table(name = "BRAND_DATA")
public class BrandData implements cat.uvic.teknos.gt3.domain.models.BrandData {
    @Id
    @Column(name = "ID_BRAND")
    private int id;

    @Column(name = "COUNTRY_OF_ORIGIN")
    private String countryOfOrigin;

    @Column(name = "CONTACT_INFO")
    private String contactInfo;

    @OneToOne(targetEntity = Brand.class)
    @MapsId
    @JoinColumn(name = "ID_BRAND")
    private cat.uvic.teknos.gt3.domain.models.Brand brand;

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
    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    @Override
    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    @Override
    public String getContactInfo() {
        return contactInfo;
    }

    @Override
    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    @Override
    public cat.uvic.teknos.gt3.domain.models.Brand getBrand() {
        return brand;
    }

    @Override
    public void setBrand(cat.uvic.teknos.gt3.domain.models.Brand brand) {
        this.brand = brand;
    }
}
