package com.Reap.ReapProject.service;

import com.Reap.ReapProject.entity.OrderSummary;
import com.Reap.ReapProject.repository.OrderSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderSummaryService {
    @Autowired
    OrderSummaryRepository orderSummaryRepository;

    public void addOrder(OrderSummary orderSummary){
        orderSummaryRepository.save(orderSummary);
    }

}
