package com.xux;

/**
 * Created by xu_xiang2401 on 2019-1-16.
 */

import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.catalyst.expressions.IntegerLiteral;
import org.apache.spark.util.LongAccumulator;
import scala.Function1;
import scala.Int;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class HamletStatistics {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
                .setMaster("local[1]")
                .setAppName("HamletStatistics");
        JavaSparkContext jsc = new JavaSparkContext(conf);

        JavaRDD<String> stopwords = jsc.textFile(args[0]);
        // TODO 1.collect stopwords and broadcast to executors
        List<String> ls = stopwords.collect();
        HashSet<String> hsStopwords = new HashSet<>();
        ls.forEach(x -> hsStopwords.add(x));

        Broadcast<HashSet<String>> stopwordsBroadcase = jsc.broadcast(hsStopwords);

        // TODO 2.define accumulators, countTotal and stopTotal
        LongAccumulator wordCount = jsc.sc().longAccumulator("wordsCount");
        LongAccumulator stopwordCount = jsc.sc().longAccumulator("stopwordCount");

        JavaRDD<String> input = jsc.textFile(args[1]);
        JavaRDD<String> words = input.flatMap(s -> splitWords(s).iterator());
        JavaRDD<String> filteredWords = words.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String v1) throws Exception {
                // TODO filter stop words, increase countTotal or stopTotal
                wordCount.add(1);
                if(stopwordsBroadcase.value().contains(v1))
                {
                    stopwordCount.add(1);
                    return false;
                }
                return true;
            }
        });

        JavaPairRDD<String, Integer> counts = filteredWords.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                // TODO add your code here
                return new Tuple2<>(s,1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                // TODO add your code here
                return v1 + v2;
            }
        });

        // TODO sort result
        JavaPairRDD<String, Integer> sortRdd = counts
                .mapToPair(x -> new Tuple2<>(x._2(),x._1()))
                .sortByKey(false)
                .mapToPair(x -> new Tuple2<>(x._2(),x._1()));

        // TODO output result
        System.out.println(String.format("出现次数最高的前10个单词:[%s]"
                //,sortRdd.take(10).stream().collect()));
                ,String.join(",",sortRdd.take(10).stream().map(x -> x.toString()).collect(Collectors.toList()))
                ));
        //sortRdd.take(10).forEach(x -> System.out.println(x));
        jsc.stop();
    }

    public static List<String> splitWords(String line) {
        List<String> result = new ArrayList<String>();
        String[] words = line.replaceAll("['.,:?!-]", "").split("\\s");
        for (String w: words) {
            if (!w.trim().isEmpty()) {
                result.add(w.trim());
            }
        }
        return result;
    }
}
