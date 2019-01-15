package com.xux;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class BasicPracticeThree {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("basicPracticeThree");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        jsc.setLogLevel("error");
        List<Tuple2<String, Integer>> data = Arrays.asList(
                new Tuple2("coffee", 1),
                new Tuple2("coffee", 3),
                new Tuple2("panda", 4),
                new Tuple2("coffee", 5),
                new Tuple2("street", 2),
                new Tuple2("panda", 5)
        );
        JavaPairRDD<String, Integer> input = jsc.parallelizePairs(data);

        // TODO add your code here
//        计算相同Key对应的的所有value的平均值，并输出到目录/tmp/output下
        JavaPairRDD<String,SumAndCount> sumAndCouuntRdd = input.groupByKey()
                .mapToPair(x -> {
                    Iterator<Integer> iv =  x._2.iterator();
                    Integer sum = 0;
                    Integer count = 0;
                    while (iv.hasNext())
                    {
                        sum += iv.next();
                        count++;
                    }
                    return new Tuple2( x._1, new SumAndCount(sum,count));
                });

        JavaPairRDD<String,Double> keyAndAvgrdd = sumAndCouuntRdd.mapToPair(x -> new Tuple2<>(x._1,x._2.average()));

        keyAndAvgrdd.saveAsTextFile("/tmp/output");
        jsc.stop();
    }
}
class SumAndCount implements Serializable {
    final int sum;
    final int count;

    public SumAndCount(int sum, int count) {
        this.sum = sum;
        this.count = count;
    }
    public double average() {
        if (count != 0) {
            return sum*1.0/count;
        } else {
            return 0.0;
        }
    }
}
