package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.Recognition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.joda.time.LocalDate;
import java.util.List;

@Repository
public interface RecognitionRepository extends CrudRepository<Recognition,Integer> {

    List<Recognition> findAll();
    List<Recognition> findRecognitionByReceiverName(String receiverName);

    List<Recognition> findRecognitionByDateBetween(LocalDate startDate,LocalDate endDate);
}
