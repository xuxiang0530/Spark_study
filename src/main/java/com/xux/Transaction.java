package com.xux;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Int;
import scala.Tuple2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zkpk on 1/10/19.
 */
public class Transaction implements Serializable {
    private final int id;
    private final Integer value;
    private final Type type;

    public Transaction(int id, Integer value, Type type) {
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public enum Type {A, B, C, D}

    public int getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }

    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(1, 10, Transaction.Type.D));
        transactions.add(new Transaction(3, 30, Transaction.Type.D));
        transactions.add(new Transaction(6, 60, Transaction.Type.D));
        transactions.add(new Transaction(5, 50, Transaction.Type.D));
        transactions.add(new Transaction(2, 20, Transaction.Type.A));
        transactions.add(new Transaction(4, 40, Transaction.Type.C));

//to do :implement the requirement using java8
        SparkConf sparkConf = new SparkConf()
                .setAppName("Transaction")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        JavaRDD firstRdd = sc.parallelize(transactions, 2);

        JavaRDD DSort =
                firstRdd
                .filter(v -> {
                    Transaction t = (Transaction) v;
                    return t.getType()==Transaction.Type.D;
                })
                 .sortBy(x -> {Transaction t = (Transaction)x; return t.getValue();},false,2);

        List<Transaction> filterSortTransactions = DSort.collect();
        filterSortTransactions.forEach(x -> System.out.println(String.format("id:%d,value:%d",x.getId(),x.getValue())));
    }
}