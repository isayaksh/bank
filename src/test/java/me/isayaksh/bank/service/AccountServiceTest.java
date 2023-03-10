package me.isayaksh.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import me.isayaksh.bank.dto.account.AccountResDto.AccountDepositResDto;
import me.isayaksh.bank.dto.account.AccountResDto.AccountRegisterResDto;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.AccountStatus;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.MemberRepository;
import me.isayaksh.bank.repository.TransactionRepository;
import me.isayaksh.bank.util.CustomDateUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private TransactionRepository transactionRepository;

    private ObjectMapper mapper = new ObjectMapper();

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

        assertThat(register.getId()).isEqualTo(1L);
        assertThat(register.getBalance()).isEqualTo(1234L);
        assertThat(register.getNumber()).isEqualTo(7282L);

    }

    @Test
    public void accountDelete_test() throws Exception {
        // given
        Long memberId = 1L;
        Long number = 7282L;

        // when
        Member member = newMockMember(2L, "ssar", "쌀");
        Account account = newMockAccount(1L, 7282L, 1234L, member);

        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account));

        Account findAccount = accountRepository.findByNumber(number).orElseThrow(() -> new CustomApiException("해당 계좌가 존재하지 않습니다."));

        assertThrows(CustomApiException.class, () -> {
            findAccount.checkOwner(memberId);
        });

        accountRepository.deleteById(findAccount.getId());

        // then
    }

    @Test
    public void deposit_test() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setStatus("DEPOSIT");
        accountDepositReqDto.setTel("01012345678");

        Member member = newMockMember(1L, "ssar", "쌀");
        Account account1 = newMockAccount(1L, 1111L, 1000L, member);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account1));

        Account account2 = newMockAccount(1L, 1111L, 1000L, member);
        Transaction transaction = newMockTransaction(1L, account2);
        when(transactionRepository.save(any())).thenReturn(transaction);


        // stub이 진행될때마다 연관된 객체는 새로 만들어서 주입하기 - 타이밍 때문에 꼬인다.
        // when
        AccountDepositResDto accountDepositResDto = accountService.deposit(accountDepositReqDto);
        System.out.println("accountDepositResDto = " + accountDepositResDto.getTransactionDto().getDepositAmountBalance());
        System.out.println("account = " + account2.getBalance());

        // then
        assertThat(accountDepositResDto.getTransactionDto().getDepositAmountBalance()).isEqualTo(1100L);
        assertThat(account2.getBalance()).isEqualTo(1100L);

    }

    @Test
    public void deposit_test2() throws Exception {
        // given
        AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(0L);
        accountDepositReqDto.setStatus("DEPOSIT");
        accountDepositReqDto.setTel("01012345678");

        // when
        assertThrows(CustomApiException.class, () -> {
            accountService.deposit(accountDepositReqDto);
        });

        accountDepositReqDto.setAmount(1000L);

        Member member = newMockMember(1L, "ssar", "쌀");
        Account account1 = newMockAccount(1L, 1111L, 1000L, member);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account1));

        Member member2 = newMockMember(1L, "ssar", "쌀");
        Account account2 = newMockAccount(1L, 1111L, 1000L, member2);
        Transaction transaction = newMockTransaction(1L, account2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        AccountDepositResDto dto = accountService.deposit(accountDepositReqDto);
        String responseBody = mapper.writeValueAsString(dto);
        System.out.println("responseBody = " + responseBody);

        // then
        assertThat(dto.getNumber()).isEqualTo(1111L);
        assertThat(dto.getTransactionDto().getStatus()).isEqualTo(AccountStatus.DEPOSIT.getStatus());
        assertThat(dto.getTransactionDto().getReceiver()).isEqualTo("1111");
        assertThat(dto.getTransactionDto().getAmount()).isEqualTo(100L);

    }

}