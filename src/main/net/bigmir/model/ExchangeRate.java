package net.bigmir.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Currency")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currency;
    private BigDecimal convertationToUAH;

    public ExchangeRate(String currency, BigDecimal convertationToUAH) {
        this.currency = currency;
        this.convertationToUAH = convertationToUAH;
    }

    public ExchangeRate(String currency) {
        this.currency = currency;
    }

    public ExchangeRate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getConvertationToUAH() {
        return convertationToUAH;
    }

    public void setConvertationToUAH(BigDecimal convertationToUAH) {
        this.convertationToUAH = convertationToUAH;
    }

    @Override
    public String toString() {
        return currency;
    }
}
