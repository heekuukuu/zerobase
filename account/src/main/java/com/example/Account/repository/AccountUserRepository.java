package com.example.Account.repository;

import com.example.Account.domain.Account;
import com.example.Account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountUserRepository extends JpaRepository <Account, Long>
{
    Optional<Account> findFirstByOrderByIDesc();

   Integer countByAccountUser (AccountUser accountUser);

    Object findByAccountUser(Object any);
}
