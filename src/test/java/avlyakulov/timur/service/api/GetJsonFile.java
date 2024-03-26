package avlyakulov.timur.service.api;


public class GetJsonFile {

    public static String getJsonFileGeo() {
        return "[{\"name\":\"Kyiv\",\"local_names\":{\"no\":\"Kyiv\",\"ko\":\"키이우\",\"az\":\"Kiyev\",\"oc\":\"Kyiiv\",\"hu\":\"Kijev\",\"ug\":\"كىيېۋ\",\"yo\":\"Kiev\",\"ru\":\"Киев\",\"da\":\"Kyiv\",\"ms\":\"Kiev\",\"bo\":\"ཀིབ།\",\"my\":\"ကီးယက်မြို့\",\"pt\":\"Kiev\",\"fy\":\"Kiev\",\"cy\":\"Kyiv\",\"eo\":\"Kijivo\",\"ku\":\"Kîev\",\"nn\":\"Kiev\",\"pl\":\"Kijów\",\"ro\":\"Kiev\",\"fi\":\"Kiova\",\"mk\":\"Кијив\",\"mt\":\"Kjiv\",\"es\":\"Kiev\",\"sh\":\"Kyiv\",\"io\":\"Kyiv\",\"fa\":\"کی\u200Cیف\",\"kl\":\"Kyiv\",\"ca\":\"Kíiv\",\"mn\":\"Киев\",\"en\":\"Kyiv\",\"sl\":\"Kijev\",\"ba\":\"Киев\",\"bg\":\"Киев\",\"te\":\"క్యివ్\",\"bi\":\"Kyiv\",\"feature_name\":\"Kyiv\",\"kn\":\"ಕೀವ್\",\"zu\":\"IKiyevi\",\"ar\":\"كييف\",\"sc\":\"Kiev\",\"hr\":\"Kijiv\",\"he\":\"קייב\",\"af\":\"Kyiv\",\"an\":\"Kyiv\",\"bn\":\"কিয়েভ\",\"fr\":\"Kiev\",\"wo\":\"Kiyew\",\"ia\":\"Kiev\",\"ie\":\"Kyiv\",\"el\":\"Κίεβο\",\"se\":\"Kiova\",\"sq\":\"Kievi\",\"ht\":\"Kyèv\",\"ascii\":\"Kyiv\",\"ky\":\"Киев\",\"yi\":\"קיעוו\",\"os\":\"Киев\",\"id\":\"Kyiv\",\"sr\":\"Кијев\",\"tl\":\"Kiev\",\"th\":\"เคียฟ\",\"is\":\"Kænugarður\",\"uk\":\"Київ\",\"ur\":\"کیف\",\"bs\":\"Kijev\",\"br\":\"Kyiv\",\"zh\":\"基辅\",\"jv\":\"Kyiv\",\"de\":\"Kiew\",\"ga\":\"Cív\",\"mi\":\"Kieu\",\"sk\":\"Kyjev\",\"it\":\"Kiev\",\"lv\":\"Kijiva\",\"la\":\"Kiovia\",\"sw\":\"Kiev\",\"vi\":\"Kyiv\",\"cv\":\"Кийӳ\",\"sv\":\"Kyiv\",\"cs\":\"Kyjev\",\"mr\":\"क्यीव\",\"kk\":\"Киев\",\"hy\":\"Կիև\",\"eu\":\"Kiev\",\"gd\":\"Kyiv\",\"lt\":\"Kijevas\",\"et\":\"Kõjiv\",\"tg\":\"Киев\",\"ja\":\"キーウ\",\"qu\":\"Kiyiw\",\"ln\":\"Kyjiw\",\"hi\":\"कीव\",\"lb\":\"Kiew\",\"ml\":\"കീവ്\",\"am\":\"ኪየቭ\",\"gl\":\"Kiev\",\"kv\":\"Киев\",\"pa\":\"ਕੀਵ\",\"tr\":\"Kıyiv\",\"ta\":\"கீவ்\",\"ab\":\"Кыив\",\"ka\":\"კიევი\",\"tw\":\"Kiev\",\"fo\":\"Kyiv\",\"nl\":\"Kiev\",\"uz\":\"Kiyev\",\"cu\":\"Кꙑѥвъ\",\"gv\":\"Kyiv\",\"tt\":\"Киев\",\"vo\":\"Küyiv\",\"be\":\"Кіеў\"},\"lat\":50.4500336,\"lon\":30.5241361,\"country\":\"UA\"},{\"name\":\"Kief\",\"local_names\":{\"uk\":\"Київ\",\"zh\":\"基夫\",\"en\":\"Kief\"},\"lat\":47.85817,\"lon\":-100.512629,\"country\":\"US\",\"state\":\"North Dakota\"},{\"name\":\"Kyiv\",\"local_names\":{\"de\":\"Kyjiw\",\"uk\":\"Київ\",\"en\":\"Kyiv\",\"ru\":\"Киев\"},\"lat\":47.8671228,\"lon\":31.0179572,\"country\":\"UA\",\"state\":\"Mykolaiv Oblast\"}]";
    }

    public static String getJsonFileGeoBadRequest400() {
        return "{\"cod\":\"400\",\"message\":\"Nothing to geocode\"}\"";
    }

    public static String getJsonFileWeather() {
        return "{\n" +
                "  \"coord\" : {\n" +
                "    \"lon\" : 36.2304,\n" +
                "    \"lat\" : 49.9903\n" +
                "  },\n" +
                "  \"weather\" : [ {\n" +
                "    \"id\" : 804,\n" +
                "    \"main\" : \"Clouds\",\n" +
                "    \"description\" : \"overcast clouds\",\n" +
                "    \"icon\" : \"04d\"\n" +
                "  } ],\n" +
                "  \"base\" : \"stations\",\n" +
                "  \"main\" : {\n" +
                "    \"temp\" : 8.86,\n" +
                "    \"feels_like\" : 6.27,\n" +
                "    \"temp_min\" : 8.86,\n" +
                "    \"temp_max\" : 8.86,\n" +
                "    \"pressure\" : 1020,\n" +
                "    \"humidity\" : 50,\n" +
                "    \"sea_level\" : 1020,\n" +
                "    \"grnd_level\" : 1002\n" +
                "  },\n" +
                "  \"visibility\" : 10000,\n" +
                "  \"wind\" : {\n" +
                "    \"speed\" : 4.73,\n" +
                "    \"deg\" : 90,\n" +
                "    \"gust\" : 6.21\n" +
                "  },\n" +
                "  \"clouds\" : {\n" +
                "    \"all\" : 100\n" +
                "  },\n" +
                "  \"dt\" : 1710929193,\n" +
                "  \"sys\" : {\n" +
                "    \"country\" : \"UA\",\n" +
                "    \"sunrise\" : 1710905846,\n" +
                "    \"sunset\" : 1710949650\n" +
                "  },\n" +
                "  \"timezone\" : 7200,\n" +
                "  \"id\" : 706483,\n" +
                "  \"name\" : \"Kharkiv\",\n" +
                "  \"cod\" : 200\n" +
                "}";
    }
}
