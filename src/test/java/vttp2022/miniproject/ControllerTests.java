package vttp2022.miniproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import vttp2022.miniproject.models.Quote;
import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.AssetsService;
import vttp2022.miniproject.services.QuoteService;
import vttp2022.miniproject.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userSvc;

    @Autowired
    private AssetsService assetsSvc;

    @Autowired
    private QuoteService quoteSvc;

    @Autowired
    private JdbcTemplate template;

    @Test
    void shouldReturnLoginPage() throws Exception {
        RequestBuilder req = MockMvcRequestBuilders.get("/");

        MvcResult result = mvc.perform(req).andReturn();
        int status = result.getResponse().getStatus();

        Assertions.assertEquals(200, status);
    }

    @Test
    void shouldLogout() throws Exception {
        RequestBuilder req = MockMvcRequestBuilders.get("/logout");

        MvcResult result = mvc.perform(req).andReturn();
        int status = result.getResponse().getStatus();

        Assertions.assertEquals(302, status);
    }

    @Test
    void shouldReturnCreatePage() throws Exception {
        RequestBuilder req = MockMvcRequestBuilders.post("/create");

        MvcResult result = mvc.perform(req).andReturn();
        int status = result.getResponse().getStatus();

        Assertions.assertEquals(200, status);
    }

    @Test
    void shouldNotCreateUser() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "oyh@gmail.com");
        params.add("password", "oyh");
        params.add("name", "Yong Hao");
        
        RequestBuilder req = MockMvcRequestBuilders
            .post("/")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(params);

        MvcResult result = mvc.perform(req).andReturn();
        
        assertThrows(IllegalArgumentException.class, () -> userSvc.addNewUser(params.getFirst("email"), 
            params.getFirst("password"), params.getFirst("name")));
        
    }

    @Test
    void shouldCreateUser() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "brycelow@gmail.com");
        params.add("password", "brycelow");
        params.add("name", "Bryce");
        
        RequestBuilder req = MockMvcRequestBuilders
            .post("/")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(params);

        MvcResult result = mvc.perform(req).andReturn();
        userSvc.findUserByEmailAndPassword(params.getFirst("email"), params.getFirst("password"));
        int status = result.getResponse().getStatus();

        Assertions.assertEquals(201, status);
    }

    

    @Test 
    void shouldNotShowHomePage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "garyoh@gmail.com");
        params.add("password", "12345679");

        RequestBuilder req = MockMvcRequestBuilders
            .post("/home")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(params);
        
        MvcResult result = mvc.perform(req).andReturn();
        int status = result.getResponse().getStatus();

        assertEquals(403, status);
    }

    @Test 
    void shouldShowHomePage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("email", "garyoh@gmail.com");
        params.add("password", "12345678");

        RequestBuilder req = MockMvcRequestBuilders
            .post("/home")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .params(params);
        
        MvcResult result = mvc.perform(req).andReturn();

        Optional<User> opt = userSvc.findUserByEmailAndPassword(params.getFirst("email"), params.getFirst("password"));
        User user = opt.get();

        List<Stock> stocks = assetsSvc.getAssets(user.getUser_id());
        assertFalse(stocks.isEmpty());

        Quote quote = new Quote();
        List<Quote> quotes = new LinkedList<>();
        
        for (int i = 0; i < stocks.size(); i++) {
            
            Optional<Quote> optQuote = quoteSvc.getQuote(stocks.get(i).getSymbol());
            quote = optQuote.get();
            quotes.add(quote);
        }
        assertFalse(quotes.isEmpty());
    }

    @Test
    void shouldReturnEditPortfolioPage() throws Exception {
        RequestBuilder req = MockMvcRequestBuilders.post("/home/edit");

        MvcResult result = mvc.perform(req).andReturn();
        int status = result.getResponse().getStatus();

        Assertions.assertEquals(200, status);
    }

    @Test
    void shouldAddStock() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbol", "fb");
        params.add("shares", "1");
        params.add("share_price", "199.9");
        params.add("date_traded", "2022-05-20");

        Optional<User> opt = userSvc.findUserByEmailAndPassword("garyoh@gmail.com", "12345678");
        User user = opt.get();

        HashMap<String, Object> sessionAttr = new HashMap<String, Object>();
        sessionAttr.put("user", user);

        RequestBuilder req = MockMvcRequestBuilders
            .post("/home/edit")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttrs(sessionAttr).param("email", "garyoh@gmail.com")
            .param("add", "add")
            .params(params);
        
        MvcResult result = mvc.perform(req).andReturn();
    }

    @Test
    void shouldNotDeleteStock() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("symbol", "fb");
        params.add("shares", "1");
        params.add("share_price", "199.9");
        params.add("date_traded", "2022-05-30");

        Optional<User> opt = userSvc.findUserByEmailAndPassword("garyoh@gmail.com", "12345678");
        User user = opt.get();

        HashMap<String, Object> sessionAttr = new HashMap<String, Object>();
        sessionAttr.put("user", user);

        RequestBuilder req = MockMvcRequestBuilders
            .post("/home/edit")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .sessionAttrs(sessionAttr).param("email", "garyoh@gmail.com")
            .param("delete", "delete")
            .params(params);

        Stock stock = create(params);

        MvcResult result = mvc.perform(req).andReturn();

        assertThrows(IllegalArgumentException.class, () -> assetsSvc.deleteStock(stock, user.getUser_id()));
    }

    @Test 
    void shouldReturnToHomePage() throws Exception {

        Optional<User> opt = userSvc.findUserByEmailAndPassword("garyoh@gmail.com", "12345678");
        User user = opt.get();

        HashMap<String, Object> sessionAttr = new HashMap<String, Object>();
        sessionAttr.put("user", user);

        RequestBuilder req = MockMvcRequestBuilders
            .post("/home")
            .param("return", "return")
            .sessionAttrs(sessionAttr).param("email", "garyoh@gmail.com");
        
        MvcResult result = mvc.perform(req).andReturn();

        List<Stock> stocks = assetsSvc.getAssets(user.getUser_id());
        assertFalse(stocks.isEmpty());

        Quote quote = new Quote();
        List<Quote> quotes = new LinkedList<>();
        
        for (int i = 0; i < stocks.size(); i++) {
            
            Optional<Quote> optQuote = quoteSvc.getQuote(stocks.get(i).getSymbol());
            quote = optQuote.get();
            quotes.add(quote);
        }
        assertFalse(quotes.isEmpty());
    }

    
    @AfterEach
    public void tearDown() {
        template.update("delete from user where email = ?", "brycelow@gmail.com");
        template.update("delete from assets where symbol = ? and shares = ? and share_price = ? and date_traded = ? and user_id = ?",
            "fb", "1", "199.9", "2022-05-20", "1");
    }

    private Stock create(MultiValueMap<String, String> payload) {
        Stock stock = new Stock();

        stock.setSymbol(payload.getFirst("symbol").toUpperCase());
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