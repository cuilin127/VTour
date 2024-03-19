package com.example.vtour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class AddressReport {
    @Id //automatically assigned value
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;//Primary Key

    @OneToMany(cascade=CascadeType.ALL)
    List<PublicInfra> infraList = new ArrayList<>();
}
