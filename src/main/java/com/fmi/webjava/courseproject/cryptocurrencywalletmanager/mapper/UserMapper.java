package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TransactionMapper.class})
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    User userDTOtoUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);
}
