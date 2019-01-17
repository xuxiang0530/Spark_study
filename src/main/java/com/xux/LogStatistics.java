package com.xux;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

public class LogStatistics {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("LogStatistics");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        Accumulator<Integer> total = jsc.accumulator(0);
        Accumulator<Integer> count400 = jsc.accumulator(0);
        Accumulator<Integer> count200 = jsc.accumulator(0);

        JavaRDD<String> input = jsc.textFile(args[0]);
        input.foreach(s -> {
            String[] row = s.split(",");
            // TODO add your code here
            //    在//TODO注释处添加加入代码, 完成下列功能
            //•	使用累加器计数
            switch (row[0])
            {
                case "200":
                    count200.add(1);
                    break;
                case "400":
                    count400.add(1);
                    break;
            }
            total.add(1);
        });

        // TODO add your code here
        //•	打印出总数，400的个数，200的个数
        System.out.println(String.format("total:%d ,200count: %d ,400count:%d "
                ,total.value()
                ,count200.value()
                ,count400.value()
        ));
        jsc.stop();
    }
}
