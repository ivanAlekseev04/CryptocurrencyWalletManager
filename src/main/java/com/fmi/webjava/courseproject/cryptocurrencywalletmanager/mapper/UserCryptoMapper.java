package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserCryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring"/*, uses = {UserCryptoIdMapper.class}*/)
public interface UserCryptoMapper {
    UserCrypto userCryptoDTOtoUserCrypto(UserCryptoDTO userCryptoDTO);

    UserCryptoDTO userCryptoToUserCryptoDTO(UserCrypto userCrypto);
}
