package com.datpd.checkin.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
public class TimeRange {
    private LocalTime start;
    private LocalTime end;

    public boolean isWithin(LocalTime time) {
        return time.isAfter(start) && time.isBefore(end);
    }
}
