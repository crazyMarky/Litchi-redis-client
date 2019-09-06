package top.xiaomingkeji.redis.exception;

/**
 * @author liaohuiming
 * @date 2019/9/6 11:05
 */

public class InvalidArgException extends RuntimeException{
    public InvalidArgException() {
        super();
    }

    public InvalidArgException(String message) {
        super(message);
    }
}
