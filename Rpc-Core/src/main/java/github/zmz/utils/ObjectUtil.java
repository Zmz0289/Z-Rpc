package github.zmz.utils;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@Slf4j
public class ObjectUtil {

    /**
     * 序列化对象
     *
     * @param obj 待序列化的对象
     * @return
     */
    public static byte[] serialize(Object obj) {
        if (obj == null) {
            return null;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(obj);
        } catch (IOException e) {
            log.error("ObjectUtil serialize has error occurred, msg = {}", e.getMessage(), e);
        }


        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 反序列化对象
     *
     * @param objectArr 对象字节数组
     * @return
     */
    public static <T> T deSerialize(byte[] objectArr, Class<T> clazz) {
        if (objectArr == null) {
            return null;
        }

        ByteInputStream byteInputStream = new ByteInputStream(objectArr, objectArr.length);

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);

            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            log.error("ObjectUtil deSerialize has error occurred, msg = {}", e.getMessage(), e);
        }

        throw new RuntimeException("deSerialize error");
    }


}