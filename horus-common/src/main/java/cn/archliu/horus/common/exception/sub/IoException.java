package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

public class IoException extends BaseException {

    public IoException() {
        super("io exception !");
    }

    public IoException(String message) {
        super(message);
    }

    public static IoException throwE() {
        return new IoException();
    }

    public static IoException throwE(String message) {
        return new IoException(message);
    }

}
