package pl.synektik.medview.tests;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class E2EServiceMeddreamTest extends AbstractCore {

    public E2EServiceMeddreamTest() {
        super(System.getProperty("configFile",
                System.getenv().getOrDefault("CONFIG_FILE", "")));
    }

    @Test
    void loadsConfigAndChecksMeddreamService() throws Exception {
        // Pomijamy test, jeśli nie Windows
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("win"),
                "Test uruchamiany tylko na Windows.");

        boolean running = loadsConfigAndChecksService("MEDVIEW_SERVICE_MEDDREAM");
        assertTrue(running, "Usługa MEDVIEW_SERVICE_MEDDREAM powinna być uruchomiona.");
    }
}
