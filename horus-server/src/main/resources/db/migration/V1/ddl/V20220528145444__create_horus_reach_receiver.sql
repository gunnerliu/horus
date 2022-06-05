create table if not exists horus_reach_receiver(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    receiver_name    varchar(128) NOT NULL COMMENT '接收人名称',
    reacher         VARCHAR(128)            COMMENT '消息推送方式,dingtalk->钉钉,workwx->企业微信,feishu->飞书,email->电子邮箱',
    mobile      varchar(256)    COMMENT '手机号码',
    email       VARCHAR(256)    COMMENT '电子邮箱',
    web_hook    VARCHAR(512)    COMMENT 'webhook',
    enable_state    VARCHAR(16) NOT NULL    DEFAULT 'ENABLED'   COMMENT '启用状态',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='消息接收人';