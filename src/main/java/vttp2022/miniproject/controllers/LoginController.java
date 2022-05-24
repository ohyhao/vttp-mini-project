package vttp2022.miniproject.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.UserService;

@Controller
@RequestMapping
public class LoginController {

    @Autowired
    private UserService userSvc;

    @GetMapping(path = "/")
    public ModelAndView loginPage() {

        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("index");
        return mvc;
    }

    @GetMapping(path = "/logout")
    public String logout(HttpSession sess) {
        sess.invalidate();
        return "redirect:/";
    }

    @PostMapping(path = "/create")
    public ModelAndView createPage() {

        ModelAndView mvc = new ModelAndView();
        mvc.setViewName("create");
        return mvc;
    }

    @PostMapping(path = "/")
    public ModelAndView createUser(@RequestBody MultiValueMap<String, String> form) {

        ModelAndView mvc = new ModelAndView();
        String email = form.getFirst("email");
        String password = form.getFirst("password");
        String name = form.getFirst("name");
        
        try {
            userSvc.addNewUser(email, password, name);
        } catch (Exception ex) {
            mvc.addObject("error", ex.getMessage());
            mvc.setViewName("create");
            return mvc;
        }

        Optional<User> opt = userSvc.findUserByEmailAndPassword(email, password);

        if (opt.isPresent())
            mvc.addObject("created", "Account created, please log in");
            mvc.setStatus(HttpStatus.CREATED);
            mvc.setViewName("index");
            return mvc;
    }
}

    
