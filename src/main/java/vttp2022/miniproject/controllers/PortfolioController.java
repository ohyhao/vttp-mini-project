package vttp2022.miniproject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = "/home/edit")
public class PortfolioController {

    @PostMapping
    public ModelAndView editPortfolio() {

        ModelAndView mvc = new ModelAndView();

        mvc.setViewName("edit");
        return mvc;
    }
    
}
