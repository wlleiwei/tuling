package SPI;

public class BAppServiceImpl implements AppService{
    @Override
    public void notice() {
        System.out.println("BAppServiceImpl execute !");
    }
}
