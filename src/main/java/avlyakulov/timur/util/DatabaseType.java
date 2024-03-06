package avlyakulov.timur.util;

public enum DatabaseType {
    H2("h2.properties"),
    POSTGRES("postgres.properties");

    private final String propertyFileName;

    DatabaseType(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }
}