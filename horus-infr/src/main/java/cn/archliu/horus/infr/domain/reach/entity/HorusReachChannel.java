package cn.archliu.horus.infr.domain.reach.entity;

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
 * 消息触达通道
 * </p>
 *
 * @author Arch
 * @since 2022-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_reach_channel")
public class HorusReachChannel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 匹配类别编码
     */
    private String categoryCode;

    /**
     * 启用状态
     */
    private String enableState;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
