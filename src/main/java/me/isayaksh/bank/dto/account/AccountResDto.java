package me.isayaksh.bank.dto.account;

import lombok.Getter;
import lombok.Setter;
import me.isayaksh.bank.entity.account.Account;

public class AccountResDto {

    @Getter
    @Setter
    public static class AccountRegisterResDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountRegisterResDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }

}
