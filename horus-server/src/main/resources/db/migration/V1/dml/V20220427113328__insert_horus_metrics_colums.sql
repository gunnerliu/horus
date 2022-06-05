insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('horusCounter', '主键', 'ts', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('horusCounter', '打点时间', 'marking_time', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('horusCounter', '计数指标', 'counter_code', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('horusCounter', '链路ID', 'trace_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('horusCounter', '链路节点ID', 'span_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('horusCounter', '标签', 'label', 'NCHAR');