package com.example.vtour.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class PublicInfra {
    @Id //automatically assigned value
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;//Primary Key

    String type;
    String name;
    String address;
    String url;
    double distance;
}
