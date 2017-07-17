package cjl.hycollege.com.account.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 将流转换为字符串
 * Created by xiaolong on 2017/6/4.
 */

public class StreamUtil {
    /**
     *
     * @param is 传入的流
     * @return 将流转换为字符串之后将其返回，如果返回为null则表示异常
     */
    public static String streamToString(InputStream is) {
        //1、在读取的过程中，将读取的内容存储在缓冲中，然后一次性的转换成字符串返回
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        //2、定义读取大小
        byte[] buffer = new byte[1024];
        //3、记录读取内容的临时变量
        int temp=-1;
        //4、读流操作，读到没有为止，
        try {
            while ((temp=is.read(buffer))!=-1){
                bos.write(buffer,0,temp);
            }
            //将读取到的数据以字符串方式返回
            return bos.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
