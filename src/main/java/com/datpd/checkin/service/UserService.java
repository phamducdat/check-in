package com.datpd.checkin.service;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.mapper.UserMapper;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.util.CheckInService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final TurnHistoryService turnHistoryService;
    private final UserMapper mapper;

    private final CheckInService checkInService;

    public UserService(UserRepository userRepository,
                       TurnHistoryService turnHistoryService,
                       UserMapper mapper,
                       CheckInService checkInService) {
        this.userRepository = userRepository;
        this.turnHistoryService = turnHistoryService;
        this.mapper = mapper;
        this.checkInService = checkInService;
    }

    public UserDto getUserById(long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return optionalUserEntity.map(mapper::mapFromEntityToDto).orElse(null);
    }

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
                turnHistoryService.createTurnHistory(
                        userEntity.getId(),
                        1L,
                        balance);
            }
            return mapper.mapFromEntityToDto(userEntity);
        } else
            return null;
    }
}
