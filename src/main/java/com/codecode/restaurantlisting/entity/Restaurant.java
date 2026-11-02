package com.codecode.restaurantlisting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String address;
    private String city;
    private String restaurantDescription;

    public Restaurant() {
    }

    public Restaurant(String name, String address, String city, String restaurantDescription) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.restaurantDescription = restaurantDescription;
    }

    public Restaurant(int id, String name, String address, String city, String restaurantDescription) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.restaurantDescription = restaurantDescription;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRestaurantDescription() {
        return restaurantDescription;
    }

    public void setRestaurantDescription(String restaurantDescription) {
        this.restaurantDescription = restaurantDescription;
    }
}
