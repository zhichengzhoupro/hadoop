import com.zhicheng.model.User;
import jdk.nashorn.internal.parser.JSONParser;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class Serilize2 {
    public static void main(String[] args) {
        User user = new User();
        user.setFavoriteColor("red");
        user.setFavoriteNumber(88);
        user.setName("zhicheng");

        User user1 = User
                .newBuilder()
                .setFavoriteColor("blue")
                .setFavoriteNumber(666)
                .setName("FangFang")
                .build();



        try {
            Schema schema = new Schema.Parser().parse(new File("/Users/zhichengzhou/Documents/bigdata/avro/user.avsc"));
            DatumWriter<GenericRecord> datumWriter =  new GenericDatumWriter<GenericRecord>();
            DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<GenericRecord>(datumWriter);
            dataFileWriter.create(schema, new File("users-schema_ext.avro"));

            dataFileWriter.append(user);
            dataFileWriter.append(user1);

            dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
