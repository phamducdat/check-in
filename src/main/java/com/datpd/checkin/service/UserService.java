package com.datpd.checkin.service;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.mapper.UserMapper;
import com.datpd.checkin.repository.UserRepository;
import com.datpd.checkin.util.CheckInService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    private final CheckInService checkInService;

    public UserService(UserRepository userRepository, UserMapper mapper, CheckInService checkInService) {
        this.userRepository = userRepository;
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
            if (checkInService.isCheckInTimeValid()) {
                userEntity.setTurn(userEntity.getTurn() + 1);
                userRepository.save(userEntity);

            }
            return mapper.mapFromEntityToDto(userEntity);
        } else
            return null;
    }
}
