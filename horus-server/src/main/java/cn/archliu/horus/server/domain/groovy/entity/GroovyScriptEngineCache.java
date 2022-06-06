package cn.archliu.horus.server.domain.groovy.entity;

import groovy.lang.GroovyClassLoader;
import groovy.lang.Script;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-20 19:08:00
 * @Description: groovy 脚本执行引擎缓存
 */
@Data
@Accessors(chain = true)
public class GroovyScriptEngineCache {

    /** 脚本code */
    private String groovyCode;

    /** groovy 脚本执行引擎 */
    private Script groovyScript;

    /** GroovyObject 的加载器 */
    private GroovyClassLoader groovyClassLoader;

    /** groovy 脚本编译时文件最后修改时间 */
    private Long lastModTime;

}
