create table if not exists horus_groovy_info(
    id  BIGINT  AUTO_INCREMENT  PRIMARY KEY COMMENT '主键',
    groovy_code    varchar(256) NOT NULL    COMMENT '指标 code',
    groovy_file_name       VARCHAR(256)    NOT NULL    COMMENT '文件名',
    file_path        VARCHAR(512)    NOT NULL    COMMENT '脚本路径,放在项目resource路径下的脚本需要加前缀 classpath:',
    execute_type    VARCHAR(64)     NOT NULL    COMMENT '执行类型,SCRIPT->使用GroovyScript执行,CLASS_LOAD->使用 GroovyClassLoader 加载',
    last_mod_time   BIGINT   COMMENT  'groovy 脚本编译时文件最后修改时间,会跟文件实际的最后修改时间比较进行缓存更新',
    create_time     datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
)ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT  COMMENT='groovy 脚本信息表';