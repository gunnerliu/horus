package cn.archliu.horus.infr.domain.groovy.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * groovy 脚本信息表
 * </p>
 *
 * @author Arch
 * @since 2022-05-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_groovy_info")
public class HorusGroovyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 脚本 code
     */
    private String groovyCode;

    /**
     * 脚本路径,放在项目resource路径下的脚本需要加前缀 classpath:
     */
    private String filePath;

    /**
     * 执行类型,SCRIPT->使用GroovyScript执行,CLASS_LOAD->使用 GroovyClassLoader 加载
     */
    private String executeType;

    /**
     * groovy脚本代码
     */
    private String scriptContent;

    /**
     * groovy 脚本编译时文件最后修改时间,会跟文件实际的最后修改时间比较进行缓存更新
     */
    private Long lastModTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
