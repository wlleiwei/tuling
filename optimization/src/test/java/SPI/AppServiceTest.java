package SPI;

import junit.framework.TestCase;

import java.util.ServiceLoader;

public class AppServiceTest extends TestCase {

    public void testNotice() {
        ServiceLoader<AppService> services = ServiceLoader.load(AppService.class);
        services.forEach(AppService::notice);
    }
}