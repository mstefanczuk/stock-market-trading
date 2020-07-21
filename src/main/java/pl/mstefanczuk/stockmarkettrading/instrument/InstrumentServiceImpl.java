package pl.mstefanczuk.stockmarkettrading.instrument;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class InstrumentServiceImpl implements InstrumentService {

    private final UserInstrumentRepository userInstrumentRepository;

    @Override
    public String getCurrentRates() {
        Random rand = new Random();
        int n = rand.nextInt(50);
        return String.valueOf(n);
    }

    @Override
    public Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments) {
        return userInstrumentRepository.saveAll(instruments);
    }
}
