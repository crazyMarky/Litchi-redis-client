package top.xiaomingkeji.redis;

/**
 * @author liaohuiming
 * @date 2019/9/6 14:00
 */
public interface RedisClient {

    public String get(final String key);

    public String set(final String key,String value);
}
