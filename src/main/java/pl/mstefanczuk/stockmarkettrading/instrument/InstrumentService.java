package pl.mstefanczuk.stockmarkettrading.instrument;

public interface InstrumentService {

    String getCurrentRates();

    Iterable<UserInstrument> saveAll(Iterable<UserInstrument> instruments);
}
