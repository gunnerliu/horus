package cn.archliu.horus.server.domain.metrics.service;

import java.util.Map;

/**
 * @Author: Arch
 * @Date: 2022-04-30 09:43:23
 * @Description: beetl 模板引擎
 */
public interface BeetlService {

    /**
     * 基于 btl 文件创建模板
     * 
     * @param templatePath
     * @param params
     * @return
     */
    String generateTemplate(String templatePath, Map<String, Object> params);

}
