package cn.archliu.horus.infr.domain.metrics.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-05 21:08:42
 * @Description: TDEngine 超级表信息
 */
@Data
@Accessors(chain = true)
public class ShowSTableEntity {

    /** 超级表表名 */
    private String name;

    /** 创建时间 */
    private String createdTime;

    /** 列数量 */
    private Integer columns;

    /** 标签数量 */
    private Integer tags;

    /** 子表数量 */
    private Integer tables;

}
