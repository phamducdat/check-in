package com.datpd.checkin.mapper;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    private final RedissonClient redissonClient;

    public UserMapper(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public UserDto mapFromEntityToDto(UserEntity from) {
        RSet<String> set = redissonClient.getSet("checkin");
        String key = "checkin_" + from.getId();
        UserDto to = new UserDto();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEnableCheckin(!set.contains(key));
        to.setTurn(from.getTurn());
        return to;
    }
}
