package com.example.Account.repository;

import com.example.Account.domain.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {
}
