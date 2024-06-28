package com.fmi.webjava.courseproject.cryptocurrencywalletmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDTOInput {

    @NotNull(message = "User: userName cannot be null")
    @NotBlank(message = "User: userName need to have minimum 1 non-white space character")
    @Length(max = 30, message = "User: userName can have length maximum 30 symbols")
    private String userName;

    @NotNull(message = "User: password cannot be null")
    @NotBlank(message = "User: password need to have minimum 1 non-white space character")
    @Length(max = 256, message = "User: password can have length maximum 256 symbols")
    private String password;
}
