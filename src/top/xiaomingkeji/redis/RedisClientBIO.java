package top.xiaomingkeji.redis;

import top.xiaomingkeji.redis.model.Arg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**客户端实现类
 * @author liaohuiming
 * @date 2019/9/6 10:47
 */
//reference https://www.cnblogs.com/rjzheng/p/9347749.html
public class RedisClientBIO implements RedisClient{

    private static RedisClientBIO redisClientBIO;
    private Socket socket;
    private OutputStream outputStream;
    private InputStream inputStream;
    private static final Charset charset = Charset.forName("UTF-8");
    private static Arg arg;

    //创建连接
    private RedisClientBIO(Arg arg){
        try {
            this.arg=arg;
            conneted();
        }catch (Exception e){
            System.out.println("ERROR : fail to connect "+arg.getHost()+" "+arg.getPort());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    //链接
    private void conneted(){
        try {
            this.socket = new Socket(arg.getHost(),arg.getPort());
            this.outputStream = this.socket.getOutputStream();
            this.inputStream = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取实例
     * @param arg
     * @return
     * @throws IOException
     */
    public static RedisClientBIO getInstance(Arg arg){
        if (redisClientBIO == null){
            synchronized (RedisClientBIO.class){
                if (redisClientBIO == null){
                    return new RedisClientBIO(arg);
                }
            }
        }
        return redisClientBIO;
    }

    /**
     * 设置一个值
     * @param key
     * @param value
     * @return
     */
    public String  set(final  String key,String value){
        conneted();
        StringBuilder sb = new StringBuilder();
        sb.append("*3").append("\r\n");
        sb.append("$3").append("\r\n");
        sb.append("SET").append("\r\n");
        sb.append("$").append(key.length()).append("\r\n");
        sb.append(key).append("\r\n");
        sb.append("$").append(value.length()).append("\r\n");
        sb.append(value).append("\r\n");
        byte[] bytes= new byte[1024];
        try {
            ByteBuffer encode = charset.encode(sb.toString());
            outputStream.write(encode.array());
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.flush();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer decode = charset.decode(byteBuffer);
        Buffer clear = byteBuffer.clear();
        char[] array = decode.array();
        String formatSetMessage = this.formatSetMessage(array);
        return  formatSetMessage;
    }

    //去除不必要的元素
    private String formatSetMessage(char[] message){
        int j = 0;
        char[] result = new char[message.length];
        if (message[0] == '+'){
            for (int i = 1; i < message.length; i++) {
                if (message[i]!='\r'&&message[i]!='\n'&&message[i]!='\u0000'){
                    result[j++]=message[i];
                }
            }
        }
        return new String(result);
    }

    /**
     * 根据键获取一个值
     * @param key
     * @return
     */
    public String get(final  String key)  {
        conneted();
        StringBuilder sb = new StringBuilder();
        sb.append("*2").append("\r\n");
        sb.append("$3").append("\r\n");
        sb.append("GET").append("\r\n");
        sb.append("$").append(key.length()).append("\r\n");
        sb.append(key).append("\r\n");
        byte[] bytes= new byte[1024];
        try {
            outputStream.write(sb.toString().getBytes());
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.flush();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer decode = charset.decode(byteBuffer);
        char[] array = decode.array();
        String formatGetMessage = this.formatGetMessage(array);
        return  formatGetMessage;
    }

    //去除不必要的元素
    private  String formatGetMessage(char[] message){
        char[] result = null;
        char[] number;
        int numberInt;
        int start = 0;
        int end = 0;
        for (int i = 0; i < message.length; i++) {
            if ( message[i] == '$'){
                start = i+1;
            }
            if (message[i]=='\r' && message[i+1]=='\n'){
                end  = i;
                break;
            }
        }
        int n = 0;
        number = new char[end-start];
        for (int i = 0; i < end -1 ; i++) {
            number[n++]=message[start+i];
        }
        n = 0;
        numberInt = Integer.valueOf(new String(number));
        result = new char[numberInt+1];
        for (int i = end+2; i <= numberInt+end+1; i++) {
            result[n++] = message[i];
        }
        if (message[1]=='-' && message[2]=='1'){
            return "(nil)\n";
        }
        return new String(result);
    }



}
