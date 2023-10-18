package com.datpd.checkin.util;

import com.datpd.checkin.repository.TurnHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CheckInService {

    private final TurnHistoryRepository turnHistoryRepository;

    public CheckInService(TurnHistoryRepository turnHistoryRepository) {
        this.turnHistoryRepository = turnHistoryRepository;
    }

    public boolean hasUserCheckedInDuringValidTimes(long userId) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        return TimeConstants.VALID_CHECKIN_TIMES.stream().anyMatch(timeRange -> {
            if (timeRange.isWithin(currentTime)) {
                Date startTime = Date.from(LocalDateTime.of(today, timeRange.getStart()).atZone(ZoneId.systemDefault()).toInstant());
                Date endTime = Date.from(LocalDateTime.of(today, timeRange.getEnd()).atZone(ZoneId.systemDefault()).toInstant());
                return !turnHistoryRepository.existsByUserIdAndCreateAtBetween(userId, startTime, endTime);
            }
            return false;
        });
    }

}
