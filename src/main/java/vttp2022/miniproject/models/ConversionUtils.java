package vttp2022.miniproject.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class ConversionUtils {
    
    public static User convert(SqlRowSet rs) {
        User user = new User();
        user.setUser_id(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setName(rs.getString("name"));
        return user;
    }

    public static Stock populate(SqlRowSet rs) {
        Stock stock = new Stock();
        stock.setSymbol(rs.getString("symbol"));
        stock.setCompany_name(rs.getString("company_name"));
        stock.setShares(rs.getInt("shares"));
        stock.setShare_price(rs.getFloat("share_price"));
        stock.setDate_traded(rs.getDate("date_traded"));
        return stock;
    }
}
