package com.datpd.checkin.util;

public enum CacheKeyEnum {

    USER_CHECKIN("user:checkin"),
    USER_DTO("user:dto"),

    USER_EXIT_CHECKIN_AT_VALID_TIMES("user:checkin:at:valid:times");


    private static final String PREFIX = "wiinvent";
    private final String key;

    CacheKeyEnum(String key) {
        this.key = key;
    }

    public String genKey(long id) {
        return PREFIX + ":" + key + ":" + id;
    }
}
