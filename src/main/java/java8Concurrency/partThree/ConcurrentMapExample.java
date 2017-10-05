package java8Concurrency.partThree;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *  This class covers the ConcurrentMap portion of part 3 for the Java 8 Concurrency tutorial
 */
public class ConcurrentMapExample {

    /**
     * This method provides a basic example for the ConcurrentMap class.
     * @param args
     */
    public static void main(String args[]) {

        // create concurrent map and store some key value pairs
        ConcurrentMap<String, String> map = new ConcurrentHashMap<>();
        map.put("foo", "bar");
        map.put("han", "solo");
        map.put("r2", "d2");
        map.put("c3", "p0");

        // uses lambda expression BiConsumer to print each key value pair
        map.forEach((key, value) -> System.out.printf("%s = %s\n", key, value));

        // adds value to c3 if it is absent, but returns p0 because it is not
        String value = map.putIfAbsent("c3", "p1");
        System.out.println(value);

        // gets value if it exists, returns the passed default value if it doesn't
        String value2 = map.getOrDefault("hi", "there");
        System.out.println(value2);

        // replaces r2 key with d3 value
        map.replaceAll((key, value3) -> "r2".equals(key) ? "d3" : value3);
        System.out.println(map.get("r2"));

        // changes foo's entry to be barbar
        map.compute("foo", (key, value4) -> value4 + value4);
        System.out.println(map.get("foo"));

        // merges foo's old value with a new given value
        map.merge("foo", "boo", (oldVal, newVal) -> newVal + " was " + oldVal);
        System.out.println(map.get("foo"));


    }
}
