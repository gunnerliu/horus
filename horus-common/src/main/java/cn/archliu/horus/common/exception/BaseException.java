package cn.archliu.horus.common.exception;

/**
 * @Author: Arch
 * @Date: 2022-04-23 22:08:11
 * @Description: 业务基础异常
 */
public class BaseException extends RuntimeException {

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

}
