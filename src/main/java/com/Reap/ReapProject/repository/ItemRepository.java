package com.Reap.ReapProject.repository;

import com.Reap.ReapProject.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends CrudRepository<Item,Integer> {
    List<Item> findAll();
    Optional<Item> findById(Integer itemId);
}
