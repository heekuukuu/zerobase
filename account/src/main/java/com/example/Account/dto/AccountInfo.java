package com.example.Account.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

 // AccountDto와 겹치지만
//  분리를 시켜 특정용도로 만드는것이 효율 극대화

public class AccountInfo {
    private String accountNumber;
    private Long balance;

}
