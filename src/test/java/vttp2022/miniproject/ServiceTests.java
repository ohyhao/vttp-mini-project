package vttp2022.miniproject;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import vttp2022.miniproject.models.Quote;
import vttp2022.miniproject.models.Stock;
import vttp2022.miniproject.models.User;
import vttp2022.miniproject.services.AssetsService;
import vttp2022.miniproject.services.QuoteService;
import vttp2022.miniproject.services.UserService;

@SpringBootTest
class ServiceTests {

	@Autowired
	private UserService userSvc;

	@Autowired
	private QuoteService quoteSvc;

	@Autowired
	private AssetsService assetsSvc;

	@Test
	void shouldFindGaryOh() {
		Optional<User> opt = userSvc.findUserByEmailAndPassword("garyoh@gmail.com", "12345678");
		assertTrue(opt.isPresent());
	}

	@Test
	void shouldNotFindFred() {
		Optional<User> opt = userSvc.findUserByEmailAndPassword("fred@gmail.com", "82374837");
		assertTrue(opt.isEmpty());
	}

	@Test
	void shouldFindQuote() {
		Optional<Quote> opt = quoteSvc.getQuote("TSLA");
		assertTrue(opt.isPresent());
	}

	@Test
	void shouldNotFindQuote() {
		Optional<Quote> opt = quoteSvc.getQuote("TESLA");
		assertTrue(opt.isEmpty());
	}

	@Test
	void shouldNotReturnListofStocks() {
		List<Stock> stocks = assetsSvc.getAssets(2);
		assertTrue(stocks.isEmpty());
	}

	@Test
	void shouldNotBeAbleToFindSymbol() {
		Stock stock = new Stock();
		stock.setSymbol("TESLA");
		assertThrows(IllegalArgumentException.class,
			() -> assetsSvc.addNewStock(stock, 1));
	}

	@Test
	void shouldNotBeAbleToAddStock() {
		Stock stock = new Stock();
		stock.setSymbol("FB");
		stock.setShares(10);
		stock.setShare_price(199.9);
		
		String date = "2022-06-21";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
			stock.setDate_traded(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertThrows(IllegalArgumentException.class,
				() -> assetsSvc.addNewStock(stock, 1));
	}

	@Test
	void shouldNotBeAbleToDeleteStock() {
		Stock stock = new Stock();
		stock.setSymbol("FB");
		stock.setShares(10);
		stock.setShare_price(199.9);
		
		String date = "2022-06-21";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
			stock.setDate_traded(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		assertThrows(IllegalArgumentException.class,
			() -> assetsSvc.deleteStock(stock, 1));
	}

	@Test
	void shouldNotBeAbleToAddUser() {
		assertThrows(IllegalArgumentException.class,
			() -> userSvc.addNewUser("garyoh@gmail.com", "garyoh", "Gary Oh"));
	}

}
