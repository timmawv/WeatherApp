package avlyakulov.timur;

import avlyakulov.timur.util.hibernate.HibernateSingletonUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Slf4j
public abstract class JsonLoadTestBase {

    private static String geosJson;

    private static String geoJson;

    private static String emptyJson = "[]";

    @BeforeAll
    static void setUp() {
        HibernateSingletonUtil.initEnvironments();
        geoJson = fillJsonFile("geo.json");
        geosJson = fillJsonFile("geos.json");
    }

    private static String fillJsonFile(String fileNameJson) {
        InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileNameJson);
        assert resourceAsStream != null;
        String fileJson = "";
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            while ((line = reader.readLine()) != null) {
                fileJson = fileJson.concat(line);
            }
        } catch (IOException e) {
            log.error("File wasn't found");
            throw new RuntimeException();
        }
        return fileJson;
    }

    public String getGeosJson() {
        return geosJson;
    }

    public String getGeoJson() {
        return geoJson;
    }

    public String getEmptyJson() {
        return emptyJson;
    }
}