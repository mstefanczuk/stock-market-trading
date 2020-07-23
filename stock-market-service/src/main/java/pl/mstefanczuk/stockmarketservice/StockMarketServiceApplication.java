package pl.mstefanczuk.stockmarketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockMarketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockMarketServiceApplication.class, args);
    }

}
