package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.rdd.RDD;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class SparkD2 {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("test");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        List<String> list = Arrays.asList("a", "b", "c", "d");
        JavaRDD<String> parallelize = sparkContext.parallelize(list, 4);
        list = parallelize.collect();


        List<Tuple2<String, String>> tuple2s = Arrays.asList(
                new Tuple2<String, String>("zhicheng", "a"),
                new Tuple2<String, String>("fangfang", "b"),
                new Tuple2<String, String>("zhouhong", "c"),
                new Tuple2<String, String>("zhicheng", "d")
        );

        JavaPairRDD<String, String> stringStringJavaPairRDD = sparkContext.parallelizePairs(tuple2s, 6);

        JavaPairRDD<String, String> stringStringJavaPairRDD1 = stringStringJavaPairRDD.reduceByKey(new Function2<String, String, String>() {
            @Override
            public String call(String v1, String v2) throws Exception {
                return v2;
            }
        });

        tuple2s =  stringStringJavaPairRDD1.collect();

        System.out.println(tuple2s.size());

        sparkContext.close();


    }
}
