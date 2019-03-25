package net.bigmir.abstractDao;

import javax.persistence.EntityManager;

public abstract class BankDAO<T> {
    private final EntityManager em;

    public BankDAO(EntityManager em) {
        this.em = em;
    }

    public void add(T t){
         try {
             em.getTransaction().begin();
             em.merge(t);
             em.getTransaction().commit();
         } catch (Exception e) {
             e.printStackTrace();
             System.out.println("Error: Can't add new " + t.getClass().getName());
             em.getTransaction().rollback();
         }
    }
    public T find(Long id, Class<T> cls) {
        T t = null;
        try {
            t = cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            System.err.println("No such object in DataBase");
        }
        T o = (T) em.find(t.getClass(), id);
        return o;
    }


}
