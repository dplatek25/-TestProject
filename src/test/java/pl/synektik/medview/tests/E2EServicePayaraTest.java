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
}
