package cn.archliu.horus.server.domain.metrics.entity;

import java.util.List;

import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsColumns;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Arch
 * @Date: 2022-05-18 10:49:48
 * @Description: 导入导出的 metrics 实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class MetricsJson {

    private List<MetricsJsonItem> metrics;

    @Data
    @Accessors(chain = true)
    public static class MetricsJsonItem {

        /** 指标数据结构 */
        private HorusMetricsDesc horusMetricsDesc;

        /** 指标列 */
        private List<HorusMetricsColumns> horusMetricsColumns;

    }

}
