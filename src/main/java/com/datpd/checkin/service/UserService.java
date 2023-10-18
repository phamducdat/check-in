package com.datpd.checkin.service;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.TurnHistoryEntity;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.mapper.UserMapper;
import com.datpd.checkin.repository.TurnHistoryRepository;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.util.CheckInService;
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

    private final TurnHistoryService turnHistoryService;
    private final UserMapper mapper;

    private final CheckInService checkInService;

    public UserService(UserRepository userRepository,
                       TurnHistoryRepository turnHistoryRepository,
                       TurnHistoryService turnHistoryService,
                       UserMapper mapper,
                       CheckInService checkInService) {
        this.userRepository = userRepository;
        this.turnHistoryRepository = turnHistoryRepository;
        this.turnHistoryService = turnHistoryService;
        this.mapper = mapper;
        this.checkInService = checkInService;
    }

    public UserDto getUserById(long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return optionalUserEntity.map(mapper::mapFromEntityToDto).orElse(null);
    }

    @Transactional
    public UserDto checkInByUserId(long userId) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(userId);
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();

            // CheckInTimeValid
            if (checkInService.hasUserCheckedInDuringValidTimes(userId)) {
                logger.info("Add turn for user");
                long balance = userEntity.getTurn();
                userEntity.setTurn(userEntity.getTurn() + 1);
                userRepository.save(userEntity);

                // Create history
                logger.info("Create Turn History");
                TurnHistoryEntity turnHistoryEntity = new TurnHistoryEntity();
                turnHistoryEntity.setUserId(userId);
                turnHistoryEntity.setAmount(1);
                turnHistoryEntity.setBalance(balance);
                turnHistoryEntity.setCreateAt(new Date());
                turnHistoryRepository.save(turnHistoryEntity);

            }
            return mapper.mapFromEntityToDto(userEntity);
        } else
            return null;
    }
}
