package jvm;

public class ClassLoad {
    static {
        System.out.println("load ClassLoad");
    }

    public static void main(String[] args) {
        A a = new A();
        Class c = a.getClass();
        System.out.println(c.getConstructors());
    }


}

class A {
    static {
        System.out.println("load ClassA");
    }

    public A() {
        System.out.println("init ClassA");
    }
}