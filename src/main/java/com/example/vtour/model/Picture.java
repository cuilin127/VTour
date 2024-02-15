package com.example.vtour.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class Picture {
    @Id //automatically assigned value
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;//Primary Key

    String fileId;
    String url;
    @ManyToOne
    private House house;

}
