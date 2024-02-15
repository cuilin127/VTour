package com.example.vtour.repo;

import com.example.vtour.model.Picture;
import org.springframework.data.repository.CrudRepository;

public interface PictureRepo extends CrudRepository<Picture, Integer> {
    Picture findById(int id);
}
