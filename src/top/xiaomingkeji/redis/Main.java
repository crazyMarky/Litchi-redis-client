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

    private static  float version = 1.1f;

    private static Handler handler ;

    public static void main (String[] args){
        System.out.println("welcome use Litchi-redis-client v"+version+",author:liaohuiming");
        Arg enterArg = new Arg();
        //参数输入
        for (int i = 0; i < args.length-1; i++) {
            if (Command.HOST.getCommand().equals(args[i])){
                if (isValidAddr(args[i+1])){
                    enterArg.setHost(args[i+1]);
                    i++;
                }
            }else if (Command.PORT.getCommand().equals(args[i])){
                if (isValidPort(args[i+1])){
                    enterArg.setPort(Integer.valueOf(args[i+1]));
                    i++;
                }
            }
        }

        //检查参数
        checkArg(enterArg);
        //取出客户端实例
        RedisClient redisClientBIO = RedisClientBIO.getInstance(enterArg);
        handler = Handler.getInstance(redisClientBIO);

        Scanner scanner = new Scanner(System.in);
        //监听键盘的输入
        while (true){
            System.out.print(genHeadGuideText(enterArg));
            String next = "";

            try{
                next = scanner.nextLine();
            }catch (Exception e){
                System.out.println(genDisconnectText(enterArg));
            }
            System.out.println("input:"+next);

            if ("".equals(next)){
                System.out.println("empty enter");
                continue;
            }
            String[] s = next.split(" ");
            List<String> strings = Arrays.asList(s);
            if (strings == null || strings.size() < 0){
                System.out.println("empty enter");
                continue;
            }

            if (Struct.GET.getStruct().equals(strings.get(0))){
                handler.handleGet(strings);
            } else if (Struct.SET.getStruct().equals(strings.get(0))){
                handler.handleSet(strings);
            } else {
                System.out.println("Invalid strut "+strings.get(0));
            }
        }
    }

    private static String genHeadGuideText(Arg enterArg){
       return String.format("%s:%s>", enterArg.getHost(),enterArg.getPort());
    }

    private static String genDisconnectText(Arg enterArg){
        return String.format("disconnected %s:%s", enterArg.getHost(),enterArg.getPort());
    }

    private static void checkArg(Arg enterArg){
        if (enterArg.getHost() == null){
            enterArg.setHost(Constants.DEFALUT_HOST);
        }
        if (enterArg.getPort() == null){
            enterArg.setPort(Constants.DEFALUT_PORT);
        }
    }

    //校验参数是否合法
    private static boolean isValidPort(String arg){
        if (arg.isEmpty() || arg.contains("-")){
            throw new InvalidArgException("Invalid arg :"+arg);
        }
        return true;
    }

    private static boolean isValidAddr(String arg){
        if (arg.isEmpty() || arg.contains("-")){
            throw new InvalidArgException("Invalid address :"+arg);
        }
        return true;
    }


}
