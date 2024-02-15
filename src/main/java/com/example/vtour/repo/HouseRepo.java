package com.example.vtour.repo;

import com.example.vtour.model.House;
import org.springframework.data.repository.CrudRepository;

public interface HouseRepo extends CrudRepository<House, Integer> {
    House findById(int id);
}
