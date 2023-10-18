package com.datpd.checkin.service;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.TurnHistoryEntity;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.mapper.UserMapper;
import com.datpd.checkin.repository.TurnHistoryRepository;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.util.CheckInService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final TurnHistoryRepository turnHistoryRepository;

    private final RedissonClient redissonClient;

    private final UserMapper mapper;

    private final CheckInService checkInService;

    public UserService(UserRepository userRepository,
                       TurnHistoryRepository turnHistoryRepository,
                       UserMapper mapper,
                       CheckInService checkInService,
                       RedissonClient redissonClient) {
        this.userRepository = userRepository;
        this.turnHistoryRepository = turnHistoryRepository;
        this.redissonClient = redissonClient;
        this.mapper = mapper;
        this.checkInService = checkInService;
    }

    public UserDto getUserById(long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return optionalUserEntity.map(mapper::mapFromEntityToDto).orElse(null);
    }

    @Transactional(
            isolation = Isolation.SERIALIZABLE
    )
    public void checkInByUserId(long userId) throws Exception {

        if (checkInService.isCheckInTimeValid()) {
            RBucket<String> bucket = redissonClient.getBucket("checkin_" + userId);

            if (bucket.isExists())
                throw new Exception("Check-in already marked");

            logger.info("Add turn for user");
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            long balance = userEntity.getTurn();
            userEntity.setTurn(balance + 1);
            userRepository.save(userEntity);

            logger.info("Create Turn History");
            TurnHistoryEntity turnHistoryEntity = new TurnHistoryEntity();
            turnHistoryEntity.setUserId(userId);
            turnHistoryEntity.setAmount(1);
            turnHistoryEntity.setBalance(balance);
            turnHistoryEntity.setCreateAt(new Date());
            turnHistoryRepository.save(turnHistoryEntity);

            bucket.set("checkedIn");
            bucket.expire(checkInService.getExpiryTime().toInstant());
        } else
            throw new Exception("Invalid check-in time");
    }

}
