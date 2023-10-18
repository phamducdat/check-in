package com.datpd.checkin.util;

public enum CacheKeyEnum {

    USER("user");

    private static final String PREFIX = "wiinvent";
    private final String key;

    CacheKeyEnum(String key) {
        this.key = key;
    }

    public String genKey(long id) {
        return PREFIX + ":" + key + ":" + id;
    }
}