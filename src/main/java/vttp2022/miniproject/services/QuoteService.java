package vttp2022.miniproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import vttp2022.miniproject.models.Quote;

@Service
public class QuoteService {
    
    public static final String STOCK_SEARCH = "https://finnhub.io/api/v1/quote";

    @Value("${stock.api.key}")
    private String stockKey;

    public Optional<Quote> getQuote(String symbol) {

        String url = UriComponentsBuilder.fromUriString(STOCK_SEARCH)
            .queryParam("symbol", symbol)
            .queryParam("token", stockKey)
            .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> resp = null;

        try {
            resp = template.exchange(req, String.class);
            Quote q = Quote.create(resp.getBody());
            return Optional.of(q);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return Optional.empty();
    }

}
