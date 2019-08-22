package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.Optional;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class SparkD3 {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("test");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        List<Tuple2<String, String>> tuple2s = Arrays.asList(
                new Tuple2<String, String>("zhangsan", "a"),
                new Tuple2<String, String>("lisi", "b"),
                new Tuple2<String, String>("wangwu", "c"),
                new Tuple2<String, String>("maliu", "d")
        );

        JavaPairRDD<String, String> stringStringJavaPairRDD = sparkContext.parallelizePairs(tuple2s, 2);


        List<Tuple2<String, Integer>> tuple3s = Arrays.asList(
                new Tuple2<String, Integer>("zhangsan", 1),
                new Tuple2<String, Integer>("lisi", 2),
                new Tuple2<String, Integer>("lisi", 3),
                new Tuple2<String, Integer>("wangwu", 3),
                new Tuple2<String, Integer>("tianqi", 4)
        );

        JavaPairRDD<String, Integer> stringIntegerJavaPairRDD = sparkContext.parallelizePairs(tuple3s, 3);


        List<Tuple2<String, String>> tuple4s = Arrays.asList(
                new Tuple2<String, String>("zhangsan", "a"),
                new Tuple2<String, String>("lisi", "mm"),
                new Tuple2<String, String>("wangwu", "gdsfds"),
                new Tuple2<String, String>("tianqi", "g")
        );

        JavaPairRDD<String, String> stringStringJavaPairRDD2 = sparkContext.parallelizePairs(tuple4s, 4);


        /*
            join  可以按照两个rdd的key 去关联
            1. 第一个string 是key ， 第二个string是第一个rdd 的值 integer 是第二个rdd的值
            2. 与父RDD 分区多的那个分区数一致

         */

        JavaPairRDD<String, Tuple2<String, Integer>> join = stringStringJavaPairRDD.join(stringIntegerJavaPairRDD);

        join.foreach(new VoidFunction<Tuple2<String, Tuple2<String, Integer>>>() {
                         @Override
                         public void call(Tuple2<String, Tuple2<String, Integer>> stringTuple2Tuple2) throws Exception {
                             System.out.println("join: " + stringTuple2Tuple2);
                         }
                     }
        );

         /*
            left join  可以按照两个rdd的key 去关联
            1. 第一个string 是key ， 第二个string是第一个rdd 的值 integer 是第二个rdd的值
            2. 相当于sql的左连接 如果key 没有找到 是Optional empty
         */

        JavaPairRDD<String, Tuple2<String, Optional<Integer>>> join2 = stringStringJavaPairRDD.leftOuterJoin(stringIntegerJavaPairRDD);

        join2.foreach(new VoidFunction<Tuple2<String, Tuple2<String, Optional<Integer>>>>() {
                         @Override
                         public void call(Tuple2<String, Tuple2<String, Optional<Integer>>> stringTuple2Tuple2) throws Exception {
                             System.out.println("left join:" + stringTuple2Tuple2);
                         }
                     }
        );

        /*
          1. union RDD 类型必须一致
          2. 分区数与父RDD 相加的和一样
         */

//        stringStringJavaPairRDD.union(stringIntegerJavaPairRDD)



        /*
          intersection 得到两个rdd中 kv 完全一样的
         */
        JavaPairRDD<String, String> intersection = stringStringJavaPairRDD.intersection(stringStringJavaPairRDD2);

        intersection.foreach(new VoidFunction<Tuple2<String, String>>() {
                                 @Override
                                 public void call(Tuple2<String, String> stringStringTuple2) throws Exception {
                                     System.out.println("intersection : " + stringStringTuple2);
                                 }
                             }
        );

        /*
          substrac 得到第一个rdd 中kv 完全和第二个RDD中不一样的
         */

        /*
          cogroup
          将两个RDD 重组 ，value 都变成iterable 集合里面
         */

        JavaPairRDD<String, Tuple2<Iterable<String>, Iterable<Integer>>> cogroup = stringStringJavaPairRDD.cogroup(stringIntegerJavaPairRDD);
        cogroup.foreach(new VoidFunction<Tuple2<String, Tuple2<Iterable<String>, Iterable<Integer>>>>() {
            @Override
            public void call(Tuple2<String, Tuple2<Iterable<String>, Iterable<Integer>>> stringTuple2Tuple2) throws Exception {
                System.out.println("co group : " + stringTuple2Tuple2);
            }
        });
    }

}
