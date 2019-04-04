package com.Reap.ReapProject.service;

import com.Reap.ReapProject.entity.Recognition;
import com.Reap.ReapProject.repository.RecognitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RecognitionService {
    @Autowired
    RecognitionRepository recognitionRepository;

    public void addRecognition(Recognition recognition){
        recognition.setDate(new Date());
        recognition.setTime(new Date());
        recognitionRepository.save(recognition);
    }
}
