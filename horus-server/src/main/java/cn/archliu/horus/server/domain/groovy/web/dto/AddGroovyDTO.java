package cn.archliu.horus.server.domain.groovy.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.archliu.horus.server.domain.groovy.enums.ExecuteType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("新增 groovy 脚本")
@Data
@Accessors(chain = true)
public class AddGroovyDTO {

    @ApiModelProperty("脚本 code")
    @NotBlank(message = "脚本 code 不允许为空")
    private String groovyCode;

    @ApiModelProperty("执行类型,SCRIPT->使用GroovyScript执行,CLASS_LOAD->使用 GroovyClassLoader 加载")
    @NotNull(message = "执行类型不允许为空")
    private ExecuteType executeType;

    @ApiModelProperty("Groovy 脚本内容")
    @NotBlank(message = "脚本内容不允许为空")
    private String scriptContent;

}
