package cn.archliu.horus.infr.domain.schedule.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务表
 * </p>
 *
 * @author Arch
 * @since 2022-04-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_schedule_job")
public class HorusScheduleJob implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 定时任务编号
     */
    private String jobCode;

    /**
     * 定时任务名称
     */
    private String jobName;

    /**
     * corn表达式
     */
    private String cornStr;

    /**
     * 定时任务类型,BEAN->执行job bean,SCRIPT->执行脚本
     */
    private String jobType;

    /**
     * BEAN->执行 bean,SCRIPT->执行脚本
     */
    private String jobExecuteId;

    /**
     * 定时任务参数,json字符串
     */
    private String paramStr;

    /**
     * 状态,ENABLED->启用,DISABLED->禁用
     */
    private String state;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
