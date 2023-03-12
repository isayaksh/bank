package me.isayaksh.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.MemberRepository;
import me.isayaksh.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class TransactionControllerTest extends DummyObject {

    @Autowired
    private ObjectMapper mapper;
    @Autowired private MockMvc mvc;

    @Autowired MemberRepository memberRepository;
    @Autowired AccountRepository accountRepository;
    @Autowired TransactionRepository transactionRepository;
    @Autowired EntityManager em;

    @BeforeEach
    public void setup() {
        dataSetting();
        em.clear();
    }

    @Test
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void findTransactionList_test() throws Exception {
        // given
        Long number = 1111L;
        String status = "ALL";

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/account/"+number+"/transactions").param("status", status));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
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

}