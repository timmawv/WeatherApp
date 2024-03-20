package avlyakulov.timur.util.api;

public class SetDirectionWind {

    private static final String[] directions = {"North", "North-East", "East-North", "East", "East-South",
            "South-East", "South", "South-West", "West-South", "West", "West-North", "North-West"};

    public static String setDirectionWindByDegree(int degree) {
        if (degree >= 345 && degree <= 360) {
            return directions[0];
        } else {
            int segmentSize = 360 / directions.length; //12
            int segmentIndex = (degree + segmentSize / 2) / segmentSize;
            return directions[segmentIndex];
        }
    }
}