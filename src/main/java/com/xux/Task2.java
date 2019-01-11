package com.xux;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.*;
/**
 * Created by xu_xiang2401 on 2019-1-11.
 */
public class Task2 {
    public static void main(String args[])
    {
        List<String> list1 = Arrays.asList("abc","bcd","abc","ab","some","all","try");
        List<String> list2 = Arrays.asList("bcd","all","pip","ikks");

        List<String> listResult = list1.parallelStream().filter(x -> !list2.contains(x)).collect(Collectors.toList());

        listResult.forEach(x -> System.out.println(x));
    }
}
