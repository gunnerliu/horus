package cn.archliu.horus.server.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cn.archliu.horus.server.domain.reach.enums.ReacherType;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Component
@ConfigurationProperties(prefix = "horus.server")
public class HorusServerProperties {

    /** 指标数据结构导入支持最大上传的文件，默认是1024*1024*2=2M */
    private long metricsImportFileSize = 2097152L;

    /** 是否将 resources/groovy 下的 groovy 脚本信息同步到数据库中 */
    private Boolean syncResourcesGroovy = true;

    /** 默认触达配置 */
    private ReachInfo reachInfo = new ReachInfo();

    /** 消息聚合周期，单位毫秒 */
    private String msgAggregationCycle = "30000";

    @Data
    @Accessors(chain = true)
    public static class ReachInfo {

        /** 需要 at 的手机号 */
        private List<String> atMobiles;

        /** 默认 webHook */
        private String webHook;

        /** 触达器类型 */
        private ReacherType reacherType;

    }

}
