package net.bigmir.daoImp;

import net.bigmir.abstractDao.BankDAO;
import net.bigmir.model.Account;
import net.bigmir.model.ExchangeRate;
import net.bigmir.model.Transaction;
import net.bigmir.utils.Convertion;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class TransactionDAOImp extends BankDAO<Transaction> {

    private EntityManager em;
    private Convertion convertion = new Convertion(em);
    private ExchangeRateDAOImp rateDao;

    public TransactionDAOImp(EntityManager em,ExchangeRateDAOImp rateDao) {
        super(em);
        this.em = em;
        this.rateDao=rateDao;
    }

    @Override
    public void add(Transaction transaction) {
        super.add(transaction);
    }

    @Override
    public Transaction find(Long id, Class<Transaction> cls) {
        return super.find(id, cls);
    }

    public void doTransact(Transaction transaction) {
        ExchangeRate currency = transaction.getRate();
        Account from = transaction.getFrom();
        Account to = transaction.getWhom();
        try {
        if(from.getMoney().compareTo(convertion.doConvert(from.getRate(),transaction.getMoney(),currency,rateDao))<0){
            System.out.println("You don't have enough money");
                throw new Exception();

        }
            em.getTransaction().begin();
            from.setMoney(from.getMoney().subtract(convertion.doConvert(from.getRate(),transaction.getMoney(),currency,rateDao)).setScale(2,BigDecimal.ROUND_HALF_UP));
            to.setMoney(to.getMoney().add(convertion.doConvert(to.getRate(),transaction.getMoney(),currency,rateDao)).setScale(2,BigDecimal.ROUND_HALF_UP));
            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: transaction");
            em.getTransaction().rollback();
        }
    }

    public Transaction refill(Account account, BigDecimal money, ExchangeRate currency) {
        Transaction transaction = new Transaction(null,account,money,currency);
        try {
            em.getTransaction().begin();
            BigDecimal valuta=convertion.doConvert(account.getRate(),money,currency,rateDao);
            transaction.getWhom().setMoney(transaction.getWhom().getMoney().add(valuta).setScale(2,BigDecimal.ROUND_HALF_UP));

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Impossible to refill");
            em.getTransaction().rollback();
        }
        return transaction;
    }
}
