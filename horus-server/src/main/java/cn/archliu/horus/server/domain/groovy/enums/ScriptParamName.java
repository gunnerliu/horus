package cn.archliu.horus.server.domain.groovy.enums;

/**
 * @Author: Arch
 * @Date: 2022-05-23 10:49:51
 * @Description: groovy 脚本传入的参数名
 */
public class ScriptParamName {

    /** mysql 数据源 */
    public static final String MASTER = "master";

    /** TDEngine 数据源 */
    public static final String TD = "td";

    /** 日志 log */
    public static final String LOG = "log";

    /** 定时任务传进来的参数 */
    public static final String HORUS_SCHEDULE_PARAM = "horusScheduleParams";

    /**
     * 指标数据落地时传入 filter 的指标数据, metrics数据结构见
     * {@see cn.archliu.horus.infr.domain.collection.entity.MetricsCol}
     */
    public static final String METRICS = "metric";
    /** 指标数据落地时传入 filter 的 appName */
    public static final String APP_NAME = "appName";
    /** 指标数据落地时传入 filter 的 accessParty */
    public static final String ACCESS_PARTY = "accessParty";
    /** 指标数据落地时传入 filter 的 instanceId */
    public static final String INSTANCE_ID = "instanceId";

    /** 消息触达服务 */
    public static final String MESSAGE_REACH = "messageReach";

    private ScriptParamName() {
    }

}
