package vttp2022.miniproject.repositories;

public interface Queries {

    public static final String SQL_SELECT_USER_BY_EMAIL_AND_PASSWORD =
        "select * from user where email = ? and password = sha1(?)";

    public static final String SQL_FIND_ASSETS_BY_USER_ID = 
        "select * from assets where user_id = ?";

    public static final String SQL_FIND_STOCKS_BY_SYMBOL_AND_USER_ID = 
        "select * from assets where symbol = ? and user_id = ?";

    public static final String SQL_INSERT_STOCK_BY_USER_ID =
        "insert into assets (symbol, shares, share_price, date_traded, user_id) values (?, ?, ?, ?, ?)";

    public static final String SQL_DELETE_STOCK_BY_USER_ID =
        "delete from assets where symbol = ? and shares = ? and share_price = ? and date_traded = ? and user_id = ?";
}
