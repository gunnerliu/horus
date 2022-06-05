package cn.archliu.horus.server.domain.groovy.web.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import cn.archliu.horus.server.domain.groovy.enums.ExecuteType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("groovy 脚本上传信息")
@Data
@Accessors(chain = true)
public class UploadGroovyDTO {

    @ApiModelProperty("指标 code")
    @NotBlank(message = "指标 code 不允许为空")
    private String groovyCode;

    @ApiModelProperty("执行类型,SCRIPT->使用GroovyScript执行,CLASS_LOAD->使用 GroovyClassLoader 加载")
    @NotNull(message = "执行类型不允许为空")
    private ExecuteType executeType;

}
