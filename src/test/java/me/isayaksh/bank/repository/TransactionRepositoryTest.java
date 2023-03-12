package me.isayaksh.bank.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest
class TransactionRepositoryTest extends DummyObject {


    @Autowired private TransactionRepository transactionRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private EntityManager em;

    @BeforeEach
    private void setup() {
        autoIncrementReset();
        dataSetting();
        em.flush();
        em.clear();
    }

    @Test
    public void findAll_test() throws Exception {
        List<Transaction> transactionList = transactionRepository.findAll();
        for (Transaction transaction : transactionList) {
            System.out.println(transaction.getId() + ") " + transaction.getSender() + " → " + transaction.getReceiver() + " / " + transaction.getAmount() + " / " + transaction.getStatus());
        }
    }

    @Test
    public void findAllTransactionList_test() throws Exception {
        Long accountId = 1L;
        List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "ALL", 0);
        System.out.println("============ ALL ============");
        transactionList.forEach(t -> {
            System.out.println(t.getSender() + " → " + t.getReceiver() + " / " + t.getAmount() + " / " + t.getStatus());
        });
    }

    @Test
    public void findDepositTransactionList_test() throws Exception {
        Long accountId = 1L;
        List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "DEPOSIT", 0);
        System.out.println("============ DEPOSIT ============");
        transactionList.forEach(t -> {
            System.out.println(t.getSender() + " → " + t.getReceiver() + " / " + t.getAmount() + " / " + t.getStatus());
        });
    }

    @Test
    public void findWithdrawTransactionList_test() throws Exception {
        Long accountId = 1L;
        List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "WITHDRAW", 0);
        System.out.println("============ WITHDRAW ============");
        transactionList.forEach(t -> {
            System.out.println(t.getSender() + " → " + t.getReceiver() + " / " + t.getAmount() + " / " + t.getStatus());
        });
    }

    private void dataSetting() {
        Member ssar = memberRepository.save(newMember("ssar", "쌀"));
        Member cos = memberRepository.save(newMember("cos", "코스,"));
        Member love = memberRepository.save(newMember("love", "러브"));
        Member admin = memberRepository.save(newMember("admin", "관리자"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, 1234L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, 1234L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, 1234L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, 1234L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }

    private void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE member ALTER COLUMN `member_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE account ALTER COLUMN `account_id` RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE transaction ALTER COLUMN `transaction_id` RESTART WITH 1").executeUpdate();
    }
}