package top.xiaomingkeji.redis.format;

public class MessageFormater {

    //
    public static String formatSetMessage(char[] message){
        int j = 0;
        char[] result = new char[message.length];
        if (message[0] == '+'){
            for (int i = 1; i < message.length; i++) {
                if (message[i]!='\r'&&message[i]!='\n'&&message[i]!='\u0000'){
                    result[j++]=message[i];
                }
            }
        }
        char[] result2 = new char[j];
        for (int i = 0; i < result2.length; i++) {
            result2[i]=result[i];
        }
        return new String(result2);
    }

    //
    public static   String formatGetMessage(char[] message){
        char[] result ;
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

    public static   String formatPingMessage(char[] message){
        return formatGetMessage(message);
    }



}
