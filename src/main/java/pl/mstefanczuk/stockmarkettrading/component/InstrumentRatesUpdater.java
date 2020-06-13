package pl.mstefanczuk.stockmarkettrading.component;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.mstefanczuk.stockmarkettrading.service.InstrumentRateService;

@Component
@AllArgsConstructor
public class InstrumentRatesUpdater {

    private final InstrumentRateService instrumentRatesService;
    private final SimpMessagingTemplate template;

    @Scheduled(fixedRate = 5000)
    public void update() {
        instrumentRatesService.getCurrentRates();
    }
}
