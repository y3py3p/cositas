package com.SafeWebDev.attempt.Models.Services;

import com.SafeWebDev.attempt.Models.*;
import com.SafeWebDev.attempt.Models.Respositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public ItemService(){

    }

    public List<Item> getAll(){
        return itemRepository.findAll();
    }

    public Item findById(long id){
        return itemRepository.findById(id);
    }

    public void add(Item item){
        itemRepository.save(item);
    }

    public void delete(Item item){
        itemRepository.delete(item);
    }

    public boolean exists(long id){
        if(itemRepository.findById(id) != null){
            return true;
        }else {
            return false;
        }
    }

    public void updateItem(long id, Item item){

        Item updated = itemRepository.findById(id);
        updated.update(item);
        itemRepository.save(updated);
    }

    public long getId(Item item){
        return item.getProductID();
    }
}
