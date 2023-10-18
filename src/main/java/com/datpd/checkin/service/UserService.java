package com.datpd.checkin.service;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.TurnHistoryEntity;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.mapper.UserMapper;
import com.datpd.checkin.repository.TurnHistoryRepository;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.util.CacheKeyEnum;
import com.datpd.checkin.util.CheckInService;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
        RBucket<UserDto> userBucket = redissonClient.getBucket(CacheKeyEnum.USER_DTO.genKey(id));
        UserDto cachedUserDto = userBucket.get();

        if (cachedUserDto != null) {
            return cachedUserDto;
        }
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        UserDto userDto = optionalUserEntity.map(mapper::mapFromEntityToDto).orElse(null);

        if (userDto != null) {
            userBucket.set(userDto);
            userBucket.expire(10, TimeUnit.MINUTES);
        }

        return userDto;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void checkInByUserId(long userId) throws Exception {
        RBucket<String> checkInBucket = redissonClient.getBucket(CacheKeyEnum.USER_CHECKIN.genKey(userId));

        if (!checkInService.isCheckInTimeValid()) {
            throw new Exception("Invalid check-in time");
        }
        if (checkInBucket.isExists()) {
            throw new Exception("Check-in already marked");
        }

        try {
            RBucket<UserDto> userBucket = redissonClient.getBucket(CacheKeyEnum.USER_DTO.genKey(userId));
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

            checkInBucket.set(CacheKeyEnum.USER_CHECKIN.genKey(userId));
            checkInBucket.expire(checkInService.getExpiryTime().toInstant());

            userBucket.delete();

        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException)
                logger.error("Database error during check-in for user: " + userId, e);
            checkInBucket.delete();
            throw e;
        }
    }


}
