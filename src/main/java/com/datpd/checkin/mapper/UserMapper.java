package com.datpd.checkin.mapper;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.util.CacheKeyEnum;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    private final RedissonClient redissonClient;

    public UserMapper(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public UserDto mapFromEntityToDto(UserEntity from) {
        RBucket<String> bucket = redissonClient.getBucket(CacheKeyEnum.USER_CHECKIN.genKey(from.getId()));
        UserDto to = new UserDto();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEnableCheckin(!bucket.isExists());
        to.setTurn(from.getTurn());
        return to;
    }
}
