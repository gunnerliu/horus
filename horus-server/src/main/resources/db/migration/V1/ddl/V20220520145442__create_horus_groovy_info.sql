create table if not exists horus_groovy_info(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    groovy_code    varchar(256) NOT NULL    COMMENT '脚本 code',
    file_path        VARCHAR(512)    NOT NULL    COMMENT '脚本路径,DB则为存放为数据库中,放在项目resource路径下的脚本需要加前缀 classpath:',
    execute_type    VARCHAR(64)     NOT NULL    COMMENT '执行类型,SCRIPT->使用GroovyScript执行,CLASS_LOAD->使用 GroovyClassLoader 加载',
    script_content  VARCHAR(20000)              COMMENT 'groovy脚本代码',
    last_mod_time   BIGINT   COMMENT  'groovy 脚本最后修改时间,做缓存比对更新使用',
    create_time     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='groovy 脚本信息表';