package com.example.Account.controller;

import com.example.Account.domain.Account;
import com.example.Account.dto.AccountInfo;
import com.example.Account.dto.CreateAccount;
import com.example.Account.dto.DeleteAccount;
import com.example.Account.service.AccountService;
import com.example.Account.service.RedisTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RestController
@RequiredArgsConstructor

public class AccountController {
    private final RedisTestService redisTestService;
    private final AccountService accountService;

    @PostMapping("/acccount")
    public CreateAccount.Response createAccount(
             @RequestBody @Valid CreateAccount.Request request
    ) {
        return CreateAccount.Response.from(
                accountService.createAccount(
                request.getUserId(),
                request.getInitialBalance()
                )
                );
    }

    @DeleteMapping("/acccount")
    public DeleteAccount.Response deleteAccount(
            @RequestBody @Valid DeleteAccount.Request request
    ) {
        return DeleteAccount.Response.from(
                accountService.deleteAccount(
                        request.getUserId(),
                        request.getAccountNumber()
                )
        );
    }

    @GetMapping ("/acccount")
    public List<AccountInfo> getAccountByUserId(
             @RequestParam("User_id") Long userId

    ){
      return accountService.getAccountByUserId(userId)
              .stream().map(accountDto ->
                       AccountInfo.builder()
                      .accountNumber(accountDto.getAccountNumber())
                      .balance(accountDto.getBalance())
                      .build())
              .collect(Collectors.toList());

    }


    @GetMapping("/get-lock")
    public String getLock() {
        return redisTestService.getLock();
    }


    @GetMapping("/account/{id}")
    public Account getAccount(
            @PathVariable Long id) {
        return accountService.getAccount(id);
    }

}
