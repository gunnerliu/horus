create table if not exists horus_schedule_history(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    job_code    varchar(64) NOT NULL COMMENT '定时任务编号',
    corn_str    varchar(256)    NOT NULL    COMMENT 'corn表达式',
    job_type    varchar(64) NOT NULL    COMMENT '定时任务类型,BEAN->执行job bean,SCRIPT->执行脚本',
    job_execute_id   varchar(256)    NOT NULL    COMMENT 'BEAN->执行 bean,SCRIPT->执行脚本',
    param_str   varchar(4000)   COMMENT '定时任务参数,json字符串',
    execute_state   VARCHAR(16)     COMMENT '定时任务执行状态',
    msg         VARCHAR(512)    COMMENT '提示信息',    
    create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='定时任务执行记录';