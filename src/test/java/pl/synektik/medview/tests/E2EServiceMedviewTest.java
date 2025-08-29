package pl.synektik.medview.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class E2EServiceMedviewTest extends AbstractCore {

    public E2EServiceMedviewTest() {
        super(System.getProperty("configFile",
                System.getenv().getOrDefault("CONFIG_FILE", "")));
    }

    @Test
    void checkPayaraHttpPort() {
        int port = Integer.parseInt(findSettings("MEDVIEW_SERVICE_PAYARA_PORT_HTTP"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port HTTP " + port + " powinien być zajęty przez usługę Payara.");
    }

    @Test
    void checkPayaraHttpsPort() {
        int port = Integer.parseInt(findSettings("MEDVIEW_SERVICE_PAYARA_PORT_HTTPS"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port HTTPS " + port + " powinien być zajęty przez usługę Payara.");
    }
}
