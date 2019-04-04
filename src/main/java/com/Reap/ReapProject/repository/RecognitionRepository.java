package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.Recognition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecognitionRepository extends CrudRepository<Recognition,Integer> {
}
