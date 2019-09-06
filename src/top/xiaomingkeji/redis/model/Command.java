package top.xiaomingkeji.redis.model;

/**
 * @author liaohuiming
 * @date 2019/9/6 10:55
 */

public enum  Command {
    HOST("-h","主机地址"),
    PORT("-p","端口")
    ;

    private  String command;
    private String desc;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    Command(String command, String desc) {
        this.command = command;
        this.desc = desc;
    }
}
