package me.isayaksh.bank.entity.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    ADMIN("관리자"), CUSTOMER("고객");
    private String role;
}
