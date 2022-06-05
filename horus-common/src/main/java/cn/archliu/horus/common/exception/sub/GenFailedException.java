package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

/**
 * @Author: Arch
 * @Date: 2022-04-30 09:21:15
 * @Description: 代码生成失败异常
 */
public class GenFailedException extends BaseException {

    public GenFailedException() {
        super("code generate failed exception !");
    }

    public GenFailedException(String message) {
        super(message);
    }

    public static GenFailedException throwE() {
        return new GenFailedException();
    }

    public static GenFailedException throwE(String message) {
        return new GenFailedException(message);
    }

}
