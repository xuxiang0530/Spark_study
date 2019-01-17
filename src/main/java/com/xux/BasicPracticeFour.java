package com.xux;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.catalyst.expressions.aggregate.Collect;
import scala.Int;
import scala.Tuple2;
import scala.Tuple3;
import scala.Tuple4;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
//new Tuple2<String,Tuple4<Integer,Integer,Integer, Integer>>(
        JavaPairRDD<String, Tuple4<Integer,Integer,Integer, Integer>> outRdd = inputRdd
                .filter(v -> {
                    Boolean f;
                    String[] strSplit = v.split(",");
                    //判断是否4位
                    f = (strSplit.length == 4);

                    //过滤非数字
                    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
                    if(f)
                    {
                        for (String str: strSplit
                             ) {
                            if(!pattern.matcher(str).matches())
                            {
                                f = false;
                                return f;
                            }
                        }
                    }
                    return f;
                })
                .mapToPair(v -> {
                        String[] strSplit = v.split(",");
                        String id = strSplit[0];
                    Integer x = Integer.parseInt( strSplit[1]);
                    Integer y = Integer.parseInt( strSplit[2]);
                    Integer z = Integer.parseInt( strSplit[3]);
                    return new Tuple2<>(id,new Tuple3<>(x,y,z));
                    }
                )
                .groupByKey()
                .mapToPair(x -> {
                    Iterator<Tuple3<Integer,Integer,Integer>> iterator =  x._2.iterator();
                    Integer sumx;
                    Integer maxy ;
                    Integer minz;
                    Integer count;
                    Tuple3<Integer,Integer,Integer> tuple3;
                    if(iterator.hasNext()) {
                        tuple3 = iterator.next();
                        sumx = tuple3._1();
                        maxy = tuple3._2();
                        minz = tuple3._3();
                        count = 1;
                        while (iterator.hasNext()) {
                            tuple3 = iterator.next();
                            sumx += tuple3._1();
                            maxy = maxy > tuple3._2() ? maxy : tuple3._2();
                            minz = minz > tuple3._3() ? tuple3._3() : minz;
                            count++;
                        }
                        Integer avgx = count == 0 ? 0 : sumx / count;
                        return new Tuple2<>(x._1,new Tuple4<>(sumx,maxy,minz,avgx));
                    }
                    else
                    {
                        return null;
                    }
                });

            outRdd.foreach(x -> System.out.println(String.format("id: %s ,sumx : %d , maxy : %d ,minz : %d ,avgx: %d ."
                    ,x._1
                    ,x._2._1()
                    ,x._2._2()
                    ,x._2._3()
                    ,x._2._4()
            )));


        jsc.stop();
    }
}
