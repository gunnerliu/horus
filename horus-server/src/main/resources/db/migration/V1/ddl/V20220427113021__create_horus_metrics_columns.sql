create table if not exists horus_metrics_columns(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    metrics_code    varchar(64) NOT NULL    COMMENT '指标 code',
    column_name     varchar(128)    NOT NULL    COMMENT '列名',
    column_code     varchar(128)    NOT NULL    COMMENT '列 code',
    column_type     varchar(64)     NOT NULL    COMMENT '列类型',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='指标列信息';