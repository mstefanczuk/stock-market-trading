package pl.mstefanczuk.stockmarkettrading.instrument;

import java.util.Map;

public interface InstrumentService {

    Map<Long, Price> getCurrentPrices();

    void setCurrentPrices(Map<Long, Price> prices);

    Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments);

    UserInstrument save(UserInstrument userInstrument);

    UserInstrument findUserInstrument(Long userId, Long instrumentId);

    Instrument findById(Long id);
}
