package com.datpd.checkin.mapper;

import com.datpd.checkin.dto.UserDto;
import com.datpd.checkin.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {


    // TODO: missing enableCheckin
    public UserDto mapFromEntityToDto(UserEntity from) {
        UserDto to = new UserDto();
        to.setId(from.getId());
        to.setName(from.getName());
        to.setEnableCheckin(false);
        to.setTurn(from.getTurn());
        return to;
    }
}
