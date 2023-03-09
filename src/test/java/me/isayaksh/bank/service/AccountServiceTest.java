package me.isayaksh.bank.service;

import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import me.isayaksh.bank.dto.account.AccountResDto.AccountRegisterResDto;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MemberRepository memberRepository;


    @Test
    public void register_test() throws Exception {
        // given
        Long memberId = 1L;

        AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
        accountRegisterReqDto.setNumber(7282L);
        accountRegisterReqDto.setPassword(1234L);

        Member member = newMockMember(1L, "ssar", "쌀");
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));

        lenient().when(accountRepository.findByNumber(7282L)).thenReturn(Optional.empty());

        Account account = newMockAccount(1L, 7282L, 1234L, member);
        when(accountRepository.save(any())).thenReturn(account);

        AccountRegisterResDto register = accountService.register(memberId, accountRegisterReqDto);

        Assertions.assertThat(register.getId()).isEqualTo(1L);
        Assertions.assertThat(register.getBalance()).isEqualTo(1000L);
        Assertions.assertThat(register.getNumber()).isEqualTo(7282L);

    }

}