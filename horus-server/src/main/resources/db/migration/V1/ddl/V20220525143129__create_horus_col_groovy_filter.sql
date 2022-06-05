create table if not exists horus_col_groovy_filter(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    groovy_code    VARCHAR(64)     COMMENT 'groovy脚本code',
    metrics_code    VARCHAR(64)     COMMENT '需要处理的 metricsCode,AllMetric->处理所有的',
    create_time     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='指标落地前数据处理groovy脚本信息';