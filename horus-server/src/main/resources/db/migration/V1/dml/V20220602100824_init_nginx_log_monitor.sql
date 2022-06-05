-- 初始化接口监控指标
insert into horus_metrics_desc(metrics_code, metrics_name, metrics_type, taos_st_name) values('nginxLogMonitor', 'nginx日志监控', 'METRICS', 'nginx_log_monitor_st');

insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '主键', 'ts', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '打点时间', 'marking_time', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '链路ID', 'trace_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '链路节点ID', 'span_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '客户端IP', 'remote_addr', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '跳点主机', 'http_x_forwarded_for', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '请求uri', 'request_uri', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '请求状态', 'status', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '客户端标识', 'http_user_agent', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('nginxLogMonitor', '处理时间', 'request_time', 'DOUBLE');