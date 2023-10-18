package com.datpd.checkin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CheckInService {

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

}
