create table if not exists horus_application_info(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    app_name    VARCHAR(64)     COMMENT 'SDK应用名称',
    access_party    VARCHAR(64) COMMENT '接入方',
    application_name    VARCHAR(126)    COMMENT '应用名称',
    app_ip          VARCHAR(64) COMMENT 'app应用IP',
    app_port        INT          COMMENT 'app应用IP',
    online_state     VARCHAR(64) COMMENT '在线状态,ONLINE->在线,OFFLINE->离线',
    last_heat_time  datetime    COMMENT '上次心跳时间',
    create_time     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='接入应用信息';