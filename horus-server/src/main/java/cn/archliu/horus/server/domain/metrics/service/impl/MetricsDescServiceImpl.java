package cn.archliu.horus.server.domain.metrics.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.CaseFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cn.archliu.horus.common.exception.sub.IoException;
import cn.archliu.horus.common.exception.sub.ParamErrorException;
import cn.archliu.horus.infr.domain.metrics.entity.AddColumnEntity;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsColumns;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.infr.domain.metrics.entity.ShowSTableEntity;
import cn.archliu.horus.infr.domain.metrics.mapper.HorusMetricsColumnsMapper;
import cn.archliu.horus.infr.domain.metrics.mapper.HorusMetricsDescMapper;
import cn.archliu.horus.infr.domain.metrics.mapper.HorusTaosInitMapper;
import cn.archliu.horus.server.domain.metrics.entity.MetricsField;
import cn.archliu.horus.server.domain.metrics.entity.MetricsJson;
import cn.archliu.horus.server.domain.metrics.entity.MetricsJson.MetricsJsonItem;
import cn.archliu.horus.server.domain.metrics.enums.MetricsType;
import cn.archliu.horus.server.domain.metrics.enums.TDDataType;
import cn.archliu.horus.server.domain.metrics.service.BeetlService;
import cn.archliu.horus.server.domain.metrics.service.MetricsDescService;
import cn.archliu.horus.server.domain.metrics.web.convert.MetricsConvert;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsColumnDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsDTO;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MetricsDescServiceImpl implements MetricsDescService {

    /** ?????????????????? */
    protected static final List<String> COM_COLUMNS = ListUtil.toList("ts", "marking_time", "trace_id", "span_id");

    @Autowired
    private HorusMetricsDescMapper metricsDescMapper;

    @Autowired
    private HorusMetricsColumnsMapper metricsColumnsMapper;

    @Autowired
    private HorusTaosInitMapper horusTaosInitMapper;

    @Autowired
    private BeetlService beetlService;

    /** ????????????????????????????????????????????????????????? */
    private ReentrantLock metricsImportLock = new ReentrantLock();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void addMetrics(AddMetricsDTO addMetricsDTO) {
        // 1???????????? metrics_code ??????????????????
        boolean exists = new LambdaQueryChainWrapper<>(metricsDescMapper)
                .eq(HorusMetricsDesc::getMetricsCode, addMetricsDTO.getMetricsCode()).exists();
        if (exists) {
            throw ParamErrorException.throwE(addMetricsDTO.getMetricsCode() + " ??????????????????");
        }
        HorusMetricsDesc horusMetrics = MetricsConvert.INSTANCE.convert(addMetricsDTO);
        // ??????????????????????????? metricsCode
        if (MetricsType.COUNTER.equals(addMetricsDTO.getMetricsType())) {
            // 3???????????????
            metricsDescMapper.insert(horusMetrics);
            return;
        }
        // 2????????? TDEngine ??????
        String stableName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, addMetricsDTO.getMetricsCode());
        horusMetrics.setTaosStName(stableName);
        // 3???????????????
        metricsDescMapper.insert(horusMetrics);
        // 4???mysql ?????????????????????????????????????????? ts???marking_time???trace_id???span_id
        initColumns(addMetricsDTO.getMetricsCode());
        // 5???????????? TDEngine ?????????
        initTDSTable(horusMetrics.getTaosStName());
    }

    /**
     * ???????????????????????????
     * 
     * @param metricsCode
     */
    private void initColumns(String metricsCode) {
        metricsColumnsMapper.initColumns(metricsCode);
    }

    /**
     * ????????? TDEngine ?????????
     * 
     * @param taosStName
     */
    private void initTDSTable(String taosStName) {
        horusTaosInitMapper.createTDSTable(taosStName, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void addMetricsColumn(AddMetricsColumnDTO addMetricsColumn) {
        // 1???????????? metrics_code ???????????????
        HorusMetricsDesc horusMetrics = new LambdaQueryChainWrapper<>(metricsDescMapper)
                .eq(HorusMetricsDesc::getMetricsCode, addMetricsColumn.getMetricsCode()).one();
        if (horusMetrics == null) {
            throw ParamErrorException.throwE(addMetricsColumn.getMetricsCode() + " ??????????????????");
        }
        // 2?????????????????????
        addMetricsColumn.setColumnCode(
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, addMetricsColumn.getColumnCode()));
        // 3?????????????????????????????????
        boolean columnExists = new LambdaQueryChainWrapper<>(metricsColumnsMapper)
                .eq(HorusMetricsColumns::getMetricsCode, addMetricsColumn.getMetricsCode())
                .eq(HorusMetricsColumns::getMetricsCode, addMetricsColumn.getColumnCode()).exists();
        if (columnExists) {
            throw ParamErrorException.throwE(addMetricsColumn.getColumnCode() + " ?????????????????????");
        }
        // 4???????????????
        HorusMetricsColumns metricsColumn = MetricsConvert.INSTANCE.convert(addMetricsColumn);
        metricsColumnsMapper.insert(metricsColumn);
        // 5???TDEngine ???????????????
        AddColumnEntity addColumnEntity = new AddColumnEntity().setTaosStName(horusMetrics.getTaosStName())
                .setColumnCode(metricsColumn.getColumnCode()).setColumnName(metricsColumn.getColumnName())
                .setColumnType(metricsColumn.getColumnType());
        horusTaosInitMapper.addColumn(addColumnEntity);
    }

    @Override
    public IPage<HorusMetricsDesc> pageQueryMetrics(Page<HorusMetricsDesc> page, MetricsType metricsType) {
        QueryWrapper<HorusMetricsDesc> query = null;
        if (metricsType != null) {
            query = new QueryWrapper<HorusMetricsDesc>().eq("metrics_type", metricsType.name());
        }
        return metricsDescMapper.selectPage(page, query);
    }

    @Override
    public List<HorusMetricsColumns> queryMetricsColumns(String metricsCode) {
        return new LambdaQueryChainWrapper<>(metricsColumnsMapper).eq(HorusMetricsColumns::getMetricsCode, metricsCode)
                .list();
    }

    @Override
    public String genMetrics(String metricsCode) {
        // 1???????????? metrics_code ??????????????????
        boolean exists = new LambdaQueryChainWrapper<>(metricsDescMapper)
                .eq(HorusMetricsDesc::getMetricsCode, metricsCode).exists();
        if (!exists) {
            throw ParamErrorException.throwE(metricsCode + " ??????????????????");
        }
        // 2?????????????????????
        List<HorusMetricsColumns> columns = new LambdaQueryChainWrapper<>(metricsColumnsMapper)
                .eq(HorusMetricsColumns::getMetricsCode, metricsCode).list();
        // ????????????
        String className = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, metricsCode);
        Set<String> importPackages = new HashSet<>();
        List<MetricsField> fields = new ArrayList<>();
        for (HorusMetricsColumns column : columns) {
            // ???????????????
            if (ListUtil.toList("ts").contains(column.getColumnCode())) {
                continue;
            }
            MetricsField field = new MetricsField();
            TDDataType dataType = TDDataType.convertThrowE(column.getColumnType());
            field.setPropertyName(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, column.getColumnCode()))
                    .setPropertyType(dataType.getJavaType()).setComment(column.getColumnName());
            fields.add(field);
            importPackages.add(dataType.getClassPath());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("importPackages", importPackages);
        params.put("entity", className);
        params.put("fields", fields);
        return beetlService.generateTemplate("/metrics.java.btl", params);
    }

    @Override
    public void createTaosSTables() {
        // 1????????? TDEngine ???????????????????????????
        List<ShowSTableEntity> createdSTables = horusTaosInitMapper.showSTables();
        List<String> createdSTableNames = createdSTables.stream().map(ShowSTableEntity::getName)
                .collect(Collectors.toList());
        // 2????????? mysql ????????? metricsCodes
        List<HorusMetricsDesc> metricsCodes = new LambdaQueryChainWrapper<>(metricsDescMapper)
                .eq(HorusMetricsDesc::getMetricsType, MetricsType.METRICS.name()).list();
        if (CollUtil.isEmpty(metricsCodes)) {
            return;
        }
        // 3??????????????? TDEngine ?????????????????? metricsCode ????????????
        metricsCodes.parallelStream().filter(metrics -> !createdSTableNames.contains(metrics.getTaosStName()))
                .forEach(metrics -> {
                    List<HorusMetricsColumns> newColumns = new LambdaQueryChainWrapper<>(metricsColumnsMapper)
                            .eq(HorusMetricsColumns::getMetricsCode, metrics.getMetricsCode())
                            .notIn(HorusMetricsColumns::getColumnCode, COM_COLUMNS)
                            .list();
                    // ????????????,??????????????????????????????
                    List<AddColumnEntity> collect = newColumns.stream().map(item -> new AddColumnEntity()
                            .setColumnCode(item.getColumnCode()).setColumnType(item.getColumnType()))
                            .collect(Collectors.toList());
                    log.info("Taos ??????,??????: {},????????????: {}", metrics.getTaosStName(), collect.toString());
                    horusTaosInitMapper.createTDSTable(metrics.getTaosStName(), collect);
                });
    }

    @Override
    public MetricsJson exportMetrics(List<String> metricsCodes) {
        List<MetricsJsonItem> items = new ArrayList<>();
        MetricsJson metricsJson = new MetricsJson(items);
        // ??????????????????????????????
        List<HorusMetricsDesc> descList = new LambdaQueryChainWrapper<>(metricsDescMapper)
                .in(HorusMetricsDesc::getMetricsCode, metricsCodes).list();
        // ??????????????????
        List<HorusMetricsColumns> columns = new LambdaQueryChainWrapper<>(metricsColumnsMapper)
                .in(HorusMetricsColumns::getMetricsCode, metricsCodes).list();
        // ????????????
        for (HorusMetricsDesc metricsDesc : descList) {
            MetricsJsonItem metricsJsonItem = new MetricsJsonItem().setHorusMetricsDesc(metricsDesc)
                    .setHorusMetricsColumns(columns.stream()
                            .filter(item -> StrUtil.equals(metricsDesc.getMetricsCode(), item.getMetricsCode()))
                            .collect(Collectors.toList()));
            items.add(metricsJsonItem);
        }
        return metricsJson;
    }

    @Override
    public void importMetrics(MultipartFile multipartFile) {
        // ????????????????????????
        metricsImportLock.lock();
        try (InputStream ins = multipartFile.getInputStream()) {
            List<String> contents = new ArrayList<>();
            IoUtil.readUtf8Lines(ins, contents);
            if (CollUtil.isEmpty(contents)) {
                throw ParamErrorException.throwE("?????????????????????");
            }
            String importJson = String.join("", contents);
            MetricsJson metricsJson = JSONUtil.toBean(importJson, MetricsJson.class);
            for (MetricsJsonItem item : metricsJson.getMetrics()) {
                // ???????????? metricsCode ??????????????????????????????
                boolean exists = new LambdaQueryChainWrapper<>(metricsDescMapper)
                        .eq(HorusMetricsDesc::getMetricsCode, item.getHorusMetricsDesc().getMetricsCode()).exists();
                // ????????????????????????????????????
                if (!exists) {
                    metricsDescMapper
                            .insert(item.getHorusMetricsDesc().setId(null).setUpdateTime(null).setCreateTime(null));
                    // mysql ?????????
                    metricsColumnsMapper.batchInsert(item.getHorusMetricsColumns());
                    // TDEngine ?????????
                    List<AddColumnEntity> collect = item.getHorusMetricsColumns().stream()
                            // ???????????????????????????????????????????????????????????????
                            .filter(column -> !COM_COLUMNS.contains(column.getColumnCode()))
                            .map(column -> new AddColumnEntity().setColumnCode(column.getColumnCode())
                                    .setColumnType(column.getColumnType()))
                            .collect(Collectors.toList());
                    log.info("Taos ??????,??????: {},????????????: {}", item.getHorusMetricsDesc().getTaosStName(), collect.toString());
                    horusTaosInitMapper.createTDSTable(item.getHorusMetricsDesc().getTaosStName(), collect);
                } else {// ??????????????????????????????????????????
                    List<HorusMetricsColumns> existsColumns = new LambdaQueryChainWrapper<>(metricsColumnsMapper)
                            .eq(HorusMetricsColumns::getMetricsCode, item.getHorusMetricsDesc().getMetricsCode())
                            .list();
                    // ?????????????????????
                    List<HorusMetricsColumns> newColumns = item.getHorusMetricsColumns().stream()
                            .filter(newColumn -> existsColumns.stream().noneMatch(existsColumn -> StrUtil
                                    .equals(existsColumn.getColumnCode(), newColumn.getColumnCode())))
                            .collect(Collectors.toList());
                    // mysql ?????????
                    metricsColumnsMapper.batchInsert(newColumns);
                    // TDEngine ?????????????????????
                    for (HorusMetricsColumns newColumn : newColumns) {
                        AddColumnEntity addColumnEntity = new AddColumnEntity()
                                .setTaosStName(item.getHorusMetricsDesc().getTaosStName())
                                .setColumnCode(newColumn.getColumnCode()).setColumnName(newColumn.getColumnName())
                                .setColumnType(newColumn.getColumnType());
                        log.info("Taos ????????????{} ????????????: {}", item.getHorusMetricsDesc().getTaosStName(),
                                addColumnEntity.toString());
                        horusTaosInitMapper.addColumn(addColumnEntity);
                    }
                }
            }
        } catch (ParamErrorException e) {
            throw e;
        } catch (Exception e) {
            log.error("????????????????????????", e);
            throw IoException.throwE("?????????????????????");
        } finally {
            metricsImportLock.unlock();
        }
    }

}
