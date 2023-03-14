package me.isayaksh.bank.dto.account;

import lombok.Getter;
import lombok.Setter;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;

import javax.validation.constraints.*;

public class AccountReqDto {

    @Getter @Setter
    public static class AccountRegisterReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(Member member) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .member(member)
                    .build();
        }

    }

    @Getter @Setter
    public static class AccountDepositReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String status;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{11}$")
        private String tel;
    }

    @Getter @Setter
    public static class AccountWithdrawReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(WITHDRAW)$")
        private String status;
    }

    @Getter @Setter
    public static class AccountTransferReqDto {

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withDrawNumber;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withDrawPassword;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long depositNumber;

        @NotNull
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(TRANSFER)$")
        private String status;

    }

    @Getter @Setter
    public static class AccountResetPasswordReqDto {
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long newPassword;

    }


}
