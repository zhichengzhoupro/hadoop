package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;

public class SparkActionTransform {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf();

        sparkConf.setMaster("local");
        sparkConf.setAppName("JavaSparkActionTransformeTestName"); // webUI 中的名字


        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);
        JavaRDD<String> lines = javaSparkContext.textFile("testfile");

        JavaRDD<String> rdd1 = lines.filter(new Function<String, Boolean>() {
            public Boolean call(String s) throws Exception {
                return s.equals("hello fangfang");
            }
        });

        //count 是action 算子
        System.out.println(rdd1.count());


        /*
            sample 抽样算子
            withReplacement 表示抽完是否放回来
            后面的数字代表抽样的比例
            seed long类型种子 针对同一批数据只要种子相同 每次抽样的结果也是一样的
         */
        JavaRDD<String> rddSample = lines.sample(true, 0.24);

        rddSample.foreach(new VoidFunction<String>() {
            public void call(String s) throws Exception {
                System.out.println("sample :" + s );
            }
        });
    }
}
