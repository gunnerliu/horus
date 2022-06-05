-- 初始化接口监控指标
insert into horus_metrics_desc(metrics_code, metrics_name, metrics_type, taos_st_name) values('apiMonitor', '接口监控', 'METRICS', 'api_monitor_st');

insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '主键', 'ts', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '打点时间', 'marking_time', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '链路ID', 'trace_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '链路节点ID', 'span_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '请求地址', 'request_url', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '请求地址', 'client_ip', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '请求类型', 'method', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('apiMonitor', '耗时', 'time_consuming', 'BIGINT');