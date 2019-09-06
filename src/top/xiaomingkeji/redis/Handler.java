package top.xiaomingkeji.redis;

import java.util.List;

/**
 * @author liaohuiming
 * @date 2019/9/6 17:18
 */
public class Handler {

    private static RedisClient redisClient;

    private static Handler ourInstance = new Handler();

    public static Handler getInstance(RedisClient redisClient1) {
        redisClient = redisClient1;
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
            System.out.println("Invalid statement");
            return;
        }
        if (!"".equals(strings.get(1))){
            System.out.print(redisClient.get(strings.get(1)));
        }else {
            System.out.println("Invalid key "+strings.get(1));
        }
    }

    public void handleSet( List<String> strings){
        int size = strings.size();
        if (size != 3){
            System.out.println("Invalid statement");
            return;
        }
        if (!"".equals(strings.get(1))){
            if (!"".equals(strings.get(2))){
                System.out.print(redisClient.set(strings.get(1),strings.get(2)));
            }else {
                System.out.println("Invalid value "+strings.get(2));
            }
        }else {
            System.out.println("Invalid key "+strings.get(1));
        }
    }
}
