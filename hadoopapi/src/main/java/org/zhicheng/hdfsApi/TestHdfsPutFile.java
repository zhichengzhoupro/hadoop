package org.zhicheng.hdfsApi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class TestHdfsPutFile {

    public static void main(String[] args) throws IOException {

        //得到hdfs配置 我重写了core-site
        Configuration configuration = new Configuration();
        //得到文件系统对象
        FileSystem fileSystem = FileSystem.get(configuration);

        //得到路径
        Path path = new Path("hdfs://zhichengzhou:8020/test_file/api_file.txt");
        FSDataOutputStream fsDataOutputStream = fileSystem.create(path);

        fsDataOutputStream.writeChars("feafa");
        fsDataOutputStream.close();

        System.out.println("over");

    }
}
