package vttp2022.miniproject.repositories;

public interface Queries {

    public static final String SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD =
        "select * from user where email = ? and password = sha1(?)";

    public static final String SQL_FIND_ASSETS_BY_USER_ID = 
        "select * from assets where user_id = ?";

    public static final String SQL_INSERT_STOCK_BY_USER_ID =
        "insert into assets (symbol, company_name, shares, share_price, date_traded, user_id) values (?, ?, ?, ?, ?, ?)";
}
