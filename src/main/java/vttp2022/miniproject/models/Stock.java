package vttp2022.miniproject.models;

import java.util.Date;

public class Stock {
    private String symbol;
    private int shares;
    private Double share_price;
    private Date date_traded;

    
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public int getShares() {
        return shares;
    }
    public void setShares(int shares) {
        this.shares = shares;
    }
    public Double getShare_price() {
        return share_price;
    }
    public void setShare_price(Double share_price) {
        this.share_price = share_price;
    }
    public Date getDate_traded() {
        return date_traded;
    }
    public void setDate_traded(Date date_traded) {
        this.date_traded = date_traded;
    }
   
}
