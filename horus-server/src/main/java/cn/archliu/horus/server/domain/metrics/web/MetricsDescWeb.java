package cn.archliu.horus.server.domain.metrics.web;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.archliu.common.exception.sub.ParamErrorException;
import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.CRUDData;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.common.exception.sub.IoException;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsColumns;
import cn.archliu.horus.infr.domain.metrics.entity.HorusMetricsDesc;
import cn.archliu.horus.server.config.HorusServerProperties;
import cn.archliu.horus.server.domain.metrics.entity.MetricsJson;
import cn.archliu.horus.server.domain.metrics.enums.MetricsType;
import cn.archliu.horus.server.domain.metrics.service.MetricsDescService;
import cn.archliu.horus.server.domain.metrics.web.convert.MetricsConvert;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsColumnDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.AddMetricsDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.MetricsColumnDTO;
import cn.archliu.horus.server.domain.metrics.web.dto.MetricsDTO;
import cn.archliu.horus.server.util.PageUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@Api(tags = { "指标数据结构相关接口" })
@RestController
@RequestMapping("/api/horus/metrics")
public class MetricsDescWeb {

    @Autowired
    private MetricsDescService metricsDescService;

    @Autowired
    private HorusServerProperties horusServerProperties;

    @ApiOperation("添加指标")
    @PutMapping("/addMetrics")
    public ComRes<ResData<Void>> addMetrics(@Validated @RequestBody AddMetricsDTO addMetricsDTO) {
        metricsDescService.addMetrics(addMetricsDTO);
        return ComRes.success();
    }

    @ApiOperation("添加指标列")
    @PutMapping("/addMetricsColumn")
    public ComRes<ResData<Void>> addMetricsColumn(@Validated @RequestBody AddMetricsColumnDTO addMetricsColumn) {
        metricsDescService.addMetricsColumn(addMetricsColumn);
        return ComRes.success();
    }

    @ApiOperation("分页查询指标")
    @PostMapping("/pageQueryMetrics")
    public ComRes<CRUDData<MetricsDTO>> pageQueryMetrics(@RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize,
            @RequestParam(value = "metricsType", required = false) MetricsType metricsType) {
        Page<HorusMetricsDesc> page = PageUtil.build(pageIndex, pageSize);
        IPage<HorusMetricsDesc> data = metricsDescService.pageQueryMetrics(page, metricsType);
        return ComRes.success(PageUtil.build(data, MetricsConvert.INSTANCE::convert));
    }

    @ApiOperation("查询指标在 mysql 存储的表结构")
    @GetMapping("/queryMetricsColumns")
    public ComRes<CRUDData<MetricsColumnDTO>> queryMetricsColumns(@RequestParam("metricsCode") String metricsCode) {
        List<HorusMetricsColumns> list = metricsDescService.queryMetricsColumns(metricsCode);
        return ComRes.success(PageUtil.build(list, list.size(), MetricsConvert.INSTANCE::toColumnDTOs));
    }

    @ApiOperation("生成指标的实体类")
    @GetMapping("/genMetrics")
    public ComRes<ResData<String>> genMetrics(@RequestParam("metricsCode") String metricsCode) {
        return ComRes.success(new ResData<>(metricsDescService.genMetrics(metricsCode)));
    }

    @ApiOperation("导出指标结构，使用 JSON 文件")
    @PostMapping("/exportMetrics")
    public ComRes<ResData<Void>> exportMetrics(HttpServletResponse response, @RequestBody List<String> metricsCodes) {
        if (CollUtil.isEmpty(metricsCodes)) {
            throw ParamErrorException.throwE("metricsCodes 为空！");
        }
        // 导出数据结构
        MetricsJson metricsJson = metricsDescService.exportMetrics(metricsCodes);
        byte[] bytes = JSONUtil.toJsonStr(metricsJson).getBytes();
        try (ServletOutputStream ous = response.getOutputStream()) {
            response.reset();
            response.setContentType("application/octet-stream");
            response.setCharacterEncoding("utf-8");
            response.setContentLength(bytes.length);
            response.setHeader("Content-Disposition", "attachment;filename=MetricsExport.json");
            // 数据写出
            ous.write(bytes);
            ous.flush();
        } catch (Exception e) {
            log.error("文件导出异常！", e);
            throw IoException.throwE("文件导出异常！");
        }
        return ComRes.success();
    }

    @ApiOperation("导入指标结构，使用 JSON 文件")
    @PostMapping(value = "/importMetrics")
    public ComRes<ResData<Void>> importMetrics(@RequestParam("MetricsImport") MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw ParamErrorException.throwE("导入的文件为空！");
        }
        // 检查上传文件大小
        if (multipartFile.getSize() > horusServerProperties.getMetricsImportFileSize()) {
            throw ParamErrorException.throwE("导入的文件超过大小限制！");
        }
        // 文件导入
        metricsDescService.importMetrics(multipartFile);
        return ComRes.success();
    }

}
