package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.rdd.RDD;
import org.apache.spark.storage.StorageLevel;

public class SparkPersistence {
    public static void main(String[] args) {
        SparkConf sparkConf =  new SparkConf();
        sparkConf.setAppName("sparkPersistence");
        sparkConf.setMaster("local");

        JavaSparkContext sparkContext =  new JavaSparkContext(sparkConf);
        JavaRDD<String> rdds =  sparkContext.textFile("/Users/zhichengzhou/Documents/testData/ncdc/007026-99999-2017");

        /*..
        1. cache 算子默认将Rdd的数据存在内存中， 懒执行算子
         第一次是来自与磁盘的数据， 第二次就是用内存了

         */
        rdds.cache();
//        rdds.persist(StorageLevel.getCachedStorageLevel())
        // 第一次 这次走磁盘
        //
        long start = System.currentTimeMillis();
        System.out.println("Count: "   + rdds.count());
        long end = System.currentTimeMillis();
        System.out.println( "with mile seconds : "  +  + (end -start));


        // 这次走内存
        start = System.currentTimeMillis();
        System.out.println("Count: "   + rdds.count());
        end = System.currentTimeMillis();
        System.out.println(" with mile time: " + (end -start));
        sparkContext.close();
    }
}
