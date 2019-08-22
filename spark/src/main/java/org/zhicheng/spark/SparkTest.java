package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

public class SparkTest {
    public static void main(String[] args) {

        /*
           1. 设置spark的运行环境 和模式
           2。 可以设置spark在web UI 中显示 application的名称
           3  可以配置spark的资源情况 比如内存+core CPU 核数 一个核跑一个task 不等同于我们的物理核

         */

        SparkConf sparkConf = new SparkConf();
        //spark运行模式
        //1. local 在IDE spark用local模式进行开发
        //2. standalone spark 自带的资源调度框架 支持分布式搭建，spark任务可以依赖standalone资源调度框架
        //3  yarn hadoop 生态圈中的资源调度框架 ， 也可以基于yarn 调度资源
        //4 mesos  不常用
        sparkConf.setMaster("local");
        sparkConf.setAppName("JavaSparkTestName"); // webUI 中的名字

        /*
            https://spark.apache.org/docs/latest/configuration
            这里可以找到所有需要的配置
         */
        //sparkConf.set("")


        /*
         SparkContext 是通往集群的唯一通道
         */
        JavaSparkContext javaSparkContext = new JavaSparkContext(sparkConf);

        /*
            读取文件
         */
        JavaRDD<String> lines = javaSparkContext.textFile("testfile");


        /*
            flatMap 进一条数据 出多条数据 ， 一对多的关系

         */
        JavaRDD<String>  words = lines.flatMap(new FlatMapFunction<String, String>() {
            public Iterator<String> call(
                    // 这里是key
                    String s) throws Exception {

                return Arrays.asList(s.split(" ")).iterator();
            }
        });

        /*

        在java中 如果想想某个RDD 转换成KV格式xxxToPair
        每一个单词有一个键值对表示
         */
        JavaPairRDD<String, Integer> pairWords = words.mapToPair(new PairFunction<String, String, Integer>() {

            //string s 表示那个单词
            public Tuple2<String, Integer> call(String s) throws Exception {
                return new Tuple2<String, Integer>(s, 1);
            }
        });

        /*
            1. 先将相同的key分组
            2. 对每一组key对应的value去按照你的逻辑处理
         */
        JavaPairRDD<String, Integer> result = pairWords.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    public Integer call(Integer integer, Integer integer2) throws Exception {
                        return integer + integer2;
                    }
                }
        );

        JavaPairRDD<Integer, String> reduceKeyToValue = result.mapToPair(new PairFunction<Tuple2<String,Integer>, Integer, String>() {
            public Tuple2<Integer, String> call(Tuple2<String, Integer> stringIntegerTuple2) throws Exception {
//                return new Tuple2<Integer, String>(stringIntegerTuple2._2, stringIntegerTuple2._1);
                return stringIntegerTuple2.swap();
            }
        }).sortByKey(true);

        reduceKeyToValue.foreach(new VoidFunction<Tuple2<Integer, String>>() {
            public void call(Tuple2<Integer, String> stringIntegerTuple2) throws Exception {
                System.out.println(stringIntegerTuple2);
            }
        });

        javaSparkContext.close();

    }
}
