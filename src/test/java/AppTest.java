import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.oschina.j2cache.util.SerializationUtils;

/**
 * Created by huangke on 2014/12/16.
 */
public class AppTest {

    // jdk原生序列换方案
    public static byte[] jdkserialize(Object obj) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);) {
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object jdkdeserialize(byte[] bits) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(bits);
                ObjectInputStream ois = new ObjectInputStream(bais);

        ) {
            return ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
        List<TestObject> list = new ArrayList<TestObject>();
        list.add(new TestObject("aaaa", "vvvv"));
        list.add(new TestObject("aaaa", "vvvv"));
        list.add(new TestObject("aaaa", "vvvv"));
        System.out.println("序列化测试：");
        long time1 = System.currentTimeMillis();
        long length1 = 0;
        for (int i = 1; i <= 100000; i++) {
            List<TestObject> ob = (List<TestObject>) jdkdeserialize(jdkserialize(list));
            if (i == 100000)
                System.out.println(ob);
            length1 += SerializationUtils.serialize(list).length;
        }
        System.out.println("100000次原生序列化测试：" + (System.currentTimeMillis() - time1));
        System.out.println("100000次原生序列化测试体积：" + length1);

        long time2 = System.currentTimeMillis();
        long length2 = 0;
        for (int i = 1; i <= 100000; i++) {
            List<TestObject> ob = (List<TestObject>) SerializationUtils.deserialize(SerializationUtils.serialize(list));
            if (i == 100000)
                System.out.println(ob);
            length2 += SerializationUtils.serialize(list).length;
        }
        System.out.println("100000次FST序列化测试：" + (System.currentTimeMillis() - time2));
        System.out.println("100000次FST序列化测试体积：" + length2);

    }
}

class TestObject implements Serializable {

    private String region;
    private String key;

    public TestObject(String region, String key) {
        super();
        this.region = region;
        this.key = key;
    }

    @Override
    public String toString() {
        return "TestObject [region=" + region + ", key=" + key + "]";
    }

}
