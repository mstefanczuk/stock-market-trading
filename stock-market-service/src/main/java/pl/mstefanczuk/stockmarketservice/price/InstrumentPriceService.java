package pl.mstefanczuk.stockmarketservice.price;

public interface InstrumentPriceService {

    Price getCurrent(Long id);
}
