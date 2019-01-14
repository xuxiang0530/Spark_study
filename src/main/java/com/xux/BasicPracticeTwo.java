package com.xux;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Int;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        //•	使用union连接rdd1和rdd2，生成rdd4
        //•	使用glom打印rdd4的各个partition
        //•	使用coalesce将rdd4的分区数改为3，并生成rdd5
        //•	使用repartition将rdd4的分区数改为10，并生成rdd6
        //•	使用glom分别打印rdd5和rdd6中的partition元素均匀性
        JavaRDD<Integer> rdd4 = rdd1.union(rdd2);
        rdd4.cache();

        JavaRDD<Integer> rdd5 = rdd4.coalesce(3);
        JavaRDD<Integer> rdd6 = rdd4.repartition(10);

        PrintPartition(rdd4,"rdd4");

        PrintPartition(rdd5,"rdd5");

        PrintPartition(rdd6,"rdd6");
        jsc.stop();
    }

    private  static void PrintPartition(JavaRDD<Integer> rdd,String rddName)
    {
        List<List<Integer>> listRdd4 = rdd.glom().collect();

        System.out.println(String.format("%s has %d partitions",rddName,listRdd4.size()));
        for(int i = 0 ;i< listRdd4.size();i++)
        {
            System.out.println(String.format("Partition %d is:[%s]",i,String.join(",",listRdd4.get(i).stream().map(x -> x.toString()).collect(Collectors.toList()))));
        }
    }
}
