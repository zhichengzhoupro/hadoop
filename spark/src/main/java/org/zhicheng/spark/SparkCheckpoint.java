package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkCheckpoint {
    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();

        sparkConf.setMaster("local");
        sparkConf.setAppName("JavaSparkActionTransformeTestName"); // webUI 中的名字


        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        javaSparkContext.setCheckpointDir("./checkpoint");
        JavaRDD<String> lines = javaSparkContext.textFile("testfile");

        lines.checkpoint();

        lines.count();

    }
}
