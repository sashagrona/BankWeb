package net.bigmir.servlets;

import net.bigmir.daoImp.AccountDAOImp;
import net.bigmir.daoImp.ExchangeRateDAOImp;
import net.bigmir.daoImp.TransactionDAOImp;
import net.bigmir.daoImp.UserDAOImp;
import net.bigmir.model.Account;
import net.bigmir.model.ExchangeRate;
import net.bigmir.model.Transaction;
import net.bigmir.model.User;
import net.bigmir.utils.Convertion;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(urlPatterns = "/main")
public class MainServlet extends javax.servlet.http.HttpServlet {
    private User user;
    private HttpSession httpSession;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");
    private EntityManager em = emf.createEntityManager();
    private UserDAOImp users = new UserDAOImp(em);
    private Convertion convertion = new Convertion(em);
    private AccountDAOImp accountDAOImp = new AccountDAOImp(em);
    private ExchangeRateDAOImp rateDao = new ExchangeRateDAOImp(em);
    private TransactionDAOImp transactionDAOImp = new TransactionDAOImp(em, rateDao);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String currency = request.getParameter("currency");
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String acc = request.getParameter("account");
        String to = request.getParameter("to");
        String convertAcc = request.getParameter("convert");
        String allMoney = request.getParameter("getAll");
        PrintWriter pw = response.getWriter();
        List<Account> list = users.getAccounts(user);
        httpSession.setAttribute("list", list);
        if (allMoney != null) {
            getMoney(request, response);
        } else if (convertAcc != null) {
            convert(request, response);
        } else if (to != null) {
            send(request, response,pw);
        } else if (acc != null) {
            refill(request, response);
        } else if (currency == null) {
            if ((!name.equals("")) && (!surname.equals(""))) {
                User userOne = new User(name, surname);
                users.add(userOne);
                response.sendRedirect("/index.jsp");
            } else {
                pw.write("Can't sign in");
            }
        } else {
            User one = (User) httpSession.getAttribute("user");
            System.out.println(one);
            ExchangeRate c = convertion.getRate(currency);
            Account account = new Account(one, BigDecimal.ZERO, c);
            accountDAOImp.add(account);
            try {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/myaccounts.jsp");
                dispatcher.forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void refill(HttpServletRequest request, HttpServletResponse response) {
        String accountId = request.getParameter("account");
        String m = request.getParameter("money");
        String curr = request.getParameter("currency");
        Account account = accountDAOImp.find(Long.valueOf(accountId), Account.class);
        Double mon = Double.valueOf(m);
        BigDecimal money = BigDecimal.valueOf(mon).setScale(2, BigDecimal.ROUND_HALF_UP);
        Transaction transaction = transactionDAOImp.refill(account, money, convertion.getRate(curr));
        transactionDAOImp.add(transaction);
        try {
            response.sendRedirect("/options.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(HttpServletRequest request, HttpServletResponse response,PrintWriter pw) {
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String m = request.getParameter("money");
        String curr = request.getParameter("currency");
        ExchangeRate currency = convertion.getRate(curr);
        Account accountFrom = accountDAOImp.find(Long.valueOf(from), Account.class);
        Account accountTo = accountDAOImp.find(Long.valueOf(to), Account.class);
        Double mon = Double.valueOf(m);
        BigDecimal money = BigDecimal.valueOf(mon).setScale(2, BigDecimal.ROUND_HALF_UP);
        Transaction transaction = new Transaction(accountFrom, accountTo, money, convertion.getRate(curr));
        transactionDAOImp.doTransact(transaction);
        if(accountFrom.getMoney().compareTo(convertion.doConvert(accountFrom.getRate(),transaction.getMoney(),currency,rateDao))<0){
            pw.write("You don't have enough money on this account");
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {
            transactionDAOImp.add(transaction);
            try {
                response.sendRedirect("/options.jsp");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void convert(HttpServletRequest request, HttpServletResponse response) {
        String acc = request.getParameter("convert");
        String curr = request.getParameter("currency");
        Account account = accountDAOImp.find(Long.valueOf(acc), Account.class);
        ExchangeRate currency = convertion.getRate(curr);
        convertion.convertMyAccount(account, account.getRate(), currency, rateDao);
        try {
            response.sendRedirect("/options.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMoney(HttpServletRequest request, HttpServletResponse response) {
        BigDecimal money = BigDecimal.ZERO;
        double amount = 0;
        ExchangeRate currency = convertion.getRate("UAH");
        List<Account> accounts = users.getAccounts(user);
        for (Account a : accounts) {
            amount += money.add(convertion.doConvert(currency, a.getMoney(), a.getRate(), rateDao)).doubleValue();
        }
        httpSession.setAttribute("money", BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP));
        try {
            response.sendRedirect("/getmoney.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("passportID");
        Long passportID = Long.valueOf(id);
        UserDAOImp users = new UserDAOImp(em);
        user = users.find(passportID, User.class);
        if (user != null) {
            httpSession = request.getSession();
            httpSession.setAttribute("user", user);
            List<Account> list = users.getAccounts(user);
            httpSession.setAttribute("list", list);
            response.sendRedirect("/options.jsp");
        } else {
            response.sendRedirect("/cabinet.jsp");
        }

    }
}
