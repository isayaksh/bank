package me.isayaksh.bank.repository;

import me.isayaksh.bank.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
