package com.datpd.checkin.mapper;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import com.datpd.checkin.util.CheckInService;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    private final CheckInService checkInService;

    public UserMapper(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    public UserDto mapFromEntityToDto(UserEntity from) {
        UserDto to = new UserDto();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEnableCheckin(checkInService.isCheckInTimeValid());
        to.setTurn(from.getTurn());
        return to;
    }
}
