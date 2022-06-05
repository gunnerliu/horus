package cn.archliu.horus.server.domain.query.web.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@ApiModel("TDEngine 查询实体")
@Data
@Accessors(chain = true)
public class TaosQueryDTO {

    @ApiModelProperty("查询 sql")
    @NotBlank(message = "查询 sql 不允许为空")
    private String querySql;

}
