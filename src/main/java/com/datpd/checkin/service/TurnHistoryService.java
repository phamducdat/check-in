package com.datpd.checkin.service;

import com.datpd.checkin.entity.TurnHistoryEntity;
import com.datpd.checkin.repository.TurnHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TurnHistoryService {

    private final TurnHistoryRepository turnHistoryRepository;

    public TurnHistoryService(TurnHistoryRepository turnHistoryRepository) {
        this.turnHistoryRepository = turnHistoryRepository;
    }

    public TurnHistoryEntity createTurnHistory(long userId,
                                               long amount,
                                               long balance) {
        TurnHistoryEntity turnHistoryEntity = new TurnHistoryEntity();
        turnHistoryEntity.setUserId(userId);
        turnHistoryEntity.setAmount(amount);
        turnHistoryEntity.setBalance(balance);
        turnHistoryEntity.setCreateAt(new Date());

        return turnHistoryRepository.save(turnHistoryEntity);
    }
}
