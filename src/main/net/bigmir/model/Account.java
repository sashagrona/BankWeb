package net.bigmir.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;

    @ManyToOne(cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private BigDecimal money;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rate_id")
    private ExchangeRate rate;

    public Account(User user, BigDecimal money, ExchangeRate rate) {
        this.user = user;
        this.money = money;
        this.rate = rate;
    }

    public Account(User user) {
        this.user = user;
    }

    public Account() {
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public ExchangeRate getRate() {
        return rate;
    }

    public void setRate(ExchangeRate rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "number='" + number + '\'' +
                ", user=" + user +
                ", money=" + money +
                ", rate=" + rate +
                '}';
    }
}
