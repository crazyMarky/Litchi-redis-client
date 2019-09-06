package top.xiaomingkeji.redis;

import top.xiaomingkeji.redis.model.Arg;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

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

    //创建连接
    private RedisClientBIO(Arg arg){
        try {
            this.socket = new Socket(arg.getHost(),arg.getPort());
            this.outputStream = this.socket.getOutputStream();
            this.inputStream = this.socket.getInputStream();
        }catch (Exception e){
            System.out.println("ERROR : fail to connect "+arg.getHost()+" "+arg.getPort());
            e.printStackTrace();
            System.exit(0);
        }
    }

    private RedisClientBIO() {

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
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer decode = charset.decode(byteBuffer);
        return  decode.toString();
    }

    /**
     * 根据键获取一个值
     * @param key
     * @return
     */
    public String get(final  String key)  {
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
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        CharBuffer decode = charset.decode(byteBuffer);
        return  decode.toString();
    }



}
