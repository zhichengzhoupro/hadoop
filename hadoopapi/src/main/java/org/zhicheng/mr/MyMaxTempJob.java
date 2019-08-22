package org.zhicheng.mr;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class MyMaxTempJob {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        if(args.length != 2) {
            System.out.println("Usage Temparateure <input path><output path>");
            System.exit(-1);
        }

        /*

            一下set都是在设置configuration对象
         */

        Job job = Job.getInstance();
        job.setJarByClass(MyMaxTempJob.class);


        //设置作业名
        job.setJobName("MyMaxTempJob");

        //文件的输入和输出路径
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        //job 设置Mapper
        job.setMapperClass(MyMaxTempMapper.class);
        //job 设置reducer
        job.setReducerClass(MyMaxReduce.class);

        //设置输出keyleixing
        job.setOutputKeyClass(Text.class);

        //
        job.setOutputValueClass(IntWritable.class);

        //开始执行
        System.out.println(job.waitForCompletion(true) ? 0:1);
    }
}
