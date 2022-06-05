package cn.archliu.horus.infr.domain.reach.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 触达消息记录
 * </p>
 *
 * @author Arch
 * @since 2022-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_reach_message")
public class HorusReachMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收人ID
     */
    private Long receiverId;

    /**
     * 消息类别编码
     */
    private String msgCategoryCode;

    /**
     * 消息等级,INSTANT->即时消息,AGGREGATION->聚合消息
     */
    private String msgLevel;

    /**
     * 消息标签
     */
    private String msgTag;

    /**
     * 发送的消息内容
     */
    private String msgContent;

    /**
     * 发送状态,SUCCESS->成功、FAIL->失败,SILENT->静默的
     */
    private String sendState;

    /**
     * 发送失败提示
     */
    private String errorMsg;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
