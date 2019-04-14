package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.OrderSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderSummaryRepository extends CrudRepository<OrderSummary,Integer> {

    List<OrderSummary> findAll();
    List<OrderSummary> findByUserId(Integer id);

}
