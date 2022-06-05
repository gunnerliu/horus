package cn.archliu.horus.server.domain.groovy.entity;

import cn.archliu.horus.server.domain.groovy.enums.ExecuteType;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-21 11:16:36
 * @Description: groovy 文件上传信息
 */
@Data
@Accessors(chain = true)
public class UploadGroovyInfo {

    /**
     * 指标 code
     */
    private String groovyCode;

    /**
     * 文件名
     */
    private String groovyFileName;

    /**
     * 脚本路径,放在项目resource路径下的脚本需要加前缀 classpath:
     */
    private String filePath;

    /**
     * 执行类型,SCRIPT->使用GroovyScript执行,CLASS_LOAD->使用 GroovyClassLoader 加载
     */
    private ExecuteType executeType;

}
