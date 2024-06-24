package com.example.Account.service;

import com.example.Account.domain.Account;
import com.example.Account.domain.AccountStatus;
import com.example.Account.domain.AccountUser;
import com.example.Account.dto.AccountDto;
import com.example.Account.exception.AccountException;
import com.example.Account.repository.AccountRepository;
import com.example.Account.repository.AccountUserRepository;
import com.example.Account.type.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.Account.domain.AccountStatus.IN_USE;
import static com.example.Account.type.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /* 사용자 의 유무 조회
       계좌 번호 생성 후 정보 넘기기 */
    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND)).getAccountUser();
        validateCreateAccount(accountUser);

        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account ->
                        String.valueOf(Integer.parseInt(account.getAccountNumber()+ 1) + "")
                .orElse("1000000000"));

        return AccountDto.fromEntity(
                accountRepository.save(Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build())
        );
    }

    private void validateCreateAccount(AccountUser accountUser) {
        if (accountRepository.countByAccountUser(accountUser) >= 10) {
            throw new AccountException(ErrorCode.MAX_ACCOUNT_PER_USER_10);
        }
    }

    @Transactional
    public Account getAccount(Long id) {
        if (id < 0) {
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

    }
    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND)).getAccountUser();
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

        validateDeleteAccount(accountUser, account);

        account.setAccountStatus(AccountStatus.UNREGISTERD);
        account.setRegisteredAt(LocalDateTime.now());

        accountRepository.save(account);

        return AccountDto.fromEntity(account);

    }
    private void  validateDeleteAccount(AccountUser accountUser, Account account){
        if (!Objects.equals(accountUser.getId(), account.getAccountUser().getId())){
            throw new AccountException(ACCOUNT_NOT_UN_MATCH);

        }
        if (account.getAccountStatus()==AccountStatus.UNREGISTERD){
            throw new AccountException(ACCOUNT_ALREADY_UNREGISTERED);
        }
        if (account.getBalance()> 0 ){
            throw new AccountException(BALANCE_NOT_EMPTY);
        }

    }
   @Transactional
    public List<AccountDto> getAccountByUserId(Long userId) {
        AccountUser accountUser = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND)).getAccountUser();

      List<Account> accounts = accountRepository
              .findByAccountUser(accountUser);

      return accounts.stream()
              .map(AccountDto::fromEntity)
              .collect(Collectors.toList());

}}