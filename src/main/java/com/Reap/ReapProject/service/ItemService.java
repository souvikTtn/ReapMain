package com.Reap.ReapProject.service;

import com.Reap.ReapProject.entity.Item;
import com.Reap.ReapProject.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
    @Autowired
    ItemRepository itemRepository;
    public Item saveItem(Item item){
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> findItemById(Integer itemId){
        return itemRepository.findById(itemId);
    }

}
