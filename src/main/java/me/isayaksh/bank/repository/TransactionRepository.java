package me.isayaksh.bank.repository;

import me.isayaksh.bank.entity.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, TransactionRepositoryJpql {
}
