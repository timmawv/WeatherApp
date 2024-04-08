package avlyakulov.timur;

import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

@Slf4j
public abstract class JsonLoadTestBase {

    protected static String emptyJson = "[]";

    protected static String geoJson = """
            [
              {
                "name": "Poltava",
                "local_names": {
                  "ku": "Poltava",
                  "cs": "Poltava",
                  "fi": "Pultava",
                  "ko": "폴타바",
                  "eo": "Poltavo",
                  "nl": "Poltava",
                  "uk": "Полтава",
                  "zh": "波尔塔瓦",
                  "hr": "Poltava",
                  "es": "Poltava",
                  "fr": "Poltava",
                  "de": "Poltawa",
                  "it": "Poltava",
                  "pl": "Połtawa",
                  "he": "פולטבה",
                  "et": "Poltava",
                  "hu": "Poltava",
                  "en": "Poltava",
                  "ru": "Полтава",
                  "ja": "ポルタヴァ",
                  "sr": "Полтава"
                },
                "lat": 49.5897423,
                "lon": 34.5507948,
                "country": "UA",
                "state": "Poltava Oblast"
              }
            ]
            """;

    protected static String geosJson = """
            [
               {
                 "name": "Poltava",
                 "local_names": {
                   "ku": "Poltava",
                   "cs": "Poltava",
                   "fi": "Pultava",
                   "ko": "폴타바",
                   "eo": "Poltavo",
                   "nl": "Poltava",
                   "uk": "Полтава",
                   "zh": "波尔塔瓦",
                   "hr": "Poltava",
                   "es": "Poltava",
                   "fr": "Poltava",
                   "de": "Poltawa",
                   "it": "Poltava",
                   "pl": "Połtawa",
                   "he": "פולטבה",
                   "et": "Poltava",
                   "hu": "Poltava",
                   "en": "Poltava",
                   "ru": "Полтава",
                   "ja": "ポルタヴァ",
                   "sr": "Полтава"
                 },
                 "lat": 49.5897423,
                 "lon": 34.5507948,
                 "country": "UA",
                 "state": "Poltava Oblast"
               },
               {
                 "name": "Poltava",
                 "local_names": {
                   "ru": "Полтава",
                   "uk": "Полтава",
                   "en": "Poltava"
                 },
                 "lat": 49.5590361,
                 "lon": 37.2511612,
                 "country": "UA",
                 "state": "Kharkiv Oblast"
               },
               {
                 "name": "Poltava",
                 "local_names": {
                   "ru": "Полтава",
                   "uk": "Полтава",
                   "en": "Poltava"
                 },
                 "lat": 49.6316829,
                 "lon": 38.1134204,
                 "country": "UA",
                 "state": "Luhansk Oblast"
               }
             ]
            """;

    protected static String weatherJson = """
            {
              "coord" : {
                "lon" : 34.5407,
                "lat" : 49.5937
              },
              "weather" : [ {
                "id" : 802,
                "main" : "Clouds",
                "description" : "scattered clouds",
                "icon" : "03d"
              } ],
              "base" : "stations",
              "main" : {
                "temp" : 10.53,
                "feels_like" : 8.74,
                "temp_min" : 10.53,
                "temp_max" : 10.53,
                "pressure" : 1020,
                "humidity" : 42,
                "sea_level" : 1020,
                "grnd_level" : 1005
              },
              "visibility" : 10000,
              "wind" : {
                "speed" : 2.94,
                "deg" : 341,
                "gust" : 3.15
              },
              "clouds" : {
                "all" : 28
              },
              "dt" : 1712310500,
              "sys" : {
                "country" : "UA",
                "sunrise" : 1712286583,
                "sunset" : 1712333929
              },
              "timezone" : 10800,
              "id" : 696643,
              "name" : "Poltava",
              "cod" : 200
            }
            """;

    @BeforeAll
    static void setUp() {
        HibernateSingletonUtil.initEnvironments();
    }
}