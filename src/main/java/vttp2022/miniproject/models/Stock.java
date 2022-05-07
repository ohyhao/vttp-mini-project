package vttp2022.miniproject.models;

import java.util.Date;

public class Stock {
    private String symbol;
    private String company_name;
    private int shares;
    private Float share_price;
    private Date date_traded;
    
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getCompany_name() {
        return company_name;
    }
    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
    public int getShares() {
        return shares;
    }
    public void setShares(int shares) {
        this.shares = shares;
    }
    public Float getShare_price() {
        return share_price;
    }
    public void setShare_price(Float share_price) {
        this.share_price = share_price;
    }
    public Date getDate_traded() {
        return date_traded;
    }
    public void setDate_traded(Date date_traded) {
        this.date_traded = date_traded;
    }

    
    
}
