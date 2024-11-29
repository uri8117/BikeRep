package cat.uvic.teknos.gt3.clients.console.dto;

import cat.uvic.teknos.gt3.domain.models.Bike;
import cat.uvic.teknos.gt3.domain.models.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDto implements User {
    private Long id;
    private String name;
    private String email;
    private Set<Bike> bikes = new HashSet<>();

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
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Set<Bike> getBikes() {
        return bikes;
    }

    @Override
    public void setBikes(Set<Bike> bikes) {
        this.bikes = bikes;
    }
}
