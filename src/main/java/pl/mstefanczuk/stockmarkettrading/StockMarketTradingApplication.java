package pl.mstefanczuk.stockmarkettrading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StockMarketTradingApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMarketTradingApplication.class, args);
    }

}
