package me.isayaksh.bank.service;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.handler.ex.CustomApiException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest extends DummyObject {

    @Test
    public void findAll_test() throws Exception {
        // given
        Long successId = 1L;
        Long failId = 2L;

        Member member = newMockMember(1L, "son", "hm");
        Account account = newMockAccount(1L, 1111L, 1000L, member);

        // when & then
        // 계좌와 소유자 일치 여부 검증
        account.checkOwner(successId);
        assertThrows(CustomApiException.class, () -> account.checkOwner(failId));
    }

}