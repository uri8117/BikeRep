package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.Brand;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BrandDto implements Brand {
    private Long id;
    private String name;
    private String country;

    private List<Bike> bikes = new ArrayList<>();


    // Add the missing field
    private String message;

    // Getters and setters for all fields
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public List<Bike> getBikes() {
        return bikes;
    }

    @Override
    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }
}
