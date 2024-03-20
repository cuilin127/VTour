package com.example.vtour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddPhotoDto {
    private int houseId;
    private String dropBoxUrl;
}
