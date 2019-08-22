package org.zhicheng.hdfsApi;

import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class TestHdfsExample {


        public static void main(String[] args) throws IOException {

            URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());

            String s = "hdfs://zhichengzhou:8020/test_file/test.txt";

            URL url = new URL(s);

            URLConnection urlConnection = url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();

            FileOutputStream fileOutputStream = new FileOutputStream("/Users/zhichengzhou/Documents/testData/test.txt");

            byte[] buf = new byte[1024];
            int len=-1;

            while (len ==  inputStream.read(buf)) {
                fileOutputStream.write(buf, 0, len);
            }

            inputStream.close();
            fileOutputStream.close();
            System.out.println("finish");
        }


}
