package com.example.Account.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Author: leeheekyung
 * Date: 24. 6. 22.
 * Description: Account
 */

public class CreateAccount {
    @Getter
    @Setter
    @AllArgsConstructor

    public static class Request{
        @NotNull
        @Min(1)
        private Long userId; // 1부터

        @NotNull
        @Min(0)
        private Long initialBalance;

    }


     @Getter
     @Setter
     @NoArgsConstructor
     @AllArgsConstructor
     @Builder

    public static class Response{
         private Long userId;
         private String accountNumber;
         private LocalDateTime registeredAt;


         public static Response from(AccountDto accountDto){
             return Response.builder()
                     .userId(accountDto.getUserId())
                     .accountNumber(accountDto.getAccountNumber())
                     .registeredAt(accountDto.getRegisteredAt())
                     .build();

         }
    }
}


