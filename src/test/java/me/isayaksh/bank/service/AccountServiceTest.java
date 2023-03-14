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
        Transaction transaction = newMockDepositTransaction(1L, account2);
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
        Transaction transaction = newMockDepositTransaction(1L, account2);
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

    @Test
    public void withdraw_test() throws Exception {
        // given
        Member member = newMockMember(1L, "username", "userFullName");
        Account account = newMockAccount(1L, 1234L, 1000L, member);

        // when

        // 0. 출금액 확인하기
        Long amount = 100L;
        if(amount <= 0L) {
            throw new CustomApiException("0원 이하의 금액은 출금할 수 없습니다.");
        }
        assertThrows(CustomApiException.class, () -> {
            Long amount_fail = 0L;
            if(amount_fail <= 0L) {
                throw new CustomApiException("0원 이하의 금액은 출금할 수 없습니다.");
            }
        });

        // 2. 계좌와 계좌 주인 일치 여부
        account.checkOwner(1L);
        assertThrows(CustomApiException.class, () -> {
            account.checkOwner(2L);
        });

        // 3. 비밀번호 일치 여부
        account.checkPassword(1234L);
        assertThrows(CustomApiException.class, () -> {
           account.checkPassword(9999L);
        });

        // 4. 출금액과 잔액 비교 여부
        account.checkBalance(100L);
        assertThrows(CustomApiException.class, () -> {
            account.checkBalance(9999L);
        });

        // 5. 출금
        account.withdraw(100L);
        assertThat(account.getBalance()).isEqualTo(900L);

    }

    @Test
    public void transfer_test() throws Exception {
        // given

        Long amount = 100L;
        Long memberId = 1L;

        Member m1 = newMockMember(1L, "kim", "minjae");
        Account a1 = newMockAccount(1L, 1111L, 2000L, m1);
        Member m2 = newMockMember(2L, "son", "huengmin");
        Account a2 = newMockAccount(2L, 2222L, 2000L, m2);
        // when

        // 출금계좌 != 입금계좌
        if(a1.getNumber().equals(a2.getNumber())){
            throw new CustomApiException("입금계좌와 출금계좌가 동일할 순 없습니다.");
        }
        // 출금액 확인하기
        if(amount <= 0L) {
            throw new CustomApiException("0원 이하의 금액은 출금할 수 없습니다.");
        }
        // 출금 계좌, 입금 계좌 조회 → Repository
//        Account withdrawAccount = findAccountByNumber(accountTransferReqDto.getWithDrawNumber());
//        Account depositAccount = findAccountByNumber(accountTransferReqDto.getDepositNumber());

        // 출금 계좌와 계좌 주인 일치 여부
        a1.checkOwner(memberId);
        // 비밀번호 일치 여부
        a1.checkPassword(1234L);
        // 출금액과 잔액 비교 여부
        a1.checkBalance(amount);
        // 이체하기
        a1.withdraw(amount);
        a2.deposit(amount);

        // then
        assertThat(a1.getBalance()).isEqualTo(1900L);
        assertThat(a2.getBalance()).isEqualTo(2100L);
    }

    @Test
    public void resetPassword_test() throws Exception {
        // given
        Account account = newAccount(1234L, 1234L, newMember("kim", "mj"));
        // when
        account.resetPassword(4321L);
        // then
        assertThat(account.getPassword()).isEqualTo(4321L);
    }

}

    ;