package cn.archliu.horus.server.domain.groovy.entity;

import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-20 22:48:50
 * @Description: GroovyObject 缓存
 */
@Data
@Accessors(chain = true)
public class GroovyObjectCache {

    /** GroovyObject 脚本编译对象 */
    private GroovyObject groovyObject;

    /** GroovyObject 的加载器 */
    private GroovyClassLoader groovyClassLoader;

    /** groovy 脚本编译时文件最后修改时间 */
    private Long lastModTime;

}
