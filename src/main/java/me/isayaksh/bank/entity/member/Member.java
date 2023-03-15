package me.isayaksh.bank.entity.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.isayaksh.bank.handler.ex.CustomApiException;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 60) // (Bcrypt)
    private String password;

    @Column(nullable = false, length = 20)
    private String email;

    @Column(nullable = false, length = 20)
    private String fullName;

    @Enumerated(STRING)
    @Column(nullable = false)
    private MemberRole role;

    @CreatedDate // Insert
    @Column(updatable = false, nullable = false)
    private LocalDateTime createAt;

    @LastModifiedDate // Insert or Update
    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public Member(Long id, String username, String password, String email, String fullName, MemberRole role, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public void checkId(Long id) {
        if (!this.id.equals(id)) {
            throw new CustomApiException("아이디가 일치하지 않습니다.");
        }
    }

    public void checkPassword(String password) {
        if (this.password.equals(password)) {
            throw new CustomApiException("패스워드가 일치하지 않습니다.");
        }
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
