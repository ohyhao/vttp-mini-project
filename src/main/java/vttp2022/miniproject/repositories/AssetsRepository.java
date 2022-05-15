package vttp2022.miniproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.miniproject.models.Stock;

import static vttp2022.miniproject.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static vttp2022.miniproject.models.ConversionUtils.*;

@Repository
public class AssetsRepository {
    
    @Autowired
    private JdbcTemplate template;

    public List<Stock> findAssetsByUserId (Integer userId) {

        List<Stock> stocks = new LinkedList<>();
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_ASSETS_BY_USER_ID, userId);
        while (rs.next()) {
            Stock stock = populate(rs);
            stocks.add(stock);
        }
        return stocks;
    }

    public Optional<Stock> findStockBySymbolAndUserId (String symbol, Integer userId) {
        final SqlRowSet rs = template.queryForRowSet(SQL_FIND_STOCKS_BY_SYMBOL_AND_USER_ID, symbol, userId);
        if (!rs.next()) {
            return Optional.empty();
        }
        return Optional.of(populate(rs));
    }

    public boolean addStockByUserId (Stock stock, Integer userId) {

        int count = template.update(SQL_INSERT_STOCK_BY_USER_ID, 
            stock.getSymbol(), stock.getShares(), stock.getShare_price(), stock.getDate_traded(), userId);
        return count == 1;
    }

    public boolean deleteStockByUserId (Stock stock, Integer userId) {

        int count = template.update(SQL_DELETE_STOCK_BY_USER_ID, 
            stock.getSymbol(), stock.getShares(), stock.getShare_price(), stock.getDate_traded(), userId);
        return count == 1;
    }

}
