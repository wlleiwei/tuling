package SPI;

public class CAppServiceImpl implements AppService {
    @Override
    public void notice() {
        System.out.println("CAppServiceImpl execute !");
    }
}
