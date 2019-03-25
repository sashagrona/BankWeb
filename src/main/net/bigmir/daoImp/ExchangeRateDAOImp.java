package net.bigmir.daoImp;

import net.bigmir.abstractDao.BankDAO;
import net.bigmir.model.ExchangeRate;

import javax.persistence.EntityManager;

public class ExchangeRateDAOImp extends BankDAO<ExchangeRate> {
    private EntityManager em;

    public ExchangeRateDAOImp(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public ExchangeRate find(Long id, Class<ExchangeRate> cls) {
        return super.find(id, cls);
    }
}
