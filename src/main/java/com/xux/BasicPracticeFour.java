package com.xux;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Int;
import scala.Tuple2;
import scala.Tuple4;

import java.util.Arrays;

public class BasicPracticeFour {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("basicPracticeFour");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        // TODO add your code here
        //•	从/tmp/input.txt中读取数据到RDD中，
        //•	用RDD的transformation函数实现下列功能SELECT id, SUM(x), MAX(y), MIN(z), AVERAGE(x) FROM T GROUP BY id;
        //•	将结果RDD输出到控制台中
        JavaRDD<String> inputRdd = jsc.textFile("Data/input.txt");

        JavaPairRDD<String, Tuple4<Integer,Integer,Integer, Integer>> outRdd = inputRdd.filter(x -> x.split(",").length == 4)
                .mapToPair(x -> new Tuple2<String,Tuple4<Integer,Integer,Integer, Integer>>({

                }))




        jsc.stop();
    }
}
