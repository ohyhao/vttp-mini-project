package vttp2022.miniproject.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.repositories.AssetsRepository;

@Service
public class AssetsService {

    public static final String STOCK_SEARCH = "https://finnhub.io/api/v1/search";

    @Value("${stock.api.key}")
    private String stockKey;

    @Autowired
    private AssetsRepository assetsRepo;

    public List<Stock> getAssets(Integer userId) {
        return assetsRepo.findAssetsByUserId(userId);
    }

    public void addNewStock(Stock stock, Integer userId) {

        String url = UriComponentsBuilder.fromUriString(STOCK_SEARCH)
            .queryParam("symbol", stock.getSymbol())
            .queryParam("token", stockKey)
            .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try {
            resp = template.exchange(req, String.class);

            if (resp.getBody().isEmpty())
                throw new IllegalArgumentException("Symbol not found");
        
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        assetsRepo.addStockByUserId(stock, userId);
     
    }

    public void deleteStock(Stock stock, Integer userId) {

        try {
            Optional<Stock> opt = assetsRepo.findStockBySymbolAndUserId(stock.getSymbol(), userId);
        
            if (opt.isEmpty())
                throw new IllegalArgumentException("Symbol not in portfolio");
        
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }   

        assetsRepo.deleteStockByUserId(stock, userId);        
        
    }
    
}
