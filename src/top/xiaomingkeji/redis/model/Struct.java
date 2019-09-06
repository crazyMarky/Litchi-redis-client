package top.xiaomingkeji.redis.model;

/**
 * @author liaohuiming
 * @date 2019/9/6 12:50
 */
public enum Struct {
    GET("get",100),
    SET("set",101)
    ;
    private String struct;
    private Integer code;

    public String getStruct() {
        return struct;
    }

    public void setStruct(String struct) {
        this.struct = struct;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    Struct(String struct, Integer code) {
        this.struct = struct;
        this.code = code;
    }
}
