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
 * 消息接收人
 * </p>
 *
 * @author Arch
 * @since 2022-05-28
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("horus_reach_receiver")
public class HorusReachReceiver implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 接收人名称
     */
    private String receiverName;

    /**
     * 消息推送方式,dingtalk->钉钉,workwx->企业微信,feishu->飞书,email->电子邮箱
     */
    private String reacher;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * webhook
     */
    private String webHook;

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
