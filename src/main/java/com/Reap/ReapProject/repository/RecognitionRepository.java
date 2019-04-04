package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.Recognition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecognitionRepository extends CrudRepository<Recognition,Integer> {

    List<Recognition> findAll();
}
