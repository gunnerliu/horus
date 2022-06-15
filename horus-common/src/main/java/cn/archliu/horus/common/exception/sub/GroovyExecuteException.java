package cn.archliu.horus.common.exception.sub;

import cn.archliu.horus.common.exception.BaseException;

/**
 * @Author: Arch
 * @Date: 2022-05-20 22:49:59
 * @Description: groovy 脚本执行异常
 */
public class GroovyExecuteException extends BaseException {

    public GroovyExecuteException() {
        super("groovy execute error !");
    }

    public GroovyExecuteException(String message) {
        super(message);
    }

    public static GroovyExecuteException throwE() {
        return new GroovyExecuteException();
    }

    public static GroovyExecuteException throwE(String message) {
        return new GroovyExecuteException(message);
    }

}
