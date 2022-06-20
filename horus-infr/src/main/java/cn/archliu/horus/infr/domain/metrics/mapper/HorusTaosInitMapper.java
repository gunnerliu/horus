package cn.archliu.horus.infr.domain.metrics.mapper;

import java.util.List;

import com.baomidou.dynamic.datasource.annotation.DS;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import cn.archliu.horus.infr.domain.metrics.entity.AddColumnEntity;
import cn.archliu.horus.infr.domain.metrics.entity.ShowSTableEntity;

/**
 * @Author: Arch
 * @Date: 2022-04-27 20:08:23
 * @Description: taos 初始化
 */
@DS("td")
public interface HorusTaosInitMapper {

    /** 创建指标数据库 */
    @Update("create database if not exists metrics PRECISION 'ns' keep 30 days 10 update 0;")
    void initMetricsDB();

    /** 创建计数指标的超级表 */
    void initCounterST();

    /**
     * 创建 TDEngine 超级表
     * 
     * @param taosStName
     * @param columns
     */
    void createTDSTable(@Param("taosStName") String taosStName, @Param("columns") List<AddColumnEntity> columns);

    /**
     * TDEngine 表中添加列
     * 
     * @param addColumnEntity
     */
    void addColumn(@Param("addColumnEntity") AddColumnEntity addColumnEntity);

    /**
     * 捞出 TDEngine 中已经创建的超级表
     * 
     * @return
     */
    List<ShowSTableEntity> showSTables();

}
