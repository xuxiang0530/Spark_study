package com.xux;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;
import scala.Int;

import java.util.ArrayList;
import java.util.List;

public class BasicPracticeOne {
    public static void main(String args[])
    {
        SparkConf sparkConf = new SparkConf()
                .setAppName("basicPracticeOne")
                .setMaster("local[*]");

        JavaSparkContext jsc = new JavaSparkContext(sparkConf);

        List<Integer> result = new ArrayList();
        JavaRDD<Integer> input = jsc.parallelize(result,7);

        input.cache();
        //•	打印input中前5个数据
        input.take(5).forEach(x ->System.out.println(String.format("打印input中前5个数据:%s",x.toString())));


        LongAccumulator sumAccum = jsc.sc().longAccumulator("sum");
        LongAccumulator evenNumbersCountAccum = jsc.sc().longAccumulator("evenNumber");

        JavaRDD evenNumberRdd = input.filter(x ->{
                sumAccum.add(x);
                if(x % 2 == 0)
                {
                    evenNumbersCountAccum.add(1);
                    return true;
                }
                else
                {
                    return false;
                }
            });

        //•	输出input中所有元素和
        System.out.println(String.format("输出input中所有元素和:%d",sumAccum.value()));

        //•	输出input中所有元素的平均值
        System.out.println(String.format("输出input中所有元素的平均值:%d",sumAccum.avg()));

        //•	统计input中偶数的个数，并打印前5个
        System.out.println(String.format("统计input中偶数的个数:%d",evenNumbersCountAccum.value()));
        evenNumberRdd.take(5).forEach(x -> System.out.println(String.format("输出input中偶数前5个:%d",x)));
        jsc.stop();
    }
}
