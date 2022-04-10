package jvm;

import SPI.AppService;

import java.util.ServiceLoader;

public class TestSPI {


    public static void main(String[] args) {
        ServiceLoader<AppService> services = ServiceLoader.load(AppService.class);
        services.forEach(AppService::notice);
    }


}

