package net.bigmir.deprecateTest;

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
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA");
        EntityManager em = emf.createEntityManager();
        ExchangeRateDAOImp dao = new ExchangeRateDAOImp(em);
        TransactionDAOImp transactionDAOImp = new TransactionDAOImp(em, dao);
        UserDAOImp users = new UserDAOImp(em);
        AccountDAOImp accountDAOImp = new AccountDAOImp(em);
        Convertion convertion = new Convertion(em);

        convertion.fillTable();

        User user = new User("A", "Z");
        users.add(user);
        users.add(new User("B", "X"));
        users.add(new User("C", "Y"));


        Account account = new Account(user, BigDecimal.valueOf(30.0), convertion.getRate("UAH"));
        accountDAOImp.add(account);
        Account accTwo = new Account(user, BigDecimal.valueOf(100.0), convertion.getRate("EUR"));
        accountDAOImp.add(accTwo);


//        while (true) {
//            int b = 0;
//            System.out.println("1 - To refill money on the account");
//            System.out.println("2 - To send money from one to other account");
//            System.out.println("3 - To convert money in your account");
//            System.out.println("4 - To create new user");
//            System.out.println("5 - To create new account");
//            System.out.println("6 - To get All money");
//            b=sc.nextInt();
//            switch (b){
//                case 1:
//                    refill(sc,users,transactionDAOImp,convertion,accountDAOImp);
//                    break;
//                case 2:
//                    transact(users,accountDAOImp,sc,convertion,transactionDAOImp);
//                    break;
//                case 3:
//                    convertMyAcc(users,convertion,accountDAOImp,dao);
//                    break;
//                case 4:
//                    register(users);
//                    break;
//                case 5:
//                    addAcc(users,accountDAOImp,convertion);
//                    break;
//                case 6:
//                    getAllMoneyInUAH(users,convertion,dao);
//                    break;
//
//            }
//
//        }
    }
    public static void refill(Scanner sc, UserDAOImp users, TransactionDAOImp transactionDAOImp, Convertion convertion,AccountDAOImp accountDAOImp){
        System.out.println("Input your passportID:");
        Long passportID = sc.nextLong();

        User one = users.find(passportID, User.class);
        System.out.println("Your accounts: ");
        List<Account> accounts = users.getAccounts(one);
        if (accounts!=null) {
            accounts.forEach((a) -> System.out.println("Acc №: " + a.getNumber() + "; User: " + a.getUser().getName() + "; Amount of money: " + a.getMoney() + "; Currency: " + a.getRate().getCurrency()));
            System.out.println("Choose the account,which you wanna to refill");
            Long ac = sc.nextLong();
            Account accountOne = accountDAOImp.find(ac, Account.class);
            System.out.println("You chose : " + accountOne.getNumber());
            System.out.println("How much money you wanna to refill?");
            Double money = sc.nextDouble();
            System.out.println("In which currency?(UAH/USD/EUR)");
            Scanner scanner = new Scanner(System.in);
            String val = scanner.nextLine();
            System.out.println("DO YOU REALLY WANT TO REFILL Acc " + accountOne.getNumber() + " on " + money + " in " + val + "?(Y/N)");
            String confirm = scanner.nextLine();
            if ("y".equals(confirm)) {
                Transaction transaction = transactionDAOImp.refill(accountOne, BigDecimal.valueOf(money), convertion.getRate(val));
                transactionDAOImp.add(transaction);
                System.out.println("Refill succeed");
                System.out.println("Acc №: " + accountOne.getNumber() + "; User: " + accountOne.getUser().getName() + "; Amount of money: " + accountOne.getMoney() + "; Currency: " + accountOne.getRate().getCurrency());
            } else {
                System.out.println("Refill denied");
            }
        }
    }
    public static void transact(UserDAOImp users, AccountDAOImp accountDAOImp,Scanner sc,Convertion convertion, TransactionDAOImp transactionDAOImp){
        System.out.println("Input your passportID:");
        Long passportID = sc.nextLong();

        User one = users.find(passportID, User.class);
        System.out.println("Your accounts: ");
        List<Account> accounts = users.getAccounts(one);
        accounts.forEach((a) -> System.out.println("Acc №: " + a.getNumber() + "; User: " + a.getUser().getName() + "; Amount of money: " + a.getMoney() + "; Currency: " + a.getRate().getCurrency()));
        System.out.println("Choose the account from: ");
        Long from = sc.nextLong();
        Account fromAc = accountDAOImp.find(from,Account.class);
        System.out.println("To what account you wanna send money?");
        Long to = sc.nextLong();
        Account toAcc = accountDAOImp.find(to,Account.class);
        System.out.println("How much money?");
        Double money = sc.nextDouble();
        System.out.println("In which currency?(UAH/USD/EUR)");
        Scanner scanner = new Scanner(System.in);
        String val = scanner.nextLine();
        System.out.println("DO YOU REALLY WANT TO SEND " + money + " " + val + " from Acc " + fromAc.getNumber() + " to " + toAcc.getNumber()+ " ?(Y/N)");
        String confirm = scanner.nextLine();
//        if ("y".equals(confirm)) {
//            Transaction transaction = new Transaction(fromAc,toAcc,BigDecimal.valueOf(money),convertion.getRate(val));
//            transactionDAOImp.doTransact(transaction,);
//            transactionDAOImp.add(transaction);
//            System.out.println(transaction);
//        } else {
//            System.out.println("Transaction denied");
//        }

    }
    public static void convertMyAcc(UserDAOImp users,Convertion convertion,AccountDAOImp accountDAOImp,ExchangeRateDAOImp rateDao){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input your passportID:");
        Long passportID = sc.nextLong();

        User one = users.find(passportID, User.class);
        System.out.println("Your accounts: ");
        List<Account> accounts = users.getAccounts(one);
        accounts.forEach((a) -> System.out.println("Acc №: " + a.getNumber() + "; User: " + a.getUser().getName() + "; Amount of money: " + a.getMoney() + "; Currency: " + a.getRate().getCurrency()));
        System.out.println("Choose the account,which you wanna to convert");
        Long ac = sc.nextLong();
        Account accountOne = accountDAOImp.find(ac, Account.class);
        System.out.println("You chose : " + accountOne.getNumber());
        System.out.println("Input currency to convert(UAH/USD/EUR)");
        Scanner scanner = new Scanner(System.in);
        String val = scanner.nextLine();
        ExchangeRate currency = convertion.getRate(val);
        System.out.println("DO YOU REALLY WANT TO CONVERT " + accountOne.getRate().getCurrency() + " to " + currency.getCurrency()+"?");
        String confirm = scanner.nextLine();
        if ("y".equals(confirm)) {
            convertion.convertMyAccount(accountOne, accountOne.getRate(),currency,rateDao);
            System.out.println("Convertion succeed");
            System.out.println(accountOne);
        }else {
            System.out.println("Convertion denied");
        }

    }
    public static void register(UserDAOImp users){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input name:");
        String name = sc.nextLine();
        System.out.println("Input surname");
        String surname = sc.nextLine();
        User user = new User(name,surname);
        users.add(user);
        System.out.println("Registration success");

    }
    public static void addAcc(UserDAOImp users,AccountDAOImp accountDAOImp,Convertion convertion){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input your passportID:");
        Long passportID = sc.nextLong();

        User one = users.find(passportID, User.class);
        System.out.println("Input the currency of account:");
        Scanner scanner = new Scanner(System.in);
        String currency = scanner.nextLine();
        ExchangeRate c = convertion.getRate(currency);
        Account account = new Account(one,BigDecimal.ZERO,c);
        accountDAOImp.add(account);
        System.out.println("Account creating completed");
    }
    public static void getAllMoneyInUAH(UserDAOImp users, Convertion convertion,ExchangeRateDAOImp rateDao){
        Scanner sc = new Scanner(System.in);
        System.out.println("Input your passportID:");
        Long passportID = sc.nextLong();
        User user = users.find(passportID, User.class);
        BigDecimal money=BigDecimal.ZERO;
        double amount=0;
        ExchangeRate currency = convertion.getRate("UAH");
        List<Account> accounts = users.getAccounts(user);
        for (Account a:accounts) {
            amount += money.add(convertion.doConvert(currency, a.getMoney(), a.getRate(), rateDao)).doubleValue();
        }
        System.out.println("All the money you have " + BigDecimal.valueOf(amount).setScale(2,BigDecimal.ROUND_HALF_UP) + " UAH" );
    }

}
