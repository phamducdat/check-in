package com.datpd.checkin.service;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.TurnHistoryEntity;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.mapper.UserMapper;
import com.datpd.checkin.repository.TurnHistoryRepository;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.util.CheckInService;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
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

    @Transactional
    public void checkInByUserId(long userId) throws Exception {

        if (checkInService.isCheckInTimeValid()) {
            RSet<String> set = redissonClient.getSet("checkin");
            String key = "checkin_" + userId;

            if (set.contains(key))
                throw new Exception("Check-in already marked");

            logger.info("Add turn for user");
            UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            long balance = userEntity.getTurn();
            userEntity.setTurn(userEntity.getTurn() + 1);
            userRepository.save(userEntity);

            logger.info("Create Turn History");
            TurnHistoryEntity turnHistoryEntity = new TurnHistoryEntity();
            turnHistoryEntity.setUserId(userId);
            turnHistoryEntity.setAmount(1);
            turnHistoryEntity.setBalance(balance);
            turnHistoryEntity.setCreateAt(new Date());
            turnHistoryRepository.save(turnHistoryEntity);

            set.add(key);
            set.expire(checkInService.getExpiryTime().toInstant());
        } else
            throw new Exception("Invalid check-in time");
    }

}
