package vttp2022.miniproject.services;

import java.io.StringReader;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.repositories.AssetsRepository;

@Service
public class AssetsService {

    public static final String STOCK_SEARCH = "https://finnhub.io/api/v1/quote";

    @Value("${stock.api.key}")
    private String stockKey;

    @Autowired
    private AssetsRepository assetsRepo;

    public List<Stock> getAssets(Integer userId) {
        return assetsRepo.findAssetsByUserId(userId);
    }

    @Transactional
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
            JsonReader reader = Json.createReader(new StringReader(resp.getBody()));
            JsonObject quote = reader.readObject();

            if (quote.isNull("d"))
                throw new IllegalArgumentException("Error! Symbol not found!");
        
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

        long currentDateTime = System.currentTimeMillis();
        Date currentdate = new Date(currentDateTime);

        try {
            assetsRepo.addStockByUserId(stock, userId);
            Date date_traded = stock.getDate_traded(); 
            if (date_traded.compareTo(currentdate) > 0)
                throw new IllegalArgumentException("Error! Traded date is beyond current date!");
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
     
    }

    public void deleteStock(Stock stock, Integer userId) {

        try {
            boolean count = assetsRepo.deleteStockByUserId(stock, userId);
            if (count == false)
                throw new IllegalArgumentException("Asset details inaccurate, unable to delete from portfolio!"); 
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    
}
