package com.datpd.checkin.util;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class TimeConstants {
    public static final List<TimeRange> VALID_CHECKIN_TIMES = Arrays.asList(
            new TimeRange(LocalTime.of(7,0), LocalTime.of(9,0)),
            new TimeRange(LocalTime.of(13,0), LocalTime.of(16,0)),
            new TimeRange(LocalTime.of(16,0), LocalTime.of(19,0))
    );
}
