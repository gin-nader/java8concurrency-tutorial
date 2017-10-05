package java8Concurrency.partThree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;

/**
 *  This class covers the ConcurrentHashMap portion of part 3 for the Java 8 Concurrency tutorial
 */
public class ConcurrentHashMapExample {

    /**
     * This method provides a basic example for the ConcurrentHashMap class as well as implementing some of its methods
     * including ForEach, Search, and Reduce. ConcurrentHashMap is similar to ConcurrentMap but has new methods to perform
     * parallel operations on the map
     * @param args
     */
    public static void main(String args[]) {
        System.out.println("----------------------------");
        System.out.println("ConcurrentHashMap");
        System.out.println("----------------------------");

        // creates pool based on number of CPU cores
        System.out.println(ForkJoinPool.getCommonPoolParallelism());

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("foo", "bar");
        map.put("han", "solo");
        map.put("r2", "d2");
        map.put("c3", "p0");

        System.out.println("----------------------------");
        System.out.println("Next Section - ForEach");
        System.out.println("----------------------------");

        // prints each key value pair in parallel
        map.forEach(1, (key, value) ->
                System.out.printf("key: %s; value: %s; thread: %s\n",
                        key, value, Thread.currentThread().getName()));



        System.out.println("----------------------------");
        System.out.println("Next Section - Search");
        System.out.println("----------------------------");

        // finds value for foo using multiple threads
        String result = map.search(1, (key, value) -> {
            System.out.println(Thread.currentThread().getName());
            if ("foo".equals(key)) {
                return value;
            }
            return null;
        });
        System.out.println("Result: " + result);

        // finds value that is greater than 3 in length using multiple threads
        String result2 = map.searchValues(1, value -> {
            System.out.println(Thread.currentThread().getName());
            if (value.length() > 3) {
                return value;
            }
            return null;
        });

        System.out.println("Result: " + result2);

        System.out.println("----------------------------");
        System.out.println("Next Section - Reduce");
        System.out.println("----------------------------");

        // combines each key pair into a single variable then prints those values in a single results
        String result3 = map.reduce(1,
                (key, value) -> {
                    System.out.println("Transform: " + Thread.currentThread().getName());
                    return key + "=" + value;
                },
                (s1, s2) -> {
                    System.out.println("Reduce: " + Thread.currentThread().getName());
                    return s1 + ", " + s2;
                });

        System.out.println("Result: " + result3);

    }
}
