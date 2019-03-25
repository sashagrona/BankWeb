package net.bigmir.fixer;

import java.math.BigDecimal;

public class Rates {
    private BigDecimal USD;
    private BigDecimal UAH;

    public Rates() {
    }

    public BigDecimal getUSD() {
        return USD;
    }

    public void setUSD(BigDecimal USD) {
        this.USD = USD;
    }

    public BigDecimal getUAH() {
        return UAH;
    }

    public void setUAH(BigDecimal UAH) {
        this.UAH = UAH;
    }
}
