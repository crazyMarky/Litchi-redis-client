package top.xiaomingkeji.redis;

import top.xiaomingkeji.redis.model.Arg;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author liaohuiming
 * @date 2019/9/6 17:18
 */
public class Handler {

    private static RedisClient redisClient;

    private static Handler ourInstance = new Handler();


    public static Handler getInstance(Arg enterArg) {
        try{
            RedisClient redisClientBIO = RedisClientBIO.getInstance(enterArg);
            redisClient = redisClientBIO;
        }catch (Exception e){
            System.out.println("Handler getInstance failed");
            e.printStackTrace();
        }
        return ourInstance;
    }

    public static Handler getInstance() {
        return ourInstance;
    }

    private Handler() {
    }

    /**
     * 处理get的情况
     * @param strings
     */
    public void handleGet( List<String> strings){
        int size = strings.size();
        if (size != 2){
            System.out.println("GET command don`t match argument");
            return;
        }
        if (!"".equals(strings.get(1))){
            System.out.println(redisClient.get(strings.get(1)));
        }else {
            System.out.println("Invalid key "+strings.get(1));
        }
    }

    public void handleSet( List<String> strings){
        int size = strings.size();
        if (size != 3){
            System.out.println("SET command don`t match argument");
            return;
        }
        if (!"".equals(strings.get(1))){
            if (!"".equals(strings.get(2))){
                System.out.println(redisClient.set(strings.get(1),strings.get(2)));
            }else {
                System.out.println("Invalid value "+strings.get(2));
            }
        }else {
            System.out.println("Invalid key "+strings.get(1));
        }
    }

    /**
     * 处理ping
     * @param strings
     */
    public void handlePing( List<String> strings){
        int size = strings.size();
        if (size >= 3){
            System.out.println("PING command don`t match argument");
            return;
        }
        strings = strings.stream().filter(x -> !x.isEmpty()).collect(Collectors.toList());
        if (size == 2 ){
            System.out.println(redisClient.ping(strings.get(1)));
        }else {
            System.out.println(redisClient.ping());
        }
    }
}
