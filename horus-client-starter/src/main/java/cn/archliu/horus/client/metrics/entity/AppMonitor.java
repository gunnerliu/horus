package cn.archliu.horus.client.metrics.entity;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@Accessors(chain = true)
public class AppMonitor {

    /** 打点时间 */
    private Timestamp markingTime = new Timestamp(System.currentTimeMillis());

    /** 链路ID */
    private String traceId;

    /** 链路节点ID */
    private String spanId;

    /** 应用名 */
    private String applicationName;

    /** 应用IP */
    private String appIp;

    /** 应用端口 */
    private Integer appPort;

    /** 应用准备时间 */
    private Double appReadyTime;

    /** 应用运行时间 */
    private Double appStartedTime;

    /** JVM所有缓冲区数量 */
    private Double jvmBuffer;

    /** JVM缓冲区数量 */
    private Double jvmMappedBuffer;

    /** 直接内存缓冲区数量 */
    private Double jvmDirectBuffer;

    /** JVM所有缓冲区内存使用 */
    private Double jvmBufferMemoryUsed;

    /** JVM缓冲区内存使用 */
    private Double jvmMappedBufferMemoryUsed;

    /** 直接内存缓冲区内存使用 */
    private Double jvmDirectBufferMemoryUsed;

    /** 缓冲区总容量 */
    private Double jvmBufferTotalCapacity;

    /** JVM缓冲区总容量 */
    private Double jvmMappedBufferTotalCapacity;

    /** 直接内存缓冲区总容量 */
    private Double jvmDirectBufferTotalCapacity;

    /** 回收后长寿命堆内存池大小 */
    private Double jvmGcLiveDataSize;

    /** 长寿命堆内存池最大大小 */
    private Double jvmGcMaxDataSize;

    /** JVM 已分配内存 */
    private Double jvmGcMemoryAllocated;

    /** JVM GC之后内存扩张 */
    private Double jvmGcMemoryPromoted;

    /** JVM GC 所占用的 CPU */
    private Double jvmGcOverhead;

    /** gc 暂停次数 */
    private Double jvmGcPauseCount;

    /** gc 暂停总时间 */
    private Double jvmGcPauseTotalTime;

    /** gc 暂停最长时间 */
    private Double jvmgcPauseMax;

    /** 元空间 minor gc 次数 */
    private Double jvmMinorGcPauseMgtCount;

    /** 元空间 minor gc 总时间 */
    private Double jvmMinorGcPauseMgtTotalTime;

    /** 元空间 minor gc 最长时间 */
    private Double jvmMinorGcPauseMgtMax;

    /** 内存分配失败 minor gc 次数 */
    private Double jvmMinorGcPauseAfCount;

    /** 内存分配失败 minor gc 总时间 */
    private Double jvmMinorGcPauseAfTotalTime;

    /** 内存分配失败 minor gc 最长时间 */
    private Double jvmMinorGcPauseAfMax;

    /** 元空间 minor gc 次数 */
    private Double jvmMajorGcPauseMgtCount;

    /** 元空间 minor gc 总时间 */
    private Double jvmMajorGcPauseMgtTotalTime;

    /** 元空间 minor gc 最长时间 */
    private Double jvmMajorGcPauseMgtMax;

    /** 内存分配失败 minor gc 次数 */
    private Double jvmMajorGcPauseAfCount;

    /** 内存分配失败 minor gc 总时间 */
    private Double jvmMajorGcPauseAfTotalTime;

    /** 内存分配失败 minor gc 最长时间 */
    private Double jvmMajorGcPauseAfMax;

    /** JVM 申请的内存 */
    private Double jvmMemoryCommitted;

    /** heap-Compressed Class Space-jvm.memory.committed */
    private Double jvmMemoryCommittedHeapCcs;

    /** heap-PS Old Gen-jvm.memory.committed */
    private Double jvmMemoryCommittedHeapPog;

    /** heap-PS Survivor Space-jvm.memory.committed */
    private Double jvmMemoryCommittedHeapPss;

    /** heap-Metaspace-jvm.memory.committed */
    private Double jvmMemoryCommittedHeapM;

    /** heap-PS Eden Space-jvm.memory.committed */
    private Double jvmMemoryCommittedHeapPes;

    /** heap-Code Cache-jvm.memory.committed */
    private Double jvmMemoryCommittedHeapCc;

    /** nonheap-Compressed Class Space-jvm.memory.committed */
    private Double jvmMemoryCommittedNonheapCcs;

    /** nonheap-PS Old Gen-jvm.memory.committed */
    private Double jvmMemoryCommittedNonheapPog;

    /** nonheap-PS Survivor Space-jvm.memory.committed */
    private Double jvmMemoryCommittedNonheapPss;

    /** nonheap-Metaspace-jvm.memory.committed */
    private Double jvmMemoryCommittedNonheapM;

    /** nonheap-PS Eden Space-jvm.memory.committed */
    private Double jvmMemoryCommittedNonheapPes;

    /** nonheap-Code Cache-jvm.memory.committed */
    private Double jvmMemoryCommittedNonheapCc;

    /** JVM 可以使用的最大内存 */
    private Double jvmMemoryMax;

    /** heap-Compressed Class Space-jvm.memory.max */
    private Double jvmMemoryMaxHeapCcs;

    /** heap-PS Old Gen-jvm.memory.max */
    private Double jvmMemoryMaxHeapPog;

    /** heap-PS Survivor Space-jvm.memory.max */
    private Double jvmMemoryMaxHeapPss;

    /** heap-Metaspace-jvm.memory.max */
    private Double jvmMemoryMaxHeapM;

    /** heap-PS Eden Space-jvm.memory.max */
    private Double jvmMemoryMaxHeapPes;

    /** heap-Code Cache-jvm.memory.max */
    private Double jvmMemoryMaxHeapCc;

    /** nonheap-Compressed Class Space-jvm.memory.max */
    private Double jvmMemoryMaxNonheapCcs;

    /** nonheap-PS Old Gen-jvm.memory.max */
    private Double jvmMemoryMaxNonheapPog;

    /** nonheap-PS Survivor Space-jvm.memory.max */
    private Double jvmMemoryMaxNonheapPss;

    /** nonheap-Metaspace-jvm.memory.max */
    private Double jvmMemoryMaxNonheapM;

    /** nonheap-PS Eden Space-jvm.memory.max */
    private Double jvmMemoryMaxNonheapPes;

    /** nonheap-Code Cache-jvm.memory.max */
    private Double jvmMemoryMaxNonheapCc;

    /** heap-Compressed Class Space-jvm.memory.used */
    private Double jvmMemoryUsedHeapCcs;

    /** JVM 已使用的内存 */
    private Double jvmMemoryUsed;

    /** heap-PS Old Gen-jvm.memory.used */
    private Double jvmMemoryUsedHeapPog;

    /** heap-PS Survivor Space-jvm.memory.used */
    private Double jvmMemoryUsedHeapPss;

    /** heap-Metaspace-jvm.memory.used */
    private Double jvmMemoryUsedHeapM;

    /** heap-PS Eden Space-jvm.memory.used */
    private Double jvmMemoryUsedHeapPes;

    /** heap-Code Cache-jvm.memory.used */
    private Double jvmMemoryUsedHeapCc;

    /** nonheap-Compressed Class Space-jvm.memory.used */
    private Double jvmMemoryUsedNonheapCcs;

    /** nonheap-PS Old Gen-jvm.memory.used */
    private Double jvmMemoryUsedNonheapPog;

    /** nonheap-PS Survivor Space-jvm.memory.used */
    private Double jvmMemoryUsedNonheapPss;

    /** nonheap-Metaspace-jvm.memory.used */
    private Double jvmMemoryUsedNonheapM;

    /** nonheap-PS Eden Space-jvm.memory.used */
    private Double jvmMemoryUsedNonheapPes;

    /** nonheap-Code Cache-jvm.memory.used */
    private Double jvmMemoryUsedNonheapCc;

    /** GC之后 long-lived heap pool 使用的百分比 */
    private Double jvmMemoryUsageAfterGc;

    /**
     * The percentage of long-lived heap pool used after the last GC event, in the
     * range [0..1]
     */
    private Double jvmMemoryUsageAfterGcHeapPool;

    /**
     * The peak live thread count since the Java virtual machine started or peak was
     * reset
     */
    private Double jvmThreadsPeak;

    /** The current number of live daemon threads */
    private Double jvmThreadsDaemon;

    /**
     * The current number of live threads including both daemon and non-daemon
     * threads
     */
    private Double jvmThreadsLive;

    /** jvm timed_waiting 线程数量 */
    private Double jvmThreadsStatesTimedWaiting;

    /** jvm 新建线程数量 */
    private Double jvmThreadsStatesNew;

    /** jvm 正在运行线程数量 */
    private Double jvmThreadsStatesRunnable;

    /** jvm 阻塞线程数量 */
    private Double jvmThreadsStatesBlocked;

    /** jvm 等待线程数量 */
    private Double jvmThreadsStatesWaiting;

    /** jvm 死亡线程数量 */
    private Double jvmThreadsStatesTerminated;

    /** The recent cpu usage for the Java Virtual Machine process */
    private Double processCpuUsage;

    /** The maximum file descriptor count */
    private Double processFilesMax;

    /** The open file descriptor count */
    private Double processFilesOpen;

    /** Start time of the process since unix epoch */
    private Double processStartTime;

    /** The uptime of the Java virtual machine */
    private Double processUptime;

}
