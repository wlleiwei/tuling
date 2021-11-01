package stream;

import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @ClassName StreamTest.java
 * @Author weilei
 * @Description TODO
 * @CreateTime 2021年10月26日 11:12
 */
public class StreamTest {

    private static List<Person> personList = new ArrayList<Person>();

    static {
        personList.add(new Person("Tom", 8900, 21, "male", "New York"));
        personList.add(new Person("Jack", 7000, 22, "male", "Washington"));
        personList.add(new Person("Lily", 7800, 23, "female", "Washington"));
        personList.add(new Person("Anni", 8200, 24, "female", "New York"));
        personList.add(new Person("Owen", 9500, 25, "male", "New York"));
        personList.add(new Person("Alisa", 7900, 24, "female", "New York"));
    }

    @Test
    public void test1() {
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("22");
        list.add("333");
        list.add("334");

        System.out.println("正常迭代输出");
        for (String s : list) {
            if (s.startsWith("33") && s.length() > 2)
                System.out.println(s);
        }

        System.out.println("转化成流输出");
        list.stream()
                .filter(s -> s.startsWith("33"))
                .filter(s -> s.length() > 2)
                .forEach(System.out::println);
    }

    @Test
    public void test2() {
        List<Integer> list = Arrays.asList(7, 6, 9, 3, 8, 2, 1);
        Predicate<Integer> predicate = x -> x > 6;
        //遍历输出符合条件的元素
        list.stream().filter(predicate).forEach(System.out::println);
        //匹配符合条件的第一个元素
        Optional<Integer> first = list.stream().filter(predicate).findFirst();
        //匹配任意一个符合条件的元素
        Optional<Integer> anyFind = list.stream().filter(predicate).findAny();
        Optional<Integer> anyFindP = list.parallelStream().filter(predicate).findAny();
        //stream中是否存在满足条件的元素
        boolean anyMatch = list.stream().anyMatch(predicate);
        boolean allMatch = list.stream().allMatch(predicate);
        System.out.println("first:" + first.get());
        System.out.println("anyFind:" + anyFind.get());
        System.out.println("anyFindP:" + anyFindP.get());
        System.out.println("anyMatch:" + anyMatch);
        System.out.println("allMatch:" + allMatch);
    }

    @Test
    public void test3() {
        //筛选员工中工资高于8000的人，并形成新的集合
        List<String> names = personList.stream()
                .filter(x -> x.getSalary() > 8000)
                .map(Person::getName)
                .collect(Collectors.toList());
        System.out.println("工资高于8000的员工姓名：" + names);
    }

    @Test
    public void test4() {
        //获取String集合中最长的元素
        List<String> list = Arrays.asList("adnm", "admmt", "pot", "xbangd", "weoujgsd");
        //从stream中的每个元素提取出来可以比较的元素，可以Function进行转化
        Optional<String> max = list.stream().max(Comparator.comparingInt(String::length));
        System.out.println("集合中最大的元素：" + max.get());
    }

    @Test
    public void test5() {
        //获取集合中最大的值
        List<Integer> list = Arrays.asList(7, 6, 9, 4, 11, 6);
        Optional<Integer> max1 = list.stream().max(Integer::compare);
        Optional<Integer> max2 = list.stream().max(Comparator.reverseOrder());
        System.out.println("max1:" + max1);
        System.out.println("max2:" + max2);
    }

    @Test
    public void test6() {
        //获取员工工资最高的人
        Optional<String> name = personList.stream()
                .max(Comparator.comparingInt(Person::getSalary))
                .map(Person::getName);
        System.out.println("员工工资最高的人：" + name.get());
    }

    @Test
    public void test7() {
        //计算Integer集合中大于6的元素的个数
        List<Integer> list = Arrays.asList(7, 6, 4, 8, 2, 11, 9);
        long count = list.stream().filter(x -> x > 6).count();
        System.out.println("集合中大于6的元素个数：" + count);
    }

    @Test
    public void test8() {
        //改成大写
        String[] strArr = {"abcd", "bcdd", "defde", "fTr"};
        List<String> collect = Arrays.stream(strArr)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
        System.out.println("大写：" + collect);
        //数组元素加3
        List<Integer> intList = Arrays.asList(1, 3, 5, 7, 9, 11);
        List<Integer> collect1 = intList.stream().map(x -> x + 3).collect(Collectors.toList());
        System.out.println("元素加3：" + collect1);
    }

    @Test
    //map用来对stream中的元素进行映射操作
    public void test9() {
        //员工工资加10000
        List<Person> persons = personList;
        //不改变原来的stream
        List<Person> collect1 = persons.stream()
                .map(person -> {
                    Person newPerson = new Person(person.getName(),
                            person.getSalary() + 10000,
                            person.getAge(),
                            person.getSex(),
                            person.getArea());
                    return newPerson;
                }).collect(Collectors.toList());
        System.out.println(persons.get(0));
        System.out.println(collect1.get(0));
        //改变原来的stream
        List<Person> collect = persons.stream()
                .map(person -> {
                    person.setSalary(person.getSalary() + 20000);
                    return person;
                })
                .collect(Collectors.toList());
        System.out.println(persons.get(0));
        System.out.println(collect.get(0));
    }

    @Test
    public void test10() {
        //将两个字符数组合并成一个新的字符数组
        List<String> list = Arrays.asList("m,k,l,a", "1,3,5,7");
        List<String> collect = list.stream()
                .flatMap(item -> {
                    String[] strings = item.split(",");
                    return Arrays.stream(strings);
                })
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println(collect);
    }

    @Test
    public void test11() {
        String[] strings = {"abb", "abcd", "fegc", "efe", "adfes"};
        OptionalInt max = Arrays.stream(strings)
                .filter(x -> startWithA(x))
                .mapToInt(x -> map(x)).max();
        System.out.println(max);
    }

    private boolean startWithA(String s) {
        System.out.println("startWithA " + s);
        return s.startsWith("a");
    }

    private int map(String a) {
        System.out.println(a + " " + a.length());
        return a.length();
    }
}
