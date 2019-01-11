package com.xux;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by zkpk on 1/10/19.
 */



public class xuxiangWordCount {
    public static void main(String args[])
    {
        String input = "aa bb cc aa bb aa bb aa cc aa abcd";
        if (args.length > 0)
        {
            input = args[0];
        }

        SparkConf sparkConf = new SparkConf();

        sparkConf.setAppName("xuxiangStudy");
        sparkConf.setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD<String> inputRdd = sc.parallelize(Arrays.asList(input.split(" ")),2);

        JavaPairRDD<String,Integer> count = inputRdd
                .mapToPair(x -> new Tuple2<>(x,1))
                .reduceByKey((v1,v2) -> v1 + v2)
                .mapToPair(x -> new Tuple2<>(x._2(),x._1()))
                .sortByKey(false)
                .mapToPair(x -> new Tuple2<>(x._2(),x._1()));

        count.take(10).forEach(x-> System.out.println(String.format("key:%s,value:%d",x._1(),x._2())));
    }
}
