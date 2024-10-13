package bankApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyData {
    private int currencyCodeA;
    private int currencyCodeB;
    private int date;
    private double rateBuy;
    private double rateSell;
    private double rateCross;


    public CurrencyData() {
    }

    public CurrencyData(int currencyCodeA, int currencyCodeB, int date, double rateBuy, double rateSell, double rateCross) {
        this.currencyCodeA = currencyCodeA;
        this.currencyCodeB = currencyCodeB;
        this.date = date;
        this.rateBuy = rateBuy;
        this.rateSell = rateSell;
        this.rateCross = rateCross;
    }

    public int getCurrencyCodeA() {
        return currencyCodeA;
    }

    public int getCurrencyCodeB() {
        return currencyCodeB;
    }

    public int getDate() {
        return date;
    }

    public double getRateBuy() {
        return rateBuy;
    }

    public double getRateSell() {
        return rateSell;
    }

    public double getRateCross() {
        return rateCross;
    }
}
