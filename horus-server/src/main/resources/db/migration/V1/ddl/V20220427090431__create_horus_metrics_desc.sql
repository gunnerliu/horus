create table if not exists horus_metrics_desc(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    metrics_code    varchar(64) NOT NULL    UNIQUE  COMMENT '指标 code',
    metrics_name    varchar(64) NOT NULL            COMMENT '指标名',
    metrics_type    varchar(64) NOT NULL            COMMENT '指标类型,COUNTER->计数,METRICS->指标',
    taos_st_name    varchar(64)                     COMMENT 'TDengine超级表的表明',
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='指标数据结构';