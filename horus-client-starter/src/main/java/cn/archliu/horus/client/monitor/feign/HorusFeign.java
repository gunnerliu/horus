package cn.archliu.horus.client.monitor.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import cn.archliu.horus.client.monitor.entity.HorusCounter;
import cn.archliu.horus.client.monitor.entity.HorusMetrics;

@FeignClient(name = "HorusFeign", url = "${horus.client.horus-host:http://127.0.0.1:9013}")
public interface HorusFeign {

	/**
	 * 计数指标数据采集
	 * 
	 * @param counters
	 * @param appName
	 * @param accessParty
	 */
	@PutMapping("/inner/horus/collection/counterCol")
	public void counterCol(@RequestBody List<HorusCounter> counters, @RequestParam("appName") String appName,
			@RequestParam("accessParty") String accessParty, @RequestParam("instanceId") String instanceId);

	/**
	 * 实体指标数据采集
	 * 
	 * @param metrics
	 * @param appName
	 * @param accessParty
	 */
	@PutMapping("/inner/horus/collection/metricsCol")
	public void metricsCol(@RequestBody List<HorusMetrics> metrics, @RequestParam("appName") String appName,
			@RequestParam("accessParty") String accessParty, @RequestParam("instanceId") String instanceId);

}
