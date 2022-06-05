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
 * 定时任务执行记录
 * </p>
 *
 * @author Arch
 * @since 2022-05-25
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_schedule_history")
public class HorusScheduleHistory implements Serializable {

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
     * 定时任务执行状态
     */
    private String executeState;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
