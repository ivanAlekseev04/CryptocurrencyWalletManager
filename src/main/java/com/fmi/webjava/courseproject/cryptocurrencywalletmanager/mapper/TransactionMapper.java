package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.TransactionDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO transactionToTransactionDTO(TransactionDTO transaction);

    @Mapping(target = "id", ignore = true)
    Transaction transactionDTOtoTransaction(TransactionDTO transactionDTO);
}
