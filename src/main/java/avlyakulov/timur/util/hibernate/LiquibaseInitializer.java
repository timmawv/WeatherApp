package avlyakulov.timur.util.hibernate;

import liquibase.command.CommandScope;
import liquibase.exception.CommandExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LiquibaseInitializer {

    private static final String CHANGELOG_FILE = "db/changelog/main-changelog.xml";

    private final String url = System.getenv("DB_URL");
    private final String username = System.getenv("DB_USERNAME");
    private final String password = System.getenv("DB_PASSWORD");

    private final String driver = "org.postgresql.Driver";

    public void addMigrationsToDB() {
        try {
            new CommandScope("update")
                    .addArgumentValue("url", url)
                    .addArgumentValue("username", username)
                    .addArgumentValue("password", password)
                    .addArgumentValue("driver", driver)
                    .addArgumentValue("changeLogFile", CHANGELOG_FILE)
                    .execute();
        } catch (CommandExecutionException e) {
            log.error("Error during liquibase migrations");
            throw new RuntimeException(e);
        }

    }
}
