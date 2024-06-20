package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.CryptoDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Crypto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CryptoMapper {
    CryptoDTO assetToAssetDTO(Crypto crypto);

    @Mapping(target = "id", ignore = true)
    Crypto assetDTOToAsset(CryptoDTO cryptoDTO);
}
