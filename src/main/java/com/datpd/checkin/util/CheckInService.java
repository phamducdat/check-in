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

    public Date getExpiryTime() {
        LocalTime now = LocalTime.now();
        return TimeConstants.VALID_CHECKIN_TIMES.stream()
                .filter(timeRange -> timeRange.isWithin(LocalTime.now()))
                .map(TimeRange::getEnd)
                .findFirst()
                .map(endTime -> {
                    LocalDateTime localDateTime = LocalDateTime.of(LocalDate.now(), endTime);
                    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
                })
                .orElse(null);
    }

    public boolean isCheckInTimeValid() {
        LocalTime now = LocalTime.now();
        return TimeConstants.VALID_CHECKIN_TIMES.stream().anyMatch(timeRange -> timeRange.isWithin(now));
    }

    public boolean exitsCheckinAtValidTimes(long userId) {
        LocalDate now = LocalDate.now();
        return TimeConstants.VALID_CHECKIN_TIMES.stream().anyMatch(timeRange -> {
            if (timeRange.isWithin(LocalTime.now())) {
                Date startTime = Date.from(LocalDateTime.of(now, timeRange.getStart()).atZone(ZoneId.systemDefault()).toInstant());
                Date endTime = Date.from(LocalDateTime.of(now, timeRange.getEnd()).atZone(ZoneId.systemDefault()).toInstant());
                return turnHistoryRepository.existsByUserIdAndCreateAtBetween(userId, startTime, endTime);
            }
            return false;
        });
    }

}
