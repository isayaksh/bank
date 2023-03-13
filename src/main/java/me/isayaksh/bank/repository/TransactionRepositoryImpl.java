package me.isayaksh.bank.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import me.isayaksh.bank.entity.transaction.Transaction;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import java.util.List;

import static me.isayaksh.bank.entity.transaction.QTransaction.transaction;

interface TransactionRepositoryJpql {
    List<Transaction> findTransactionList(@Param("accountId") Long accountId, @Param("status") String status, @Param("page") Integer page);
}

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements TransactionRepositoryJpql{

    private final EntityManager em;

    private final String DEPOSIT = "DEPOSIT";
    private final String WITHDRAW = "WITHDRAW";

    @Override
    public List<Transaction> findTransactionList(Long accountId, String status, Integer page) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        JPAQuery<Transaction> query = queryFactory.selectFrom(transaction);

        if(status.equals("DEPOSIT")) {
            query
                    .join(transaction.depositAccount)
                    .fetchJoin()
                    .where(transaction.depositAccount.id.eq(accountId));
        } else if (status.equals("WITHDRAW")) {
            query
                    .join(transaction.withdrawAccount)
                    .fetchJoin()
                    .where(transaction.withdrawAccount.id.eq(accountId));
        } else {
            query
                    .leftJoin(transaction.depositAccount)
                    .fetchJoin()
                    .leftJoin(transaction.withdrawAccount)
                    .fetchJoin()
                    .where(transaction.depositAccount.id.eq(accountId).or(transaction.withdrawAccount.id.eq(accountId)));
        }
        return query.fetch();
    }

}
