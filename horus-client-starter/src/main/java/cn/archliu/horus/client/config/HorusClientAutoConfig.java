package cn.archliu.horus.client.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@ComponentScan(basePackages = { "cn.archliu.horus.client" })
@EnableFeignClients(basePackages = { "cn.archliu.horus.client.monitor.feign" })
public class HorusClientAutoConfig {

    /**
     * 默认情况下，在创建了线程池后，线程池中的线程数为0，当有任务来之后，就会创建一个线程去执行任务，
     * 当线程池中的线程数目达到corePoolSize后，就会把到达的任务放到缓存队列当中；
     * 当队列满了，就继续创建线程，当线程数量大于等于maxPoolSize后，开始使用拒绝策略拒绝
     */

    /** 核心线程数（默认线程数） */
    private static final int CORE_POOL_SIZE = 20;
    /** 最大线程数 */
    private static final int MAX_POOL_SIZE = 100;
    /** 允许线程空闲时间（单位：默认为秒） */
    private static final int KEEP_ALIVE_TIME = 10;
    /** 缓冲队列大小 */
    private static final int QUEUE_CAPACITY = 200;
    /** 线程池名前缀 */
    private static final String THREAD_NAME_PREFIX = "Horus-Mark-Async-";

    @Bean("markExecutor")
    public ThreadPoolTaskExecutor msgReachExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        // 线程池对拒绝任务的处理策略
        // CallerRunsPolicy：由调用线程（提交任务的线程）处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 初始化
        executor.initialize();
        return executor;
    }

}
