package cn.archliu.horus.server.domain.groovy.web.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("编辑 groovy 脚本")
@Data
@Accessors(chain = true)
public class EditGroovyDTO {

    @ApiModelProperty("脚本 code")
    @NotBlank(message = "脚本 code 不允许为空")
    private String groovyCode;

    @ApiModelProperty("Groovy 脚本内容")
    @NotBlank(message = "脚本内容不允许为空")
    private String scriptContent;

}
