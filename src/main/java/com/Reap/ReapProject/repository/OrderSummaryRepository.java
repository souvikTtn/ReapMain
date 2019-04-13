package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.OrderSummary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderSummaryRepository extends CrudRepository<OrderSummary,Integer> {

}
