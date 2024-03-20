package com.example.vtour.repo;

import com.example.vtour.model.House;
import com.example.vtour.model.PublicInfra;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PublicInfraRepo extends CrudRepository<PublicInfra, Integer> {
    List<PublicInfra> findByType(String type);
}
