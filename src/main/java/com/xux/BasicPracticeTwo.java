package com.xux;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BasicPracticeTwo {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("basicPracticeTwo");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        List<Integer> data = Arrays.asList(1,2,3,4,5, 6);
        JavaRDD<Integer> rdd1 = jsc.parallelize(data, 3);
        List<Integer> data2 =Arrays.asList(7,8,9,10,11);
        JavaRDD<Integer> rdd2 = jsc.parallelize(data, 2);
        List<Integer> data3=Arrays.asList(12,13,14,15,16, 17, 18, 19, 20, 21);
        JavaRDD<Integer> rdd3 = jsc.parallelize(data3, 3);

        // TODO add your code here
        jsc.stop();
    }
}
