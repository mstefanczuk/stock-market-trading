package pl.mstefanczuk.stockmarkettrading.instrument;

import org.springframework.data.util.Pair;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

public interface InstrumentService {

    Flux<Pair<Map<Long, InstrumentPrice>, Map<Long, InstrumentPrice>>> getPrices();

    void setCurrentPrices(Map<Long, Price> prices);

    List<UserInstrument> saveAll(Iterable<UserInstrument> instruments);

    UserInstrument save(UserInstrument userInstrument);

    UserInstrument findUserInstrument(Long userId, Long instrumentId);

    Instrument findById(Long id);
}
