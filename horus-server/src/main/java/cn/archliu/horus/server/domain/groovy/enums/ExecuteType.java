package cn.archliu.horus.server.domain.groovy.enums;

/**
 * @Author: Arch
 * @Date: 2022-05-20 22:07:21
 * @Description: groovy 脚本执行类型
 */
public enum ExecuteType {

    SCRIPT, // SCRIPT->使用GroovyScript执行
    CLASS_LOAD; // CLASS_LOAD->使用 GroovyClassLoader 加载

}
