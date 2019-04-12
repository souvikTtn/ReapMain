package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.Recognition;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import org.joda.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecognitionRepository extends CrudRepository<Recognition,Integer> {

    List<Recognition> findAll();
    List<Recognition> findRecognitionByReceiverName(String receiverName);

    List<Recognition> findRecognitionByDateBetween(LocalDate startDate,LocalDate endDate);

    List<Recognition> findRecognitionByDate(LocalDate date);

    List<Recognition> findRecognitionBySenderId(Integer senderId);

    List<Recognition> findRecognitionByReceiverId(Integer receiverId);

    Optional<Recognition> findRecognitionById(Integer id);

}
