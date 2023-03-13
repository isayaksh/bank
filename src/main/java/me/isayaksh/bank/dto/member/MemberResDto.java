package me.isayaksh.bank.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.util.CustomDateUtil;

public class MemberResDto {

    @Getter
    @Setter
    public static class LoginResDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginResDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(member.getCreateAt());
        }
    }

    @Getter
    @Setter
    @ToString
    public static class JoinRespDto {
        private Long id;
        private String username;
        private String fullName;

        public JoinRespDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.fullName = member.getFullName();
        }
    }
}
