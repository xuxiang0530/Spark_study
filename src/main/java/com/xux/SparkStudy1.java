package com.xux;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;

/**
 * Created by zkpk on 1/8/19.
 */
public class SparkStudy1 {
public static void main(String[] args)
{
    SparkConf conf = new SparkConf()
            .setMaster("local[1]")
            .setAppName("wordCount");
    JavaSparkContext jsc = new JavaSparkContext(conf);

    JavaRDD<String> lines = jsc.textFile(args[0]);
    JavaRDD<String> words = lines.flatMap(x ->
            Arrays.asList(x.split(" ")).iterator());
    JavaPairRDD<String, Integer> counts = words
            .mapToPair(w -> new Tuple2<String, Integer>(w, 1))
            .reduceByKey((x, y) -> x + y);

    counts.saveAsTextFile(args[1]);
//    counts.foreach(x -> { System.out.println(String.format("key:%s,value:%d",x._1(),x._2()));});

    jsc.stop();

}
}
