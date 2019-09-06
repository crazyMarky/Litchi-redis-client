package top.xiaomingkeji.redis;

import top.xiaomingkeji.redis.constants.Constants;
import top.xiaomingkeji.redis.exception.InvalidArgException;
import top.xiaomingkeji.redis.model.Arg;
import top.xiaomingkeji.redis.model.Command;
import top.xiaomingkeji.redis.model.Struct;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** 程序的入口类
 * @author liaohuiming
 * @date 2019/9/6 10:48
 */


public class Main {

    private static RedisClientBIO redisClientBIO;

    private static  Integer version = 1;

    public static void main (String[] args){
        System.out.println("welcome use myRedisClient V"+version+",author:liaohuiming77@live.com");
        Arg enterArg = null;
        //参数输入
        for (int i = 0; i < args.length-1; i++) {
            enterArg = new Arg();
            if (Command.HOST.getCommand().equals(args[i])){
                if (isValidArg(args[i+1])){
                    enterArg.setHost(args[i+1]);
                    i++;
                }
            }else if (Command.PORT.getCommand().equals(args[i])){
                if (isValidArg(args[i+1])){
                    enterArg.setPort(Integer.valueOf(args[i+1]));
                    i++;
                }
            }
        }
        if (enterArg == null ){
            enterArg = new Arg();
            enterArg.setHost(Constants.DEFALUT_HOST);
            enterArg.setPort(Constants.DEFALUT_PORT);
        }
        if (enterArg.getHost() == null){
            enterArg.setHost(Constants.DEFALUT_HOST);
        }
        if (enterArg.getPort() == null){
            enterArg.setPort(Constants.DEFALUT_PORT);
        }
        redisClientBIO = RedisClientBIO.getInstance(enterArg);

        if (redisClientBIO != null){
            System.out.println("connected "+enterArg.getHost()+" "+enterArg.getPort());
        }
        Scanner scanner = new Scanner(System.in);
        while (true){
            String next = scanner.nextLine();
            //System.out.println(next);
            if ("".equals(next)){
                System.out.println("empty enter");
            }else {
                String[] s = next.split(" ");
                List<String> strings = Arrays.asList(s);
                if (strings != null && strings.size()> 0){
                    if (Struct.GET.getStruct().equals(strings.get(0))){
                        if (!"".equals(strings.get(1))){
                            System.out.println(redisClientBIO.get(strings.get(1)));
                        }else {
                            System.out.println("Invalid key "+strings.get(1));
                        }
                    } else if (Struct.SET.getStruct().equals(strings.get(0))){
                        if (!"".equals(strings.get(1))){
                            if (!"".equals(strings.get(2))){
                                System.out.println(redisClientBIO.set(strings.get(1),strings.get(2)));
                            }else {
                                System.out.println("Invalid value "+strings.get(2));
                            }
                        }else {
                            System.out.println("Invalid key "+strings.get(1));
                        }
                    } else {
                        System.out.println("Invalid strut "+strings.get(0));
                    }
                }

            }
        }
    }

    //校验参数是否合法
    private static boolean isValidArg(String arg){
        if (arg.isEmpty() || arg.contains("-")){
            throw new InvalidArgException("InvalidArg arg :"+arg);
        }
        return true;
    }


}
