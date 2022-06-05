package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

/**
 * @Author: Arch
 * @Date: 2022-04-23 18:09:06
 * @Description: 参数错误异常
 */
public class ParamErrorException extends BaseException {

    public ParamErrorException() {
        super("parameter error exception !");
    }

    public ParamErrorException(String message) {
        super(message);
    }

    public static ParamErrorException throwE() {
        return new ParamErrorException();
    }

    public static ParamErrorException throwE(String message) {
        return new ParamErrorException(message);
    }

}
