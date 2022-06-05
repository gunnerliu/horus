package cn.archliu.horus.infr.domain.collection.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 接入应用信息
 * </p>
 *
 * @author Arch
 * @since 2022-05-22
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_application_info")
public class HorusApplicationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * SDK应用名称
     */
    private String appName;

    /**
     * 接入方
     */
    private String accessParty;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * app应用IP
     */
    private String appIp;

    /**
     * app应用IP
     */
    private Integer appPort;

    /**
     * 在线状态,ONLINE->在线,OFFLINE->离线
     */
    private String onlineState;

    /**
     * 上次心跳时间
     */
    private LocalDateTime lastHeatTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
