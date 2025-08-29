package pl.synektik.medview.tests;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class E2EServicePostgresTest extends AbstractCore {

    public E2EServicePostgresTest() {
        super(System.getProperty("configFile",
                System.getenv().getOrDefault("CONFIG_FILE", "")));
    }

    @Test
    void loadsConfigAndChecksPostgresService() throws Exception {
        // Pomijamy test na nie-Windows (narzędzie 'sc' jest tylko na Windows)
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("win"),
                "Test uruchamiany tylko na Windows.");

        boolean running = loadsConfigAndChecksService("MEDVIEW_SERVICE_POSTGRES");
        assertTrue(running, "Usługa MEDVIEW_SERVICE_POSTGRES powinna być uruchomiona.");
    }

    // 11.1 – test portu bazy danych
    @Test
    void checkDatabasePortIsInUse() {
        int port = Integer.parseInt(findSettings("DATABASE_PORT"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port " + port + " powinien być zajęty przez PostgreSQL.");
    }

    // 11.2 – test połączenia przez JDBC
    @Test
    void canConnectToDatabaseViaJdbc() throws Exception {
        String dbName   = findSettings("DATABASE");
        String host     = findSettings("DATABASE_HOST");
        String user     = findSettings("DATABASE_USER");
        String pass     = findSettings("DATABASE_PASSWORD");
        String schema   = findSettings("DATABASE_SCHEMA");
        int port        = Integer.parseInt(findSettings("DATABASE_PORT"));

        String url = String.format(
                "jdbc:postgresql://%s:%d/%s?currentSchema=%s",
                host, port, dbName, schema
        );

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement st = conn.createStatement()) {

            // prosty test SELECT 1
            try (ResultSet rs = st.executeQuery("SELECT 1")) {
                rs.next();
                int one = rs.getInt(1);
                assertEquals(1, one, "Zapytanie kontrolne SELECT 1 powinno zwrócić 1.");
            }

            // sprawdzamy aktywny schemat
            try (ResultSet rs2 = st.executeQuery("SELECT current_schema()")) {
                rs2.next();
                String activeSchema = rs2.getString(1);
                assertEquals(schema.toLowerCase(), activeSchema.toLowerCase(),
                        "Aktywny schemat powinien być " + schema);
            }
        }
    }
}
