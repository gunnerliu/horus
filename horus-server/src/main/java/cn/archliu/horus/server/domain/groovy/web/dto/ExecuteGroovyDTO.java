package cn.archliu.horus.server.domain.groovy.web.dto;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("执行 Groovy 脚本")
@Data
@Accessors(chain = true)
public class ExecuteGroovyDTO {

    @ApiModelProperty("参数")
    private Map<String, Object> args;

}
