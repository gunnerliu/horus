package cn.archliu.horus.infr.domain.metrics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-04-29 19:20:10
 * @Description: TDEngine 表增加列实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class AddColumnEntity {

    /** TDEngine超级表的表名 */
    private String taosStName;

    /** 列名 */
    private String columnName;

    /** 列 code */
    private String columnCode;

    /** 列类型 */
    private String columnType;

    public AddColumnEntity setColumnType(String dataType) {
        if ("NCHAR".equals(dataType)) {
            // nchar 默认 64 字节
            this.columnType = "NCHAR(64)";
        } else {
            this.columnType = dataType;
        }
        return this;
    }

}
