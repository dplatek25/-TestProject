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

    @Test
    void checkMeddreamHttpPort() {
        int port = Integer.parseInt(findSettings("MEDVIEW_SERVICE_MEDDREAM_PORT_HTTP"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port HTTP " + port + " powinien być zajęty przez usługę MedDream.");
    }

    @Test
    void checkMeddreamHttpsPort() {
        int port = Integer.parseInt(findSettings("MEDVIEW_SERVICE_MEDDREAM_PORT_HTTPS"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port HTTPS " + port + " powinien być zajęty przez usługę MedDream.");
    }

}
