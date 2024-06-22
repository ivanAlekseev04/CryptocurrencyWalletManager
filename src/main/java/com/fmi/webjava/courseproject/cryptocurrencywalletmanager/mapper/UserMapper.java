package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOInput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserDTOOutput;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserCryptoMapper.class})
public interface UserMapper {
    User userDTOInputToUser(UserDTOInput input);

    @Mapping(source = "cryptoCurrencies", target = "cryptocurrenciesDTO")
    UserDTOOutput userToUserDTOOutput(User user);
}
