package cn.archliu.horus.infr.domain.collection.entity;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MetricsCol {

    private String metricsCode;

    private List<String> metricsColumns;

    private List<List<Object>> metricsColumnsValue;

    private String splitTag;

    /** 超级表的表名 */
    private String taosStName;

    /** 子表名字 */
    private String childTableName;

}
