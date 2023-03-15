package me.isayaksh.bank.dto.member;

import lombok.Getter;
import lombok.Setter;
import me.isayaksh.bank.entity.member.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static me.isayaksh.bank.entity.member.MemberRole.CUSTOMER;

public class MemberReqDto {
    @Getter
    @Setter
    public static class MemberJoinReqDto {

        @NotEmpty(message = "username은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String username;

        @NotEmpty(message = "password는 필수입니다")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String password;

        @NotEmpty(message = "email은 필수입니다")
        @Size(min = 9, max = 20)
        @Pattern(regexp = "^[a-zA-Z0-9.]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 적어주세요")
        private String email;

        @NotEmpty(message = "fullname은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요.")
        private String fullName;

        public Member toEntity(PasswordEncoder passwordEncoder) {
            return Member.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullName(fullName)
                    .role(CUSTOMER)
                    .build();
        }
    }

    @Getter
    @Setter
    public static class LoginReqDto {
        @NotEmpty(message = "username은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String username;

        @NotEmpty(message = "password는 필수입니다")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String password;
    }

    @Getter @Setter
    public static class MemberResetPasswordReqDto {
        @NotEmpty(message = "username은 필수입니다")
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        private String username;

        @NotEmpty(message = "password는 필수입니다")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String password;

        @NotEmpty(message = "newPassword는 필수입니다")
        @Size(min = 4, max = 20) // 패스워드 인코딩 때문에
        private String newPassword;
    }
}
