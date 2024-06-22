package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserCryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCrypto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserCryptoIdMapper.class})
public interface UserCryptoMapper {

    @Mapping(source = "id", target = "idDTO")
    UserCryptoDTO userCryptoToUserCryptoDTO(UserCrypto userCrypto);

    @Mapping(source = "idDTO", target = "id")
    UserCrypto userCryptoDTOToUserCrypto(UserCryptoDTO userCryptoDTO);

    @Mapping(source = "id", target = "idDTO")
    Set<UserCryptoDTO> setUserCryptoToSetUserCryptoDTO(Set<UserCrypto> userCryptos);

    @Mapping(source = "idDTO", target = "id")
    Set<UserCrypto> setUserCryptoDTOToSetUserCrypto(Set<UserCryptoDTO> userCryptoDTOS);
}
