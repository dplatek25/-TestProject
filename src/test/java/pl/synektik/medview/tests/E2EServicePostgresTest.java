package pl.synektik.medview.tests;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
