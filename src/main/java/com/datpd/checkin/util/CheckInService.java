package com.datpd.checkin.util;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class CheckInService {
    public boolean isCheckInTimeValid() {
        LocalTime now = LocalTime.now();
        return TimeConstants.VALID_CHECKIN_TIMES.stream().anyMatch(timeRange -> timeRange.isWithin(now));
    }
}
