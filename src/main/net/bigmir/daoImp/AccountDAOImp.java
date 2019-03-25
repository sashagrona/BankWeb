package net.bigmir.daoImp;

import net.bigmir.abstractDao.BankDAO;
import net.bigmir.model.Account;

import javax.persistence.EntityManager;

public class AccountDAOImp extends BankDAO<Account> {
    private EntityManager em;

    public AccountDAOImp(EntityManager em) {
        super(em);
        this.em = em;
    }


    @Override
    public Account find(Long id, Class<Account> cls) {
        return super.find(id, cls);
    }

    @Override
    public void add(Account account) {
        super.add(account);
    }

}
