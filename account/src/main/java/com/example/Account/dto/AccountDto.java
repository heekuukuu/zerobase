package com.example.Account.dto;

import com.example.Account.domain.Account;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor    //빌더재료 1
@AllArgsConstructor   //빌더재료 2
@Builder


public class AccountDto {
    private Long userId;
    private String accountNumber;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    public static AccountDto fromEntity(Account account) {
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .registeredAt(account.getRegisteredAt())
                .unRegisteredAt(account.getUnregisteredAt())
                .build();
    }
}
