-- 初始化日志监控打点
insert into horus_metrics_desc(metrics_code, metrics_name, metrics_type, taos_st_name) values('appMonitor', '应用监控', 'METRICS', 'app_monitor_st');

insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '主键', 'ts', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '打点时间', 'marking_time', 'TIMESTAMP');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '链路ID', 'trace_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '链路节点ID', 'span_id', 'NCHAR');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '应用名', 'application_name', 'NCHAR(126)');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '应用IP', 'app_ip', 'NCHAR(64)');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '应用端口', 'app_port', 'INT');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '应用准备时间', 'app_ready_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '应用运行时间', 'app_started_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM所有缓冲区数量', 'jvm_buffer', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM缓冲区数量', 'jvm_mapped_buffer', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '直接内存缓冲区数量', 'jvm_direct_buffer', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM所有缓冲区内存使用', 'jvm_buffer_memory_used', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM缓冲区内存使用', 'jvm_mapped_buffer_memory_used', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '直接内存缓冲区内存使用', 'jvm_direct_buffer_memory_used', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '缓冲区总容量', 'jvm_buffer_total_capacity', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM缓冲区总容量', 'jvm_mapped_buffer_total_capacity', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '直接内存缓冲区总容量', 'jvm_direct_buffer_total_capacity', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '回收后长寿命堆内存池大小', 'jvm_gc_live_data_size', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '长寿命堆内存池最大大小', 'jvm_gc_max_data_size', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM 已分配内存', 'jvm_gc_memory_allocated', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM GC之后内存扩张', 'jvm_gc_memory_promoted', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM GC 所占用的 CPU', 'jvm_gc_overhead', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'gc 暂停次数', 'jvm_gc_pause_count', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'gc 暂停总时间', 'jvm_gc_pause_total_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'gc 暂停最长时间', 'jvmgc_pause_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '元空间 minor gc 次数', 'jvm_minor_gc_pause_mgt_count', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '元空间 minor gc 总时间', 'jvm_minor_gc_pause_mgt_total_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '元空间 minor gc 最长时间', 'jvm_minor_gc_pause_mgt_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '内存分配失败 minor gc 次数', 'jvm_minor_gc_pause_af_count', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '内存分配失败 minor gc 总时间', 'jvm_minor_gc_pause_af_total_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '内存分配失败 minor gc 最长时间', 'jvm_minor_gc_pause_af_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '元空间 minor gc 次数', 'jvm_major_gc_pause_mgt_count', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '元空间 minor gc 总时间', 'jvm_major_gc_pause_mgt_total_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '元空间 minor gc 最长时间', 'jvm_major_gc_pause_mgt_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '内存分配失败 minor gc 次数', 'jvm_major_gc_pause_af_count', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '内存分配失败 minor gc 总时间', 'jvm_major_gc_pause_af_total_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', '内存分配失败 minor gc 最长时间', 'jvm_major_gc_pause_af_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM 申请的内存', 'jvm_memory_committed', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Compressed Class Space-jvm.memory.committed', 'jvm_memory_committed_heap_CCS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Old Gen-jvm.memory.committed', 'jvm_memory_committed_heap_POG', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Survivor Space-jvm.memory.committed', 'jvm_memory_committed_heap_PSS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Metaspace-jvm.memory.committed', 'jvm_memory_committed_heap_M', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Eden Space-jvm.memory.committed', 'jvm_memory_committed_heap_PES', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Code Cache-jvm.memory.committed', 'jvm_memory_committed_heap_CC', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Compressed Class Space-jvm.memory.committed', 'jvm_memory_committed_nonheap_CCS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Old Gen-jvm.memory.committed', 'jvm_memory_committed_nonheap_POG', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Survivor Space-jvm.memory.committed', 'jvm_memory_committed_nonheap_PSS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Metaspace-jvm.memory.committed', 'jvm_memory_committed_nonheap_M', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Eden Space-jvm.memory.committed', 'jvm_memory_committed_nonheap_PES', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Code Cache-jvm.memory.committed', 'jvm_memory_committed_nonheap_CC', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM 可以使用的最大内存', 'jvm_memory_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Compressed Class Space-jvm.memory.max', 'jvm_memory_max_heap_CCS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Old Gen-jvm.memory.max', 'jvm_memory_max_heap_POG', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Survivor Space-jvm.memory.max', 'jvm_memory_max_heap_PSS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Metaspace-jvm.memory.max', 'jvm_memory_max_heap_M', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Eden Space-jvm.memory.max', 'jvm_memory_max_heap_PES', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Code Cache-jvm.memory.max', 'jvm_memory_max_heap_CC', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Compressed Class Space-jvm.memory.max', 'jvm_memory_max_nonheap_CCS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Old Gen-jvm.memory.max', 'jvm_memory_max_nonheap_POG', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Survivor Space-jvm.memory.max', 'jvm_memory_max_nonheap_PSS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Metaspace-jvm.memory.max', 'jvm_memory_max_nonheap_M', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Eden Space-jvm.memory.max', 'jvm_memory_max_nonheap_PES', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Code Cache-jvm.memory.max', 'jvm_memory_max_nonheap_CC', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Compressed Class Space-jvm.memory.used', 'jvm_memory_used_heap_CCS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'JVM 已使用的内存', 'jvm_memory_used', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Old Gen-jvm.memory.used', 'jvm_memory_used_heap_POG', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Survivor Space-jvm.memory.used', 'jvm_memory_used_heap_PSS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Metaspace-jvm.memory.used', 'jvm_memory_used_heap_M', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-PS Eden Space-jvm.memory.used', 'jvm_memory_used_heap_PES', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'heap-Code Cache-jvm.memory.used', 'jvm_memory_used_heap_CC', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Compressed Class Space-jvm.memory.used', 'jvm_memory_used_nonheap_CCS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Old Gen-jvm.memory.used', 'jvm_memory_used_nonheap_POG', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Survivor Space-jvm.memory.used', 'jvm_memory_used_nonheap_PSS', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Metaspace-jvm.memory.used', 'jvm_memory_used_nonheap_M', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-PS Eden Space-jvm.memory.used', 'jvm_memory_used_nonheap_PES', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'nonheap-Code Cache-jvm.memory.used', 'jvm_memory_used_nonheap_CC', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'GC之后 long-lived heap pool 使用的百分比', 'jvm_memory_usage_after_gc', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The percentage of long-lived heap pool used after the last GC event, in the range [0..1]', 'jvm_memory_usage_after_gc_heap_pool', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The peak live thread count since the Java virtual machine started or peak was reset', 'jvm_threads_peak', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The current number of live daemon threads', 'jvm_threads_daemon', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The current number of live threads including both daemon and non-daemon threads', 'jvm_threads_live', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'jvm timed_waiting 线程数量', 'jvm_threads_states_timed_waiting', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'jvm 新建线程数量', 'jvm_threads_states_new', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'jvm 正在运行线程数量', 'jvm_threads_states_runnable', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'jvm 阻塞线程数量', 'jvm_threads_states_blocked', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'jvm 等待线程数量', 'jvm_threads_states_waiting', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'jvm 死亡线程数量', 'jvm_threads_states_terminated', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The recent cpu usage for the Java Virtual Machine process', 'process_cpu_usage', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The maximum file descriptor count', 'process_files_max', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The open file descriptor count', 'process_files_open', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'Start time of the process since unix epoch', 'process_start_time', 'DOUBLE');
insert into horus_metrics_columns(metrics_code, column_name, column_code, column_type) values('appMonitor', 'The uptime of the Java virtual machine', 'process_uptime', 'DOUBLE');