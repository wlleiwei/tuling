package Java8Test;

@FunctionalInterface
public interface FunctionDemo<T, R> {
    boolean equals(T t, R r);
}
