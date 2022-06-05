package cn.archliu.horus.client.plugins;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.MetricResponse;
import org.springframework.boot.actuate.metrics.MetricsEndpoint.Sample;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.archliu.horus.client.config.HorusClientProperties;
import cn.archliu.horus.client.metrics.entity.AppMonitor;
import cn.archliu.horus.client.metrics.enums.MetricsCodes;
import cn.archliu.horus.client.monitor.HorusEye;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import io.micrometer.core.instrument.Statistic;

/**
 * @Author: Arch
 * @Date: 2022-05-16 09:24:16
 * @Description: 基于 actuator 的应用监控插件
 */
@SuppressWarnings({ "squid:S3776" })
@Component("horusActuatorPlugin")
@ConditionalOnProperty(prefix = "horus.client", name = "monitor-app", havingValue = "true", matchIfMissing = false)
public class HorusActuatorPlugin {

    /** endPoint 标签 */
    private static final List<String> ID_MAPPER = ListUtil.toList("id:mapped");
    private static final List<String> ID_DIRECT = ListUtil.toList("id:direct");
    private static final List<String> META_MINOR = ListUtil.toList("cause:Metadata GC Threshold",
            "action:end of minor GC");
    private static final List<String> META_MAJOR = ListUtil.toList("cause:Metadata GC Threshold",
            "action:end of major GC");
    private static final List<String> AF_MINOR = ListUtil.toList("cause:Allocation Failure",
            "action:end of minor GC");
    private static final List<String> AF_MAJOR = ListUtil.toList("cause:Allocation Failure",
            "action:end of major GC");
    private static final List<String> HEAP_CCS = ListUtil.toList("area:heap", "id:Compressed Class Space");
    private static final List<String> HEAP_POG = ListUtil.toList("area:heap", "id:PS Old Gen");
    private static final List<String> HEAP_PSS = ListUtil.toList("area:heap", "id:PS Survivor Space");
    private static final List<String> HEAP_M = ListUtil.toList("area:heap", "id:Metaspace");
    private static final List<String> HEAP_PES = ListUtil.toList("area:heap", "id:PS Eden Space");
    private static final List<String> HEAP_CC = ListUtil.toList("area:heap", "id:Code Cache");
    private static final List<String> NONHEAP_CCS = ListUtil.toList("area:nonheap", "id:Compressed Class Space");
    private static final List<String> NONHEAP_POG = ListUtil.toList("area:nonheap", "id:PS Old Gen");
    private static final List<String> NONHEAP_PSS = ListUtil.toList("area:nonheap", "id:PS Survivor Space");
    private static final List<String> NONHEAP_M = ListUtil.toList("area:nonheap", "id:Metaspace");
    private static final List<String> NONHEAP_PES = ListUtil.toList("area:nonheap", "id:PS Eden Space");
    private static final List<String> NONHEAP_CC = ListUtil.toList("area:nonheap", "id:Code Cache");

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private HorusClientProperties horusClientProperties;

    @Autowired
    private MetricsEndpoint metricsEndpoint;

    @Autowired
    private HorusEye horusEye;

    /**
     * 5 秒中打点一次
     */
    @Scheduled(fixedDelay = 5000L)
    protected void mark() {
        AppMonitor appMonitor = new AppMonitor();
        // 应用相关数据组织
        genApplicationInfo(appMonitor);
        // JVM buffer 数据组织
        genJVMBufferInfo(appMonitor);
        // JVM GC 数据组织
        genJVMGCInfo(appMonitor);
        // JVM memory 数据组织
        genJVMMemoryInfo(appMonitor);
        // JVM 线程数据组织
        genJVMThreadInfo(appMonitor);
        // JVM 所在进程数据组织
        genProcessInfo(appMonitor);
        horusEye.mark(MetricsCodes.APP_MONITOR, appMonitor);
    }

    /**
     * 应用相关数据组织
     * 
     * @param appMonitor
     */
    private void genApplicationInfo(AppMonitor appMonitor) {
        appMonitor.setApplicationName(applicationName);
        appMonitor.setAppIp(horusClientProperties.getIp());
        appMonitor.setAppPort(horusClientProperties.getPort());
        Sample readyTime = getValue("application.ready.time", null);
        if (readyTime != null) {
            appMonitor.setAppReadyTime(readyTime.getValue());
        }
        Sample startedTime = getValue("application.started.time", null);
        if (startedTime != null) {
            appMonitor.setAppStartedTime(startedTime.getValue());
        }
    }

    /**
     * JVM buffer 数据组织
     * 
     * @param appMonitor
     */
    private void genJVMBufferInfo(AppMonitor appMonitor) {
        Sample bufferCount = getValue("jvm.buffer.count", null);
        if (bufferCount != null) {
            appMonitor.setJvmBuffer(bufferCount.getValue());
        }
        Sample mappedBufferCount = getValue("jvm.buffer.count", ID_MAPPER);
        if (mappedBufferCount != null) {
            appMonitor.setJvmMappedBuffer(mappedBufferCount.getValue());
        }
        Sample directBufferCount = getValue("jvm.buffer.count", ID_DIRECT);
        if (directBufferCount != null) {
            appMonitor.setJvmDirectBuffer(directBufferCount.getValue());
        }
        Sample bufferMemoryUsed = getValue("jvm.buffer.memory.used", null);
        if (bufferMemoryUsed != null) {
            appMonitor.setJvmBufferMemoryUsed(bufferMemoryUsed.getValue());
        }
        Sample mappedBufferMemoryUsed = getValue("jvm.buffer.memory.used", ID_MAPPER);
        if (mappedBufferMemoryUsed != null) {
            appMonitor.setJvmMappedBufferMemoryUsed(mappedBufferMemoryUsed.getValue());
        }
        Sample directBufferMemoryUsed = getValue("jvm.buffer.memory.used", ID_DIRECT);
        if (directBufferMemoryUsed != null) {
            appMonitor.setJvmDirectBufferMemoryUsed(directBufferMemoryUsed.getValue());
        }
        Sample bufferTotalCa = getValue("jvm.buffer.total.capacity", null);
        if (bufferTotalCa != null) {
            appMonitor.setJvmBufferTotalCapacity(bufferTotalCa.getValue());
        }
        Sample mappedBufferTotalCa = getValue("jvm.buffer.total.capacity", ID_MAPPER);
        if (mappedBufferTotalCa != null) {
            appMonitor.setJvmMappedBufferTotalCapacity(mappedBufferTotalCa.getValue());
        }
        Sample directBufferTotalCa = getValue("jvm.buffer.total.capacity", ID_DIRECT);
        if (directBufferTotalCa != null) {
            appMonitor.setJvmDirectBufferTotalCapacity(directBufferTotalCa.getValue());
        }
    }

    /**
     * JVM GC 数据组织
     * 
     * @param appMonitor
     */
    private void genJVMGCInfo(AppMonitor appMonitor) {
        Sample liveDataSize = getValue("jvm.gc.live.data.size", null);
        if (liveDataSize != null) {
            appMonitor.setJvmGcLiveDataSize(liveDataSize.getValue());
        }
        Sample maxDataSize = getValue("jvm.gc.max.data.size", null);
        if (maxDataSize != null) {
            appMonitor.setJvmGcMaxDataSize(maxDataSize.getValue());
        }
        Sample memoryAllocated = getValue("jvm.gc.memory.allocated", null);
        if (memoryAllocated != null) {
            appMonitor.setJvmGcMemoryAllocated(memoryAllocated.getValue());
        }
        Sample memoryPromoted = getCount("jvm.gc.memory.promoted", null);
        if (memoryPromoted != null) {
            appMonitor.setJvmGcMemoryPromoted(memoryPromoted.getValue());
        }
        Sample gcOverhead = getCount("jvm.gc.overhead", null);
        if (gcOverhead != null) {
            appMonitor.setJvmGcOverhead(gcOverhead.getValue());
        }
        MetricResponse gcPauseMetric = metricsEndpoint.metric("jvm.gc.pause", null);
        if (gcPauseMetric != null && CollUtil.isNotEmpty(gcPauseMetric.getMeasurements())) {
            gcPauseMetric.getMeasurements().stream().forEach(item -> {
                if (Statistic.COUNT.equals(item.getStatistic())) {
                    appMonitor.setJvmGcPauseCount(item.getValue());
                }
                if (Statistic.TOTAL_TIME.equals(item.getStatistic())) {
                    appMonitor.setJvmGcPauseTotalTime(item.getValue());
                }
                if (Statistic.MAX.equals(item.getStatistic())) {
                    appMonitor.setJvmgcPauseMax(item.getValue());
                }
            });
        }
        MetricResponse metaMinorMetric = metricsEndpoint.metric("jvm.gc.pause", META_MINOR);
        if (metaMinorMetric != null && CollUtil.isNotEmpty(metaMinorMetric.getMeasurements())) {
            metaMinorMetric.getMeasurements().stream().forEach(item -> {
                if (Statistic.COUNT.equals(item.getStatistic())) {
                    appMonitor.setJvmMinorGcPauseMgtCount(item.getValue());
                }
                if (Statistic.TOTAL_TIME.equals(item.getStatistic())) {
                    appMonitor.setJvmMinorGcPauseMgtTotalTime(item.getValue());
                }
                if (Statistic.MAX.equals(item.getStatistic())) {
                    appMonitor.setJvmMinorGcPauseMgtMax(item.getValue());
                }
            });
        }
        MetricResponse metaMajorMetric = metricsEndpoint.metric("jvm.gc.pause", META_MAJOR);
        if (metaMajorMetric != null && CollUtil.isNotEmpty(metaMajorMetric.getMeasurements())) {
            metaMajorMetric.getMeasurements().stream().forEach(item -> {
                if (Statistic.COUNT.equals(item.getStatistic())) {
                    appMonitor.setJvmMajorGcPauseMgtCount(item.getValue());
                }
                if (Statistic.TOTAL_TIME.equals(item.getStatistic())) {
                    appMonitor.setJvmMajorGcPauseMgtTotalTime(item.getValue());
                }
                if (Statistic.MAX.equals(item.getStatistic())) {
                    appMonitor.setJvmMajorGcPauseMgtMax(item.getValue());
                }
            });
        }
        MetricResponse afMinorMetric = metricsEndpoint.metric("jvm.gc.pause", AF_MINOR);
        if (afMinorMetric != null && CollUtil.isNotEmpty(afMinorMetric.getMeasurements())) {
            afMinorMetric.getMeasurements().stream().forEach(item -> {
                if (Statistic.COUNT.equals(item.getStatistic())) {
                    appMonitor.setJvmMinorGcPauseAfCount(item.getValue());
                }
                if (Statistic.TOTAL_TIME.equals(item.getStatistic())) {
                    appMonitor.setJvmMinorGcPauseAfTotalTime(item.getValue());
                }
                if (Statistic.MAX.equals(item.getStatistic())) {
                    appMonitor.setJvmMinorGcPauseAfMax(item.getValue());
                }
            });
        }
        MetricResponse afMajorMetric = metricsEndpoint.metric("jvm.gc.pause", AF_MAJOR);
        if (afMajorMetric != null && CollUtil.isNotEmpty(afMajorMetric.getMeasurements())) {
            afMajorMetric.getMeasurements().stream().forEach(item -> {
                if (Statistic.COUNT.equals(item.getStatistic())) {
                    appMonitor.setJvmMajorGcPauseAfCount(item.getValue());
                }
                if (Statistic.TOTAL_TIME.equals(item.getStatistic())) {
                    appMonitor.setJvmMajorGcPauseAfTotalTime(item.getValue());
                }
                if (Statistic.MAX.equals(item.getStatistic())) {
                    appMonitor.setJvmMajorGcPauseAfMax(item.getValue());
                }
            });
        }
    }

    /**
     * 组织 JVM 内存相关信息
     * 
     * @param appMonitor
     */
    private void genJVMMemoryInfo(AppMonitor appMonitor) {
        // todo 总的
        genJVMMemoryCommitted(appMonitor);
        genJVMMemoryMax(appMonitor);
        genJVMMemoryUsed(appMonitor);
        Sample memoryAfterGC = getCount("jvm.memory.usage.after.gc", null);
        if (memoryAfterGC != null) {
            appMonitor.setJvmMemoryUsageAfterGcHeapPool(memoryAfterGC.getValue());
        }
    }

    private void genJVMMemoryCommitted(AppMonitor appMonitor) {
        Sample memoryCommitted = getValue("jvm.memory.committed", null);
        if (memoryCommitted != null) {
            appMonitor.setJvmMemoryCommitted(memoryCommitted.getValue());
        }
        Sample heapCcs = getValue("jvm.memory.committed", HEAP_CCS);
        if (heapCcs != null) {
            appMonitor.setJvmMemoryCommittedHeapCcs(heapCcs.getValue());
        }
        Sample heapPog = getValue("jvm.memory.committed", HEAP_POG);
        if (heapPog != null) {
            appMonitor.setJvmMemoryCommittedHeapPog(heapPog.getValue());
        }
        Sample heapPss = getValue("jvm.memory.committed", HEAP_PSS);
        if (heapPss != null) {
            appMonitor.setJvmMemoryCommittedHeapPss(heapPss.getValue());
        }
        Sample heapM = getValue("jvm.memory.committed", HEAP_M);
        if (heapM != null) {
            appMonitor.setJvmMemoryCommittedHeapM(heapM.getValue());
        }
        Sample heapPes = getValue("jvm.memory.committed", HEAP_PES);
        if (heapPes != null) {
            appMonitor.setJvmMemoryCommittedHeapPes(heapPes.getValue());
        }
        Sample heapCc = getValue("jvm.memory.committed", HEAP_CC);
        if (heapCc != null) {
            appMonitor.setJvmMemoryCommittedHeapCc(heapCc.getValue());
        }
        Sample nonHeapCcs = getValue("jvm.memory.committed", NONHEAP_CCS);
        if (nonHeapCcs != null) {
            appMonitor.setJvmMemoryCommittedNonheapCcs(nonHeapCcs.getValue());
        }
        Sample nonHeapPog = getValue("jvm.memory.committed", NONHEAP_POG);
        if (nonHeapPog != null) {
            appMonitor.setJvmMemoryCommittedNonheapPog(nonHeapPog.getValue());
        }
        Sample nonHeapPss = getValue("jvm.memory.committed", NONHEAP_PSS);
        if (nonHeapPss != null) {
            appMonitor.setJvmMemoryCommittedNonheapPss(nonHeapPss.getValue());
        }
        Sample nonHeapM = getValue("jvm.memory.committed", NONHEAP_M);
        if (nonHeapM != null) {
            appMonitor.setJvmMemoryCommittedNonheapM(nonHeapM.getValue());
        }
        Sample nonHeapPes = getValue("jvm.memory.committed", NONHEAP_PES);
        if (nonHeapPes != null) {
            appMonitor.setJvmMemoryCommittedNonheapPes(nonHeapPes.getValue());
        }
        Sample nonHeapCc = getValue("jvm.memory.committed", NONHEAP_CC);
        if (nonHeapCc != null) {
            appMonitor.setJvmMemoryCommittedNonheapCc(nonHeapCc.getValue());
        }
    }

    private void genJVMMemoryMax(AppMonitor appMonitor) {
        Sample memoryMax = getValue("jvm.memory.max", null);
        if (memoryMax != null) {
            appMonitor.setJvmMemoryMax(memoryMax.getValue());
        }
        Sample heapCcs = getValue("jvm.memory.max", HEAP_CCS);
        if (heapCcs != null) {
            appMonitor.setJvmMemoryMaxHeapCcs(heapCcs.getValue());
        }
        Sample heapPog = getValue("jvm.memory.max", HEAP_POG);
        if (heapPog != null) {
            appMonitor.setJvmMemoryMaxHeapPog(heapPog.getValue());
        }
        Sample heapPss = getValue("jvm.memory.max", HEAP_PSS);
        if (heapPss != null) {
            appMonitor.setJvmMemoryMaxHeapPss(heapPss.getValue());
        }
        Sample heapM = getValue("jvm.memory.max", HEAP_M);
        if (heapM != null) {
            appMonitor.setJvmMemoryMaxHeapM(heapM.getValue());
        }
        Sample heapPes = getValue("jvm.memory.max", HEAP_PES);
        if (heapPes != null) {
            appMonitor.setJvmMemoryMaxHeapPes(heapPes.getValue());
        }
        Sample heapCc = getValue("jvm.memory.max", HEAP_CC);
        if (heapCc != null) {
            appMonitor.setJvmMemoryMaxHeapCc(heapCc.getValue());
        }
        Sample nonHeapCcs = getValue("jvm.memory.max", NONHEAP_CCS);
        if (nonHeapCcs != null) {
            appMonitor.setJvmMemoryMaxNonheapCcs(nonHeapCcs.getValue());
        }
        Sample nonHeapPog = getValue("jvm.memory.max", NONHEAP_POG);
        if (nonHeapPog != null) {
            appMonitor.setJvmMemoryMaxNonheapPog(nonHeapPog.getValue());
        }
        Sample nonHeapPss = getValue("jvm.memory.max", NONHEAP_PSS);
        if (nonHeapPss != null) {
            appMonitor.setJvmMemoryMaxNonheapPss(nonHeapPss.getValue());
        }
        Sample nonHeapM = getValue("jvm.memory.max", NONHEAP_M);
        if (nonHeapM != null) {
            appMonitor.setJvmMemoryMaxNonheapM(nonHeapM.getValue());
        }
        Sample nonHeapPes = getValue("jvm.memory.max", NONHEAP_PES);
        if (nonHeapPes != null) {
            appMonitor.setJvmMemoryMaxNonheapPes(nonHeapPes.getValue());
        }
        Sample nonHeapCc = getValue("jvm.memory.max", NONHEAP_CC);
        if (nonHeapCc != null) {
            appMonitor.setJvmMemoryMaxNonheapCc(nonHeapCc.getValue());
        }
    }

    private void genJVMMemoryUsed(AppMonitor appMonitor) {
        Sample memoryUsed = getValue("jvm.memory.used", null);
        if (memoryUsed != null) {
            appMonitor.setJvmMemoryUsed(memoryUsed.getValue());
        }
        Sample heapCcs = getValue("jvm.memory.used", HEAP_CCS);
        if (heapCcs != null) {
            appMonitor.setJvmMemoryUsedHeapCcs(heapCcs.getValue());
        }
        Sample heapPog = getValue("jvm.memory.used", HEAP_POG);
        if (heapPog != null) {
            appMonitor.setJvmMemoryUsedHeapPog(heapPog.getValue());
        }
        Sample heapPss = getValue("jvm.memory.used", HEAP_PSS);
        if (heapPss != null) {
            appMonitor.setJvmMemoryUsedHeapPss(heapPss.getValue());
        }
        Sample heapM = getValue("jvm.memory.used", HEAP_M);
        if (heapM != null) {
            appMonitor.setJvmMemoryUsedHeapM(heapM.getValue());
        }
        Sample heapPes = getValue("jvm.memory.used", HEAP_PES);
        if (heapPes != null) {
            appMonitor.setJvmMemoryUsedHeapPes(heapPes.getValue());
        }
        Sample heapCc = getValue("jvm.memory.used", HEAP_CC);
        if (heapCc != null) {
            appMonitor.setJvmMemoryUsedHeapCc(heapCc.getValue());
        }
        Sample nonHeapCcs = getValue("jvm.memory.used", NONHEAP_CCS);
        if (nonHeapCcs != null) {
            appMonitor.setJvmMemoryUsedNonheapCcs(nonHeapCcs.getValue());
        }
        Sample nonHeapPog = getValue("jvm.memory.used", NONHEAP_POG);
        if (nonHeapPog != null) {
            appMonitor.setJvmMemoryUsedNonheapPog(nonHeapPog.getValue());
        }
        Sample nonHeapPss = getValue("jvm.memory.used", NONHEAP_PSS);
        if (nonHeapPss != null) {
            appMonitor.setJvmMemoryUsedNonheapPss(nonHeapPss.getValue());
        }
        Sample nonHeapM = getValue("jvm.memory.used", NONHEAP_M);
        if (nonHeapM != null) {
            appMonitor.setJvmMemoryUsedNonheapM(nonHeapM.getValue());
        }
        Sample nonHeapPes = getValue("jvm.memory.used", NONHEAP_PES);
        if (nonHeapPes != null) {
            appMonitor.setJvmMemoryUsedNonheapPes(nonHeapPes.getValue());
        }
        Sample nonHeapCc = getValue("jvm.memory.used", NONHEAP_CC);
        if (nonHeapCc != null) {
            appMonitor.setJvmMemoryUsedNonheapCc(nonHeapCc.getValue());
        }
    }

    /**
     * 组织 JVM 线程相关信息
     * 
     * @param appMonitor
     */
    private void genJVMThreadInfo(AppMonitor appMonitor) {
        Sample peakThreads = getValue("jvm.threads.peak", null);
        if (peakThreads != null) {
            appMonitor.setJvmThreadsPeak(peakThreads.getValue());
        }
        Sample daemonThreads = getValue("jvm.threads.daemon", null);
        if (daemonThreads != null) {
            appMonitor.setJvmThreadsDaemon(daemonThreads.getValue());
        }
        Sample liveThreads = getValue("jvm.threads.live", null);
        if (liveThreads != null) {
            appMonitor.setJvmThreadsLive(liveThreads.getValue());
        }
        Sample timedWaitingThreads = getValue("jvm.threads.states", ListUtil.toList("state:timed-waiting"));
        if (timedWaitingThreads != null) {
            appMonitor.setJvmThreadsStatesTimedWaiting(timedWaitingThreads.getValue());
        }
        Sample newThreads = getValue("jvm.threads.states", ListUtil.toList("state:new"));
        if (newThreads != null) {
            appMonitor.setJvmThreadsStatesNew(newThreads.getValue());
        }
        Sample runnableThreads = getValue("jvm.threads.states", ListUtil.toList("state:runnable"));
        if (runnableThreads != null) {
            appMonitor.setJvmThreadsStatesRunnable(runnableThreads.getValue());
        }
        Sample blockedThreads = getValue("jvm.threads.states", ListUtil.toList("state:blocked"));
        if (blockedThreads != null) {
            appMonitor.setJvmThreadsStatesBlocked(blockedThreads.getValue());
        }
        Sample waitingThreads = getValue("jvm.threads.states", ListUtil.toList("state:waiting"));
        if (waitingThreads != null) {
            appMonitor.setJvmThreadsStatesWaiting(waitingThreads.getValue());
        }
        Sample terminatedThreads = getValue("jvm.threads.states", ListUtil.toList("state:terminated"));
        if (terminatedThreads != null) {
            appMonitor.setJvmThreadsStatesTerminated(terminatedThreads.getValue());
        }
    }

    /**
     * 组织 JVM 进程相关信息
     * 
     * @param appMonitor
     */
    private void genProcessInfo(AppMonitor appMonitor) {
        Sample processCpuUsage = getValue("process.cpu.usage", null);
        if (processCpuUsage != null) {
            appMonitor.setProcessCpuUsage(processCpuUsage.getValue());
        }
        Sample processFilesMax = getValue("process.files.max", null);
        if (processFilesMax != null) {
            appMonitor.setProcessFilesMax(processFilesMax.getValue());
        }
        Sample processFilesOpen = getValue("process.files.open", null);
        if (processFilesOpen != null) {
            appMonitor.setProcessFilesOpen(processFilesOpen.getValue());
        }
        Sample processStartTime = getValue("process.start.time", null);
        if (processStartTime != null) {
            appMonitor.setProcessStartTime(processStartTime.getValue());
        }
        Sample processUptime = getValue("process.uptime", null);
        if (processUptime != null) {
            appMonitor.setProcessUptime(processUptime.getValue());
        }
    }

    /**
     * 获取 VALUE 值
     * 
     * @param metricName
     * @return
     */
    private Sample getValue(String metricName, List<String> tags) {
        return getSample(metricName, tags, Statistic.VALUE);
    }

    /**
     * 获取 COUNT 值
     * 
     * @param metricName
     * @return
     */
    private Sample getCount(String metricName, List<String> tags) {
        return getSample(metricName, tags, Statistic.COUNT);
    }

    private Sample getSample(String metricName, List<String> tags, Statistic statistic) {
        MetricResponse readyMetric = metricsEndpoint.metric(metricName, tags);
        if (readyMetric != null) {
            List<Sample> measurements = readyMetric.getMeasurements();
            if (CollUtil.isNotEmpty(measurements)) {
                Optional<Sample> findFirst = measurements.stream()
                        .filter(item -> statistic.equals(item.getStatistic()))
                        .findFirst();
                return findFirst.isPresent() ? findFirst.get() : null;
            }
        }
        return null;
    }

}
