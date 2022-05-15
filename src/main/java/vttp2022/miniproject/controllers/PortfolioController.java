package vttp2022.miniproject.controllers;

import java.text.SimpleDateFormat;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.AssetsService;

@Controller
public class PortfolioController {

    @Autowired
    private AssetsService assetsSvc;

    @RequestMapping(path = "/home/edit")
    @PostMapping
    public ModelAndView editStock() {

        ModelAndView mvc = new ModelAndView();
        
        mvc.setViewName("edit");
        
        return mvc;
    }

    @RequestMapping(value = "/home/edit", method = RequestMethod.POST, params = "add")
    public ModelAndView addStock(@RequestBody MultiValueMap<String, String> payload, HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        // Optional<Stock> optStock = create(payload);
        // if (optStock.isEmpty()) {
        //     mvc.setStatus(HttpStatus.BAD_REQUEST);
        //     mvc.addObject("error", "Please key in !");
        //     mvc.setViewName("edit");
        //     return mvc;
        // }

        // Stock stock = optStock.get();

        Stock stock = create(payload);

        User user = (User)sess.getAttribute("user");
        assetsSvc.addNewStock(stock, user.getUser_id());
        mvc.setViewName("edit");
        
        String message = "%s shares of %s @ %s has been added to your porfolio".
            formatted(stock.getShares(), stock.getSymbol(), stock.getShare_price());
        
        mvc.addObject("message", message);
        return mvc;
    }

    @RequestMapping(value = "/home/edit", method = RequestMethod.POST, params = "delete")
    public ModelAndView deleteStock(@RequestBody MultiValueMap<String, String> payload, HttpSession sess) {

        ModelAndView mvc = new ModelAndView();

        // Optional<Stock> optStock = create(payload);
        // if (optStock.isEmpty()) {
        //     mvc.addObject("delete_error", "Symbol not in portfolio!");
        //     mvc.setViewName("edit");
        //     return mvc;
        // }

        Stock stock = create(payload);

        User user = (User)sess.getAttribute("user");
        assetsSvc.deleteStock(stock, user.getUser_id());
        mvc.setViewName("edit");

        String message = "%s shares of %s @ %s has been deleted from your porfolio".
            formatted(stock.getShares(), stock.getSymbol(), stock.getShare_price());

        mvc.addObject("message", message);
        return mvc;
    }
    
    // private Optional<Stock> create(MultiValueMap<String, String> payload) {
    //     Stock stock = new Stock();

    //     stock.setSymbol(payload.getFirst("symbol"));
    //     String shares = payload.getFirst("shares");
    //     stock.setShares(Integer.parseInt(shares));
    //     String share_price = payload.getFirst("share_price");
    //     stock.setShare_price(Double.parseDouble(share_price));
    //     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //     try {
    //         stock.setDate_traded(sdf.parse(payload.getFirst("date_traded")));
    //     } catch (Exception ex) {
    //         ex.printStackTrace();
    //         return Optional.empty();
    //     }
    //     return Optional.of(stock);
    // }

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
