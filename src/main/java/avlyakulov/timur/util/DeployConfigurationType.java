package avlyakulov.timur.util;

public enum DeployConfigurationType {

    TEST("h2.properties"),
    PROD("postgres.properties");

    private final String propertyFileName;

    DeployConfigurationType(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    public String getPropertyFileName() {
        return propertyFileName;
    }
}
