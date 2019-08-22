package org.zhicheng.mr;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MyMaxTempMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final int MISSING = 9999;

    //每一行运行一次
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 得到一行
        String line = value.toString();
        //取出年份
        String year = line.substring(15, 19);

        //提取气温
        int airTemperature;
        if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
            airTemperature = Integer.parseInt(line.substring(88, 92)); }else{
            airTemperature = Integer.parseInt(line.substring(87, 92));
        }

        //取出空气质量
        String quality = line.substring(92, 93);

        //判断气温的有效性
        if (airTemperature != MISSING && quality.matches("[01459]")) {
            // 这里是出key 出v
            context.write(new Text(year), new IntWritable(airTemperature));
        } }

}
