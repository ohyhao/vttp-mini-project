package vttp2022.miniproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.miniproject.models.Stock;

import static vttp2022.miniproject.repositories.Queries.*;

import java.util.LinkedList;
import java.util.List;

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
}
