package vttp2022.miniproject.controllers;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.miniproject.models.Quote;
import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.AssetsService;
import vttp2022.miniproject.services.QuoteService;

@Controller
public class PortfolioController {

    @Autowired
    private AssetsService assetsSvc;

    @Autowired
    private QuoteService quoteSvc;


    @RequestMapping(path = "/home/edit")
    @PostMapping
    public ModelAndView editStock() {

        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("edit");
        return mvc;
    }

    @RequestMapping(value = "/home", method = RequestMethod.POST, params = "return")
    public ModelAndView returnToHomepage(HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        User user = (User)sess.getAttribute("user");
        List<Stock> stocks = assetsSvc.getAssets(user.getUser_id());

        Double assets = 0.0;
        Double day_gain = 0.0;
        Double total_gain = 0.0;
        Double cost = 0.0;
        Quote quote = new Quote();
        List<Quote> quotes = new LinkedList<>();
        
        for (int i = 0; i < stocks.size(); i++) {
            
            Optional<Quote> optQuote = quoteSvc.getQuote(stocks.get(i).getSymbol());
            if (optQuote.isEmpty()) {
                mvc.addObject("not found", "stock not found");
                mvc.setViewName("home");
                return mvc;
            }
            quote = optQuote.get();
            quotes.add(quote);  
        }

        for (int i = 0; i < stocks.size(); i++) {
            assets += stocks.get(i).getShares() * (quotes.get(i).getCurrent_price());
            day_gain +=  stocks.get(i).getShares() * (quotes.get(i).getChange());
            cost += (stocks.get(i).getShares() * stocks.get(i).getShare_price());
            quotes.get(i).setTotal_change(stocks.get(i).getShares() * (quotes.get(i).getCurrent_price()- stocks.get(i).getShare_price()));
            quotes.get(i).setTotal_change_percentage((quotes.get(i).getCurrent_price()- stocks.get(i).getShare_price())
                /stocks.get(i).getShare_price() * 100);
            
            
        }
        
        total_gain = assets - cost;
        
        System.out.printf(">>>>> assets = %s\n".formatted(assets));
        System.out.printf(">>>>> day_gain = %s\n".formatted(day_gain));
        System.out.printf(">>>>> total_gain = %s\n".formatted(total_gain));

        mvc.addObject("quotes", quotes);
        mvc.addObject("name", user.getName());
        mvc.addObject("assets", assets);
        mvc.addObject("day_gain", day_gain);
        mvc.addObject("total_gain", total_gain);
        mvc.addObject("stocks", stocks);
        mvc.setViewName("home");
        return mvc;
    }

    @RequestMapping(value = "/home/edit", method = RequestMethod.POST, params = "add")
    public ModelAndView addStock(@RequestBody MultiValueMap<String, String> payload, HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        Stock stock = create(payload);

        System.out.printf("symbol = " + stock.getSymbol());
        System.out.printf("shares = " + stock.getShares());
        System.out.printf("share_price = " + stock.getShare_price());
        System.out.printf("date_payload = " + payload.getFirst("date_traded"));
        System.out.printf("date = " + stock.getDate_traded());

        User user = (User)sess.getAttribute("user");

        try {
            assetsSvc.addNewStock(stock, user.getUser_id());
        } catch (Exception ex) {
            mvc.addObject("error", ex.getMessage());
            mvc.setViewName("edit");
            return mvc;
        }
        
        mvc.setViewName("edit");
        
        String message = "%s shares of %s @ $%s has been added to your porfolio".
            formatted(stock.getShares(), stock.getSymbol(), stock.getShare_price(), payload.getFirst("date"));
        
        mvc.addObject("message", message);
        return mvc;
    }

    @RequestMapping(value = "/home/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView deleteStock(@RequestBody MultiValueMap<String, String> payload, HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        Stock stock = create(payload);

        User user = (User)sess.getAttribute("user");

        try {
            assetsSvc.deleteStock(stock, user.getUser_id());
        } catch (Exception ex) {
            mvc.addObject("error", ex.getMessage());
            mvc.setViewName("edit");
            return mvc;
        }

        mvc.setViewName("edit");

        String message = "%s shares of %s @ $%s has been deleted from your porfolio".
            formatted(stock.getShares(), stock.getSymbol(), stock.getShare_price());

        mvc.addObject("message", message);

        return mvc;
    }

    private Stock create(MultiValueMap<String, String> payload) {
        Stock stock = new Stock();

        stock.setSymbol(payload.getFirst("symbol"));
        String shares = payload.getFirst("shares");
        stock.setShares(Integer.parseInt(shares));
        String share_price = payload.getFirst("share_price");
        stock.setShare_price(Double.parseDouble(share_price));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            stock.setDate_traded(sdf.parse(payload.getFirst("date_traded")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stock;
    }
}
