package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.mapper;

import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto.TransactionDTO;
import com.fmi.webjava.courseproject.cryptocurrencywalletmanager.model.Transaction;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {CryptoMapper.class})
public interface TransactionMapper {

    TransactionDTO transactionToTransactionDTO(TransactionDTO transaction);

    Transaction transactionDTOtoTransaction(TransactionDTO transactionDTO);

    List<Transaction> transactionDTOListToTransactionList(List<TransactionDTO> transactionDTOList);

    List<TransactionDTO> transactionListToTransactionDTOList(List<Transaction> transactionList);
}
