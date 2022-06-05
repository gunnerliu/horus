create table if not exists horus_reach_channel(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    channel_code    varchar(128) NOT    NULL    UNIQUE  COMMENT '通道编码',
    channel_name    varchar(128) NOT NULL COMMENT '通道名称',
    category_code    varchar(256) NOT NULL COMMENT '匹配类别编码',
    enable_state    VARCHAR(16) NOT NULL    DEFAULT 'ENABLED'   COMMENT '启用状态',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='消息触达通道';