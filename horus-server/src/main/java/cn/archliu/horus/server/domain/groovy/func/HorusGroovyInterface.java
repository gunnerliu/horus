package cn.archliu.horus.server.domain.groovy.func;

import java.util.Map;

/**
 * @Author: Arch
 * @Date: 2022-05-20 20:13:34
 * @Description: Horus Groovy 脚本接口，CLASS_LOAD 脚本需要实现这个接口
 */
public interface HorusGroovyInterface {

    /**
     * horus 调用的方法
     * 
     * @param args
     * @return
     */
    Object run(Map<Object, Object> args);

}
