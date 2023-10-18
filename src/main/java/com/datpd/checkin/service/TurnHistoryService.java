package com.datpd.checkin.service;

import com.datpd.checkin.entity.TurnHistoryEntity;
import com.datpd.checkin.repository.TurnHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TurnHistoryService {

    private final Logger logger = LoggerFactory.getLogger(TurnHistoryService.class);
    private final TurnHistoryRepository turnHistoryRepository;

    public TurnHistoryService(TurnHistoryRepository turnHistoryRepository) {
        this.turnHistoryRepository = turnHistoryRepository;
    }

    public void createTurnHistory(long userId,
                                  long amount,
                                  long balance) {
        logger.info("Create Turn History");
        TurnHistoryEntity turnHistoryEntity = new TurnHistoryEntity();
        turnHistoryEntity.setUserId(userId);
        turnHistoryEntity.setAmount(amount);
        turnHistoryEntity.setBalance(balance);
        turnHistoryEntity.setCreateAt(new Date());

        turnHistoryRepository.save(turnHistoryEntity);
    }
}
