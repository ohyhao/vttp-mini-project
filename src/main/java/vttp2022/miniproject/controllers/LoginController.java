package vttp2022.miniproject.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.AssetsService;
import vttp2022.miniproject.services.UserService;

@Controller
@RequestMapping(path = "/login")
public class LoginController {

    @Autowired
    private UserService userSvc;

    @Autowired
    private AssetsService assetsSvc;

    @PostMapping
    public ModelAndView postLogin(@RequestBody MultiValueMap<String, String> payload) {

        ModelAndView mvc = new ModelAndView();

        String username = payload.getFirst("username");
        String password = payload.getFirst("password");

        User user = new User();
        Optional<User> opt = userSvc.findUserByEmailAndPassword(username, password);
        
        if (opt.isEmpty()) {
            mvc.addObject("error", "Incorrect email/password!");
            mvc.setViewName("index");
            return mvc;
        }
        
        user = opt.get();
        System.out.printf(">>>>> user = " + user.toString());

        List<Stock> stocks = assetsSvc.getAssets(user.getUser_id());

        Float assets = 0f;
        for (int i = 0; i < stocks.size(); i++) {
            assets += stocks.get(i).getShares() * stocks.get(i).getShare_price();
        }

        mvc.addObject("name", user.getName());
        mvc.addObject("assets", assets);
        mvc.addObject("stocks", stocks);
        mvc.setViewName("welcome");
        return mvc;
    }
    
}
