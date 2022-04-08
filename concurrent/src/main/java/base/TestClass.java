package base;

/**
 * @ClassName TestClass.java
 * @Author weilei
 * @Description TODO
 * @CreateTime 2022年02月21日 16:55
 */
public class TestClass {
    private static volatile Integer count = 0;
    private static void add(){
        for (int i = 0; i < 10000; i++) {
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(()->{
           add();
        });
        Thread t2 = new Thread(()->{
            add();
        });
        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(count);
    }
}
