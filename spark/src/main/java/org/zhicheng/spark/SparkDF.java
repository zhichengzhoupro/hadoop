package org.zhicheng.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

public class SparkDF {

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("test");
        JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);

        SQLContext sqlContext = new SQLContext(sparkContext);
        Dataset<Row> json = sqlContext.read().format("json").load("test.json");

        json.show();
        json.printSchema();

        // select year greater than 2011
        Dataset<Row> json1 = json.select("First Name", "Year").where(json.col("Year").$greater(2011));
        json1.show();

        /*
         将DF 注册成临时表
         T1 这样表不在内存中也不在磁盘中 相当于一个指针指向源文件，底层操作解析sparkjob
         */

        json.registerTempTable("t1");
        Dataset<Row> json2 = sqlContext.sql("select * from t1 where Year > 2012");
        json2.show();

        sparkContext.close();
    }
}
