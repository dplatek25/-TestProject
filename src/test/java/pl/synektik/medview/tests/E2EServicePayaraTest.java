package pl.synektik.medview.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class E2EServicePayaraTest extends AbstractCore {

    E2EServicePayaraTest() {
        super(System.getProperty("configFile",
                System.getenv().getOrDefault("CONFIG_FILE", "")));
    }

    @Test
    void loadsConfigAndChecksPayaraService() throws Exception {
        boolean result = loadsConfigAndChecksService("MEDVIEW_SERVICE_PAYARA");
        assertTrue(result, "Usługa MEDVIEW_SERVICE_PAYARA powinna być uruchomiona.");
    }

    @Test
    void checkPayaraHttpPort() {
        int port = Integer.parseInt(findSettings("MEDVIEW_SERVICE_PAYARA_PORT_HTTP"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port HTTP " + port + " powinien być zajęty przez Payarę.");
    }

    @Test
    void checkPayaraHttpsPort() {
        int port = Integer.parseInt(findSettings("MEDVIEW_SERVICE_PAYARA_PORT_HTTPS"));
        boolean busy = isPortInUse(port);
        assertTrue(busy, "Port HTTPS " + port + " powinien być zajęty przez Payarę.");
    }

}
