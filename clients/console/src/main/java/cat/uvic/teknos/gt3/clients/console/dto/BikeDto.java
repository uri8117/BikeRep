package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.BikeData;
import cat.uvic.teknos.gt3.domain.models.Brand;
import cat.uvic.teknos.gt3.domain.models.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class BikeDto implements Bike {
    private Long id;
    private String model;
    private int year;
    private BikeData bikeData;
    private Brand brand;
    private Set<User> users = new HashSet<>();

    // Getters i setters

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public BikeData getBikeData() {
        return bikeData;
    }

    @Override
    public void setBikeData(BikeData bikeData) {
        this.bikeData = bikeData;
    }

    @Override
    public Brand getBrand() {
        return brand;
    }

    @Override
    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public Set<User> getUsers() {
        return users;
    }

    @Override
    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
