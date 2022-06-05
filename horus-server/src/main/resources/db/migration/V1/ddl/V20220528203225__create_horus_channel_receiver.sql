create table if not exists horus_channel_receiver(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    channel_id    BIGINT    COMMENT '触达通道ID',
    receiver_id    BIGINT    COMMENT '接收人ID',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='触达消息记录';