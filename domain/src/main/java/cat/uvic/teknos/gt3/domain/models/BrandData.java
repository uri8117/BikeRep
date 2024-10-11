package cat.uvic.teknos.gt3.domain.models;

public interface BrandData {
    int getId();
    void setId(int id);

    String getCountryOfOrigin();
    void setCountryOfOrigin(String countryOfOrigin);

    String getContactInfo();
    void setContactInfo(String contactInfo);

    Brand getBrand();
    void setBrand(Brand brand);
}
