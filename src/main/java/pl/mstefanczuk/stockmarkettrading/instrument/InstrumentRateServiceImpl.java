package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class InstrumentRateServiceImpl implements InstrumentRateService {

    @Override
    public String getCurrentRates() {
        Random rand = new Random();
        int n = rand.nextInt(50);
        return String.valueOf(n);
    }
}
