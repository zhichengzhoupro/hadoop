import com.zhicheng.model.User;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import java.io.File;
import java.io.IOException;

public class Serilize {
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

        DatumWriter<User> datumWriter =  new SpecificDatumWriter<User>(User.class);
        DataFileWriter<User> dataFileWriter = new DataFileWriter<User>(datumWriter);
        try {
            dataFileWriter.create(user1.getSchema(), new File("users.avro"));

        dataFileWriter.append(user);
        dataFileWriter.append(user1);

        dataFileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
