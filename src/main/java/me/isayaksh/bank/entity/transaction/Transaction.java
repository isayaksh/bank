package me.isayaksh.bank.entity.transaction;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.isayaksh.bank.entity.account.Account;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account withdrawAccount;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private Account depositAccount;

    @Column(nullable = false)
    private Long amount;

    private Long withdrawAccountBalance;
    private Long depositAccountBalance;

    @Column(nullable = false)
    @Enumerated(STRING)
    private AccountStatus status;

    @CreatedDate // Insert
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate // Insert or Update
    @Column(nullable = false)
    private LocalDateTime updateAt;

    // Account 삭제시에도 데이터 보존
    private String sender;
    private String receiver;
    private String tel;

    @Builder
    public Transaction(Long id, Account withdrawAccount, Account depositAccount, Long amount, Long withdrawAccountBalance, Long depositAccountBalance,
                       AccountStatus status, LocalDateTime createAt, LocalDateTime updateAt, String sender, String receiver, String tel) {
        this.id = id;
        this.withdrawAccount = withdrawAccount;
        this.depositAccount = depositAccount;
        this.amount = amount;
        this.withdrawAccountBalance = withdrawAccountBalance;
        this.depositAccountBalance = depositAccountBalance;
        this.status = status;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.sender = sender;
        this.receiver = receiver;
        this.tel = tel;
    }
}
