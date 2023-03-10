package me.isayaksh.bank.repository;

import me.isayaksh.bank.entity.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(Long number);

    boolean existsByNumber(Long number);
    @Query("select a from Account a where a.member.id = :memberId")
    List<Account> findAllByMemberId(@Param("memberId") Long memberId);

}
