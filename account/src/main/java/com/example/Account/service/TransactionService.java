package com.example.Account.service;


import com.example.Account.domain.Account;
import com.example.Account.domain.AccountStatus;
import com.example.Account.domain.AccountUser;
import com.example.Account.domain.Transaction;
import com.example.Account.dto.TransactionDto;
import com.example.Account.exception.AccountException;
import com.example.Account.repository.AccountRepository;
import com.example.Account.repository.AccountUserRepository;
import com.example.Account.repository.TransactionRepository;
import com.example.Account.type.ErrorCode;
import com.example.Account.type.TransactionResultType;
import com.example.Account.type.TransactionType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.example.Account.type.TransactionResultType.F;
import static com.example.Account.type.TransactionResultType.S;
import static com.example.Account.type.TransactionType.USE;


@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountUserRepository accountUserRepository;
    private final AccountRepository accountRepository;

    /*
    사용자가 없는경우, 계좌가 없는경우,사용자 아이디와 계좌 소유주가 다른경우,
    계좌가 이미 해지 상태인경우, 거래 금액이 전보다 큰경우,
    거래 금액이 너무 작거나 큰경우 실패 응답

    */
    @Transactional
    public TransactionDto useBalance(Long userId, String accountNumber, Long amount) {

        AccountUser user = accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(ErrorCode.USER_NOT_FOUND));
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));


        validateUseBalance(user, account, amount);

        account.useBalance(amount);

        return TransactionDto.fromEntity(saveAndGetTransaction(S, account, amount));

    }

    private void validateUseBalance(AccountUser user, Account account, Long amount) {
        if (!Objects.equals(user.getId(), account.getAccountUser().getId())){
            throw new AccountException(ErrorCode.USER_ACCOUNT_UN_MATCH);
        }
        if (account.getAccountStatus() != AccountStatus.IN_USE) {
            throw new AccountException(ErrorCode.ACCOUNT_ALREADY_UNREGISTERED);
        }
        if (account.getBalance() < amount) {
            throw new AccountException(ErrorCode.AMOUNT_EXCEED_BALANCE);
        }
    }
    @Transactional
    public void saveFailedUseTransaction(String accountNumber, Long amount) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));

       saveAndGetTransaction(F, account, amount);

    }

    private Transaction saveAndGetTransaction(
            TransactionResultType transactionResultType,
            Account account, Long amount) {
        return transactionRepository.save(
                Transaction.builder()
                        .transactionType(USE)
                        .transactionResultType(transactionResultType)
                        .amount(amount)
                        .account(account)
                        .balanceSnapshot(account.getBalance())
                        .transactionId(UUID.randomUUID().toString())
                        .transactedAt(LocalDateTime.now())
                        .build()



        );
    }








}

