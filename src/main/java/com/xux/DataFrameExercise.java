package com.xux;

import org.apache.spark.sql.*;
import static org.apache.spark.sql.functions.*;


public class DataFrameExercise {
    public static void main(String args[])
    {
        SparkSession spark = SparkSession
                .builder()
                .master("local")
                .appName("Java Spark SQL data sources example")
                .getOrCreate();


        Dataset<Row> userDF =
                spark.read().format("json").load("Data/ml-1m/users.json");
        userDF.cache();
//        userDF.show(4);
//        userDF.limit(2).toJSON().foreach((String s) -> System.out.println(s));
//        userDF.printSchema();
//        userDF.withColumn("age2",userDF.col("age").plus(1)).show(2);

//        Row[] rows = userDF.collect();
//        for(int i = 0;i <rows.length;i++)
//        {
//            System.out.println(rows[1].getString(1));
//        }
//
//        System.out.println(userDF.first().toString());
//        userDF.take(2);
//        userDF.head(2);
//        userDF.select("userID","age").show();
//        userDF.selectExpr("userID", "ceil(age/10) as newAge").show(2);
//        userDF.select(max("age"), min("age"), avg("age")).show();
//        userDF.filter(col("age").gt(30)).show(2);
//        userDF.filter("age > 30 and occupation = 10").show();
//        userDF.select("userID", "age").filter("age > 30").show(2);
//        userDF.filter("age > 30").select("userID", "age").show(2);
//
//        userDF.groupBy("age").count().show();
//        userDF.groupBy("age").agg(count("gender"),countDistinct("occupation")).show();
        //Dataset<Row> userDF = spark.read().load("/tmp/users.json");

        Dataset<Row> ratingDF = spark.read().parquet("Data/ml-1m/ratings.parquet");
        ratingDF.write().mode("overwrite").json("Data/ml-1m/ratings.json");
        //ratingDF
        //Dataset<Row> ratingDF = spark.read().format("json").load("Data/ml-1m/ratings.json");

        ratingDF.filter("movieID = 2116")
                .join(userDF,"userID" )
                .select("gender", "age")
                .groupBy("gender", "age")
                .count()
                .show();

        spark.stop();
    }
}
