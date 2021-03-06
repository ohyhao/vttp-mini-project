package vttp2022.miniproject.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.servlet.ModelAndView;

import vttp2022.miniproject.models.Quote;
import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.AssetsService;
import vttp2022.miniproject.services.QuoteService;
import vttp2022.miniproject.services.UserService;

@Controller
@RequestMapping
public class MainController {

    @Autowired
    private UserService userSvc;

    @Autowired
    private AssetsService assetsSvc;

    @Autowired
    private QuoteService quoteSvc;

    @PostMapping("/home")
    public ModelAndView homePage(@RequestBody MultiValueMap<String, String> payload, HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        String email = payload.getFirst("email");
        String password = payload.getFirst("password");

        Optional<User> opt = userSvc.findUserByEmailAndPassword(email, password);
        
        if (opt.isEmpty()) {
            mvc.addObject("error", "Incorrect email/password");
            mvc.setStatus(HttpStatus.FORBIDDEN);
            mvc.setViewName("index");
            return mvc;
        }

        User user = opt.get();
        sess.setAttribute("user", user);
        sess.setMaxInactiveInterval(60*60);

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
        
        mvc.addObject("quotes", quotes);
        mvc.addObject("name", user.getName());
        mvc.addObject("assets", assets);
        mvc.addObject("day_gain", day_gain);
        mvc.addObject("total_gain", total_gain);
        mvc.addObject("stocks", stocks);
        mvc.setViewName("home");
        return mvc;
    }
    
}
