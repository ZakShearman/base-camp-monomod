package pink.zak.basecamp.monomod.repository;

import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pink.zak.basecamp.monomod.BaseCampMonoMod;
import pink.zak.basecamp.monomod.model.db.tables.Player;
import pink.zak.basecamp.monomod.model.db.tables.records.PlayerRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JooqRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCampMonoMod.MOD_ID + "-repo");

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "dummy_password"; // todo Make this an env var or config file var
    private static final String URI = "jdbc:postgresql://localhost:5432/sample";

    public static @NotNull DSLContext init() {
        try {
            Connection conn = DriverManager.getConnection(URI, USERNAME, PASSWORD);
            return DSL.using(conn, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
