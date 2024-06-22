package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.UserCryptoIdDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.usercrypto.UserCryptoId;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserCryptoIdMapper {
    UserCryptoIdDTO userCryptoIdToUserCryptoIdDTO(UserCryptoId userCryptoId);
    UserCryptoId userCryptoIdDTOToUserCryptoId(UserCryptoIdDTO userCryptoIdDTO);
    Set<UserCryptoIdDTO> setUserCryptoIdToSetUserCryptoIdDTO(Set<UserCryptoId> userCryptoIds);
    Set<UserCryptoId> setUserCryptoIdDTOToSetUserCryptoId(Set<UserCryptoIdDTO> userCryptoIdDTOS);
}
