package com.example.vtour.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class House {
    @Id //automatically assigned value
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;//Primary Key

    String address;
    String fullAddress;
    String postCode;
    String city;
    String province;
    String saleType;
    String matterPortUrl;
    String floorPlanUrl;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Picture> pictures = new ArrayList<Picture>();

    public String getGoogleMapUrl(){
        String temp = this.address.replace(" ","+");
        return "https://www.google.com/maps/embed/v1/place?key=AIzaSyBZL-OEdORxsMHeTiKVH8CobkncKQvNFDQ&q=" + temp + ","+this.city;
    }
}
