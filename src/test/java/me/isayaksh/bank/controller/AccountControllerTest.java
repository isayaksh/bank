package me.isayaksh.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.isayaksh.bank.config.dummy.DummyObject;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountDepositReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountTransferReqDto;
import me.isayaksh.bank.dto.account.AccountReqDto.AccountWithdrawReqDto;
import me.isayaksh.bank.entity.account.Account;
import me.isayaksh.bank.entity.member.Member;
import me.isayaksh.bank.entity.transaction.Transaction;
import me.isayaksh.bank.handler.ex.CustomApiException;
import me.isayaksh.bank.repository.AccountRepository;
import me.isayaksh.bank.repository.MemberRepository;
import me.isayaksh.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.JsonPath;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static me.isayaksh.bank.dto.account.AccountReqDto.AccountRegisterReqDto;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("classpath:db/teardown.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class AccountControllerTest extends DummyObject {

    @Autowired private ObjectMapper mapper;
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

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void saveAccount_test() throws Exception {
        // given
        AccountRegisterReqDto accountRegisterReqDto = new AccountRegisterReqDto();
        accountRegisterReqDto.setNumber(2143L);
        accountRegisterReqDto.setPassword(1234L);

        String requestBody = mapper.writeValueAsString(accountRegisterReqDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void findAccountList_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/account/login-user"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void deleteAccountTest() throws Exception {
        // given

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/account/" + 7282));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);


        assertThrows(CustomApiException.class, () -> {
            accountRepository.findByNumber(7282L).orElseThrow(() -> new CustomApiException("?????? ????????? ???????????? ???????????????."));
        });

        // then
    }


    @Test
    public void deposit_test() throws Exception {
        // given

        AccountDepositReqDto dto = new AccountDepositReqDto();
        dto.setNumber(1111L);
        dto.setAmount(100L);
        dto.setStatus("DEPOSIT");
        dto.setTel("01012345678");
        String requestBody = mapper.writeValueAsString(dto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/account/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void withdraw_test() throws Exception {
        // given
        AccountWithdrawReqDto dto = new AccountWithdrawReqDto();
        dto.setNumber(1111L);
        dto.setPassword(1234L);
        dto.setAmount(100L);
        dto.setStatus("WITHDRAW");
        String requestBody = mapper.writeValueAsString(dto);
        // when

        ResultActions resultActions = mvc.perform(post("/api/s/account/withdraw").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);
        // then

        resultActions.andExpect(status().isCreated());
    }

    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    public void transfer_test() throws Exception {
        // given
        AccountTransferReqDto reqDto = new AccountTransferReqDto();
        reqDto.setWithDrawNumber(1111L);
        reqDto.setDepositNumber(2222L);
        reqDto.setWithDrawPassword(1234L);
        reqDto.setAmount(100L);
        reqDto.setStatus("TRANSFER");
        String requestBody = mapper.writeValueAsString(reqDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/transfer").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void findAccountDetail_test() throws Exception {
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
        Member ssar = memberRepository.save(newMember("ssar", "???"));
        Member cos = memberRepository.save(newMember("cos", "??????,"));
        Member love = memberRepository.save(newMember("love", "??????"));
        Member admin = memberRepository.save(newMember("admin", "?????????"));

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