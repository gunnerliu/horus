package cn.archliu.horus.server.domain.metrics.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.web.multipart.MultipartFile;

import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsColumns;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.server.domain.metrics.entity.MetricsJson;
import cn.archliu.horus.server.domain.metrics.enums.MetricsType;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsColumnDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsDTO;

/**
 * @Author: Arch
 * @Date: 2022-04-27 12:42:36
 * @Description: 指标数据结构相关服务
 */
public interface MetricsDescService {

    /**
     * 添加指标
     * 
     * @param addMetricsDTO
     */
    void addMetrics(AddMetricsDTO addMetricsDTO);

    /**
     * 添加指标列
     * 
     * @param addMetricsColumn
     */
    void addMetricsColumn(AddMetricsColumnDTO addMetricsColumn);

    /**
     * 分页查询指标
     * 
     * @param page
     * @param metricsType
     * @return
     */
    IPage<HorusMetricsDesc> pageQueryMetrics(Page<HorusMetricsDesc> page, MetricsType metricsType);

    /**
     * 查询指标在 mysql 存储的表结构
     * 
     * @param metricsCode
     * @return
     */
    List<HorusMetricsColumns> queryMetricsColumns(String metricsCode);

    /**
     * 生成指标的实体类
     * 
     * @param metricsCode
     * @return
     */
    String genMetrics(String metricsCode);

    /**
     * 捞出没有在 TDEngine 中建表的 metricsCode 进行建表
     */
    void createTaosSTables();

    /**
     * 导出指标结构，使用 JSON 文件
     * 
     * @param metricsCodes
     * @return
     */
    MetricsJson exportMetrics(List<String> metricsCodes);

    /**
     * 导入指标结构，使用 JSON 文件
     * 
     * @param multipartFile
     */
    void importMetrics(MultipartFile multipartFile);

}
