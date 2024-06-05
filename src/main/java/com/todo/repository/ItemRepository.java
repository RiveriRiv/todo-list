package com.todo.repository;

import com.todo.data.Status;
import com.todo.model.Item;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {

    List<Item> findAllByStatus(Status status);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("UPDATE Item i SET i.status = :status WHERE i.dueDate < :now AND i.status != :status")
    void updateExpiredItemsStatus(@Param("now") LocalDateTime now, @Param("status") Status status);
}
