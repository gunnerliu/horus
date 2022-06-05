package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

/**
 * @Author: Arch
 * @Date: 2022-05-02 08:51:37
 * @Description: 获取 TDEngine 连接异常
 */
public class TDConnectException extends BaseException {

    public TDConnectException() {
        super("connect TDEngine exception !");
    }

    public TDConnectException(String message) {
        super(message);
    }

    public static TDConnectException throwE() {
        return new TDConnectException();
    }

    public static TDConnectException throwE(String message) {
        return new TDConnectException(message);
    }

}
