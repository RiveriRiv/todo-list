package com.todo.repository;

import com.todo.data.Status;
import com.todo.model.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findAllByStatusNot(Status status);
}
