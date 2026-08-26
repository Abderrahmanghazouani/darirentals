package ma.zyn.app.ws.facade.open;

import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService;
import ma.zyn.app.service.facade.admin.currency.ExchangeRateAdminService;
import ma.zyn.app.ws.converter.currency.CurrencyConverter;
import ma.zyn.app.ws.converter.currency.ExchangeRateConverter;
import ma.zyn.app.ws.dto.currency.CurrencyDto;
import ma.zyn.app.ws.dto.currency.ExchangeRateDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Lecture publique (sans authentification) des devises et taux de change, pour le
 * selecteur de devise de la page /reserver. Voir NOTES-devises.md.
 *
 * Attention : CurrencyConverter est un bean partage (singleton) dont les flags de liste
 * par defaut incluent "enterprises"/"collaborators" - on force initList(false) pour ne
 * jamais exposer ces listes sur un endpoint public.
 */
@RestController
@RequestMapping("/api/open/currency/")
public class CurrencyRestOpen {

    private final CurrencyAdminService currencyService;
    private final ExchangeRateAdminService exchangeRateService;
    private final CurrencyConverter currencyConverter;
    private final ExchangeRateConverter exchangeRateConverter;

    public CurrencyRestOpen(CurrencyAdminService currencyService,
                             ExchangeRateAdminService exchangeRateService,
                             CurrencyConverter currencyConverter,
                             ExchangeRateConverter exchangeRateConverter) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
        this.currencyConverter = currencyConverter;
        this.exchangeRateConverter = exchangeRateConverter;
    }

    @GetMapping("currencies")
    public ResponseEntity<List<CurrencyDto>> findCurrencies() {
        List<Currency> list = currencyService.findAll();
        currencyConverter.initList(false);
        List<CurrencyDto> dtos = currencyConverter.toDto(list);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("exchange-rates")
    public ResponseEntity<List<ExchangeRateDto>> findExchangeRates() {
        List<ExchangeRate> list = exchangeRateService.findAll();
        exchangeRateConverter.initObject(true);
        List<ExchangeRateDto> dtos = exchangeRateConverter.toDto(list);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }
}
