package me.isayaksh.bank.entity.account;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.isayaksh.bank.entity.member.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private Long number;

    @Column(nullable = false, length = 4)
    private Long password;

    @Column(nullable = false)
    private Long balance;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @CreatedDate // Insert
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate // Insert or Update
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public Account(Long id, Long number, Long password, Long balance, Member member, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.number = number;
        this.password = password;
        this.balance = balance;
        this.member = member;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}