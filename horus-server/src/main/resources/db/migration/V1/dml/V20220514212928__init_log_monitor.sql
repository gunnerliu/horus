-- 初始化日志监控打点
insert into horus_metrics_desc(metrics_code, metrics_name, metrics_type, taos_st_name) values('logMonitor', '日志监控', 'METRICS', 'log_monitor_st');

insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '主键', 'ts', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '打点时间', 'marking_time', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '链路ID', 'trace_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '链路节点ID', 'span_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '日志类', 'logger', 'NCHAR(126)');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '日志信息', 'message', 'NCHAR(256)');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '异常名', 'throw_name', 'NCHAR(126)');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('logMonitor', '异常信息', 'throw_msg', 'NCHAR(256)');