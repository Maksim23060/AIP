package test.com.gateway.config;

import main.com.gateway.config.ConfigLoader;
import org.testng.annotations.Test;
import main.com.gateway.model.ServiceInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.testng.Assert.*;

public class ConfigLoaderTest {

    @Test
    public void testLoadServices_SkipsInvalidLines() throws IOException {
        Path tempFile = Files.createTempFile("services", ".txt");
        String content = "good1,http://good1,10,true\n" +
                "badline\n" +
                "good2,http://good2,5,false\n" +
                "invalid-url,http://bad,2,true";
        Files.writeString(tempFile, content);

        List<ServiceInstance> services = ConfigLoader.loadServices(tempFile.toString());
        assertEquals(services.size(), 2);
        assertEquals(services.get(0).getId(), "good1");
        assertEquals(services.get(1).getId(), "good2");

        Files.delete(tempFile);
    }
}