package net.bigmir.utils;

import com.google.gson.Gson;
import net.bigmir.daoImp.ExchangeRateDAOImp;
import net.bigmir.fixer.Fixer;
import net.bigmir.model.Account;
import net.bigmir.model.ExchangeRate;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Convertion {
    private Fixer fixer;
    private EntityManager em;
    private List<ExchangeRate> list = new ArrayList<ExchangeRate>();

    public Convertion(EntityManager em) {
        this.em = em;
    }

    public void fillTable() {
        try {
            em.getTransaction().begin();
            ExchangeRate one = new ExchangeRate("UAH", BigDecimal.ONE.setScale(2,BigDecimal.ROUND_HALF_UP));
            ExchangeRate two = new ExchangeRate("USD", getFromFixerInUAH("USD"));
            ExchangeRate three = new ExchangeRate("EUR", getFromFixerInUAH("EUR"));
            em.persist(one);
            em.persist(two);
            em.persist(three);
            em.getTransaction().commit();
            list.add(one);
            list.add(two);
            list.add(three);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can't add new exchange");
            em.getTransaction().rollback();
        }
    }

    public ExchangeRate getRate(String currency) {
        Query query = em.createQuery("SELECT rate FROM ExchangeRate rate WHERE rate.currency =:currency",ExchangeRate.class);
         query.setParameter("currency",currency);
        ExchangeRate rate = (ExchangeRate) query.getSingleResult();
        return rate;
    }

    public BigDecimal getFromFixerInUAH(String curr) throws IOException {
        String url="http://data.fixer.io/api/latest?access_key=0fb9147e27b8f0235c4006323d58d469&base=EUR&symbols=USD,UAH&format=1";
        URL u = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) u.openConnection();
        InputStream is = httpURLConnection.getInputStream();
        byte[] buf = requestBodyToArray(is);
        Gson gson = new Gson();
        String strBuf = new String(buf, StandardCharsets.UTF_8);
        fixer = gson.fromJson(strBuf,Fixer.class);
        if("UAH".equals(curr)){
            return BigDecimal.ONE.setScale(2,BigDecimal.ROUND_HALF_UP);
        }else if("USD".equals(curr)){
            return (fixer.getRates().getUAH().divide(fixer.getRates().getUSD(),2,BigDecimal.ROUND_HALF_UP));
        }else if("EUR".equals(curr)){
            return fixer.getRates().getUAH().setScale(2,BigDecimal.ROUND_HALF_UP);
        }
        return null;

    }
    public BigDecimal doConvert(ExchangeRate one, BigDecimal money, ExchangeRate currency, ExchangeRateDAOImp rateDao){
        BigDecimal valuta=null;
        if (currency.getCurrency().equals(one.getCurrency())) {
            valuta = BigDecimal.ONE.multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP);
        } else {
            if (one.getCurrency().equals("UAH") && currency.getCurrency().equals("USD")) {
                valuta = rateDao.find(Long.valueOf(2),ExchangeRate.class).getConvertationToUAH().multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP) ;
            }
            if (one.getCurrency().equals("UAH") && currency.getCurrency().equals("EUR")) {
                valuta = rateDao.find(Long.valueOf(3),ExchangeRate.class).getConvertationToUAH().multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if (one.getCurrency().equals("USD") && currency.getCurrency().equals("UAH")) {
                valuta = (BigDecimal.ONE.divide(rateDao.find(Long.valueOf(2),ExchangeRate.class).getConvertationToUAH(),2,BigDecimal.ROUND_HALF_UP)).multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if (one.getCurrency().equals("USD") && currency.getCurrency().equals("EUR")) {
                valuta = (rateDao.find(Long.valueOf(3),ExchangeRate.class).getConvertationToUAH().divide(rateDao.find(Long.valueOf(2),ExchangeRate.class).getConvertationToUAH(),2,BigDecimal.ROUND_HALF_UP)).multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if (one.getCurrency().equals("EUR") && currency.getCurrency().equals("UAH")) {
                valuta = (BigDecimal.ONE.divide(rateDao.find(Long.valueOf(3),ExchangeRate.class).getConvertationToUAH(),2,BigDecimal.ROUND_HALF_UP)).multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
            if (one.getCurrency().equals("EUR") && currency.getCurrency().equals("USD")) {
                valuta = (rateDao.find(Long.valueOf(2),ExchangeRate.class).getConvertationToUAH().divide(rateDao.find(Long.valueOf(3),ExchangeRate.class).getConvertationToUAH(),2,BigDecimal.ROUND_HALF_UP)).multiply(money).setScale(2,BigDecimal.ROUND_HALF_UP);
            }
        }
        return valuta;
    }

    public void convertMyAccount(Account account, ExchangeRate one, ExchangeRate currency, ExchangeRateDAOImp rateDao){
        try {
            em.getTransaction().begin();
            String myCurrency = one.getCurrency();
            BigDecimal money = account.getMoney();
            if(!myCurrency.equals(currency.getCurrency())){
                account.setMoney(doConvert(currency,money,one,rateDao));
                account.setRate(currency);
            }else {
                System.out.println("Your account is already in " + currency.getCurrency());
            }
            em.getTransaction().commit();

        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        while ((r=is.read(buf))!=-1){
            bos.write(buf,0,r);
        }

        return bos.toByteArray();
    }
}
