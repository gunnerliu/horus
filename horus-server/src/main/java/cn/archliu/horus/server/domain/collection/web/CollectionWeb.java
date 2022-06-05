package cn.archliu.horus.server.domain.collection.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.archliu.common.response.ComRes;
import cn.archliu.common.response.sub.ResData;
import cn.archliu.horus.server.domain.collection.service.CollectionService;
import cn.archliu.horus.server.domain.collection.web.convert.ColConvert;
import cn.archliu.horus.server.domain.collection.web.dto.HorusCounterDTO;
import cn.archliu.horus.server.domain.collection.web.dto.MetricsColDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = { "数据采集接口,内网(/inner/*)接口不做鉴权" })
@RestController
@RequestMapping({ "/inner/horus/collection", "/api/horus/collection" })
public class CollectionWeb {

    @Autowired
    private CollectionService colService;

    @ApiOperation("计数指标数据采集")
    @PutMapping("/counterCol")
    public ComRes<ResData<Void>> counterCol(@Validated @RequestBody List<HorusCounterDTO> counters,
            @RequestParam("appName") String appName, @RequestParam("accessParty") String accessParty,
            @RequestParam("instanceId") String instanceId) {
        colService.counterCol(ColConvert.INSTANCE.convert(counters), appName, accessParty, instanceId);
        return ComRes.success();
    }

    @ApiOperation("实体指标数据采集")
    @PutMapping("/metricsCol")
    public ComRes<ResData<Void>> metricsCol(@Validated @RequestBody List<MetricsColDTO> metrics,
            @RequestParam("appName") String appName, @RequestParam("accessParty") String accessParty,
            @RequestParam("instanceId") String instanceId) {
        colService.metricsCol(ColConvert.INSTANCE.toMetrics(metrics), appName, accessParty, instanceId);
        return ComRes.success();
    }

}
