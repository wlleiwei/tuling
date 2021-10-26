package lambda;

/**
 * @ClassName DemoInterfaceImpl.java
 * @Author weilei
 * @Description TODO
 * @CreateTime 2021年10月25日 18:41
 */
public class DemoInterfaceImpl implements DemoInterface {
    @Override
    public void test() {
        System.out.println("传递实现类");
    }
}
