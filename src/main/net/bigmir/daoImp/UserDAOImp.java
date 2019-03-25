package net.bigmir.daoImp;

import net.bigmir.abstractDao.BankDAO;
import net.bigmir.model.Account;
import net.bigmir.model.User;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class UserDAOImp extends BankDAO<User> {
    private EntityManager em;

    public UserDAOImp(EntityManager em) {
        super(em);
        this.em = em;
    }

    @Override
    public void add(User user) {
        super.add(user);
    }

    @Override
    public User find(Long id, Class<User> cls) {
        return super.find(id, cls);
    }

    public List<Account> getAccounts(User user){
        try {
            Query query = em.createQuery("SELECT acc FROM Account acc WHERE user_id=:userID");
            query.setParameter("userID",user.getPassportID());
            List<Account> list = query.getResultList();
            return list;
        } catch (NullPointerException e) {
            System.err.println("You don't have any accounts yet");
        }
        return null;
    }
}
