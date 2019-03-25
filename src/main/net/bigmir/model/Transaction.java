package net.bigmir.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//for example
    private Long number;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "from_id")
    private Account from;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "whom_id")
    private Account whom;

    private BigDecimal money;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rate_id")
    private ExchangeRate rate;

    public Transaction(Account from, Account whom, BigDecimal money, ExchangeRate rate) {
        this.from = from;
        this.whom = whom;
        this.money = money;
        this.rate = rate;
    }

    public Transaction() {
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Account getFrom() {
        return from;
    }

    public void setFrom(Account from) {
        this.from = from;
    }

    public Account getWhom() {
        return whom;
    }

    public void setWhom(Account whom) {
        this.whom = whom;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ExchangeRate getRate() {
        return rate;
    }

    public void setRate(ExchangeRate rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "number=" + number +
                ", from=" + from +
                ", whom=" + whom +
                ", money=" + money +
                ", user=" + user +
                ", rate=" + rate +
                '}';
    }
}
