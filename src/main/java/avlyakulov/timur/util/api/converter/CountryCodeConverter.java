package avlyakulov.timur.util.api.converter;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.neovisionaries.i18n.CountryCode;

public class CountryCodeConverter extends StdConverter<String, String> {

    @Override
    public String convert(String countryCode) {
        if (countryCode == null)
            return null;
        else
            return CountryCode.getByAlpha2Code(countryCode).getName();
    }
}