create table if not exists horus_reach_message(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    receiver_id    BIGINT    COMMENT '接收人ID',
    msg_category_code    varchar(256) NOT NULL DEFAULT '' COMMENT '消息类别编码',
    msg_level            VARCHAR(32)    NOT NULL    DEFAULT 'WARNING' COMMENT   '消息等级,INSTANT->即时消息,AGGREGATION->聚合消息',
    msg_tag    varchar(256) NOT NULL  DEFAULT '' COMMENT '消息标签',
    msg_content     VARCHAR(4000)   COMMENT '发送的消息内容',
    send_state    VARCHAR(16) NOT NULL    DEFAULT 'SUCCESS'   COMMENT '发送状态,SUCCESS->成功、FAIL->失败,SILENT->静默的',
    error_msg       VARCHAR(512)    COMMENT '发送失败提示',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='触达消息记录';