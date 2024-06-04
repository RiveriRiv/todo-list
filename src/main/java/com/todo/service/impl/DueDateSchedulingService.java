package com.todo.service.impl;


import com.todo.config.TodoProperties;
import com.todo.data.Status;
import com.todo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DueDateSchedulingService {

    private final ItemRepository itemRepository;

    private final TodoProperties todoProperties;

    @Scheduled(fixedRateString = "#{@todoProperties.checkDueDateTime}")
    public void updateExpiredItemsStatus() {
        LocalDateTime now = LocalDateTime.now();
        itemRepository.updateExpiredItemsStatus(now, Status.PAST_DUE);
    }
}
