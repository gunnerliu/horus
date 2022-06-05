package cn.archliu.horus.server.domain.metrics.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MetricsField {

    /** 字段类型 */
    private String propertyType;

    /** 字段变量名 */
    private String propertyName;

    /** 字段备注 */
    private String comment;

}
