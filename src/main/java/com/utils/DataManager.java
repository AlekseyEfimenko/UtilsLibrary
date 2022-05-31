package com.utils;

import org.apache.commons.lang3.RandomStringUtils;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Class for generate and manipulate with different kind of data
 */
public class DataManager {

    private DataManager() {}

    /**
     * Generate random String line
     * @param length The length of generated String
     * @return Random String
     */
    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    /**
     * Generate random number
     * @param range The range to generate random number (from 1 to range)
     * @return Random number
     */
    public static int getRandomNumber(int range) {
        return new Random().nextInt(range);
    }

    /**
     * Reverse sort of list
     * @param list The list to bs sorted reverse
     * @param <T> Type of elements in the list
     */
    public static <T> void reverseList(List<T> list) {
        Collections.reverse(list);
    }

    /**
     * Check if list is sorted
     * @param listOfStrings The list to check in
     * @param <T> The type of objects in the list
     * @return true - if list is sorted. Otherwise false
     */
    public static <T extends Comparable<T>> boolean isSorted(List<T> listOfStrings) {
        if (listOfStrings.isEmpty() || listOfStrings.size() == 1) {
            return true;
        }

        Iterator<T> iter = listOfStrings.iterator();
        T current;
        T previous = iter.next();

        while (iter.hasNext()) {
            current = iter.next();
            if (previous.compareTo(current) > 0) {
                return false;
            }
            previous = current;
        }
        return true;
    }

    /**
     * Get random elements from the list
     * @param lst List to get elements from
     * @param n Number of elements to get random. n <= lst.size()
     * @param <T> The type of objects in the list
     * @return List of random elements selected from lst
     */
    public static <T> List<T> getRandomList(List<T> lst, int n) {
        List<T> copy = new LinkedList<>(lst);
        Collections.shuffle(copy);
        return copy.subList(0, n);
    }
}
