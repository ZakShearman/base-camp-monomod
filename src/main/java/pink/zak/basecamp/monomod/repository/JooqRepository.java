package pink.zak.basecamp.monomod.repository;

import org.flywaydb.core.Flyway;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.basecamp.monomod.BaseCampMonoMod;
import pink.zak.basecamp.monomod.util.EnvUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JooqRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCampMonoMod.MOD_ID + "-repo");

    private static final String USERNAME = EnvUtil.getOrDefault("DB_USERNAME", "admin");
    private static final String PASSWORD = EnvUtil.getOrDefault("DB_PASSWORD", "dummy_password");
    private static final String URI = EnvUtil.getOrDefault("DB_URI", "jdbc:postgresql://localhost:5432/sample");

    public static @NotNull DSLContext init() {
        JooqRepository.migrate(); // Perform Flyway migrations before we connect to the DB

        try {
            Connection conn = DriverManager.getConnection(URI, USERNAME, PASSWORD);
            return DSL.using(conn, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void migrate() {
        LOGGER.info("-- Beginning Flyway DB Migrations --");
        Flyway flyway = Flyway.configure()
                .dataSource(URI, USERNAME, PASSWORD)
                .locations("classpath:db/migration")
                .failOnMissingLocations(true)
                .validateMigrationNaming(true)
                .load();

        flyway.migrate();
        LOGGER.info("-- Finished Flyway DB Migrations --");
    }
}
