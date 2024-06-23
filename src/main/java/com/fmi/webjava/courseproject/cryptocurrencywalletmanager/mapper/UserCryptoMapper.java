package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserCryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserCryptoIdMapper.class})
public interface UserCryptoMapper {

    UserCryptoDTO userCryptoToUserCryptoDTO(UserCrypto userCrypto);

    UserCrypto userCryptoDTOToUserCrypto(UserCryptoDTO userCryptoDTO);

    Set<UserCryptoDTO> setUserCryptoToSetUserCryptoDTO(Set<UserCrypto> userCryptos);

    Set<UserCrypto> setUserCryptoDTOToSetUserCrypto(Set<UserCryptoDTO> userCryptoDTOS);
}
