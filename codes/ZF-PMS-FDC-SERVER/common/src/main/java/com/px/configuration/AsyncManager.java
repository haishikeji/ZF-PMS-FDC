package com.px.configuration;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.google.common.cache.Cache;
import com.px.enums.CodeMsgEnumInterface;
import com.px.exception.ErrorCode;
import com.px.utils.LoginContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 异步任务管理器
 *
 * @author 品讯科技
 */
@Component
@Slf4j
public class AsyncManager {

    private static ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private static Cache<String, Object> lockCache;



    @Autowired
    @Qualifier("mainThreadPool")
    public void setThreadPoolTaskExecutor(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        AsyncManager.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Autowired
    @Qualifier("lockCache")
    public void setLockCache(Cache<String, Object> cache) {
        AsyncManager.lockCache = cache;
    }

    public static Cache<String, Object> getLockCache() {
        return lockCache;
    }

    public static ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return threadPoolTaskExecutor;
    }

    /**
     * 单例模式
     */
    private AsyncManager() {
    }


    private static AsyncManager me = new AsyncManager();

    public static AsyncManager me() {
        return me;
    }

    public static AsyncManager getInstance() {
        return me;
    }


    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        threadPoolTaskExecutor.execute(task);
    }


    /**
     * 执行任务 延迟调用
     *
     * @param task
     * @param millis 延迟时间 毫秒
     */
    public void execute(Runnable task, long millis) {
        threadPoolTaskExecutor.execute(() -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.run();
        });
    }


    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        threadPoolTaskExecutor.execute(task);
    }

    /**
     * 并发执行一组任务,并且等待所有的任务执行完成才返回结果
     * 一个param 对应 一个业务函数 function (param为function的入参)
     * 需要保证 params[i] 为 functions[i] 的入参
     * 执行完后将返回一个<strong>有序</strong>的结果集合
     * 入参的顺序和返回的结果顺序一致
     * 若没入参可以给Null  函数若没有返回 也将返回对应的Null
     * 注意: 并发任务数不能超过<strong>50</strong>
     * todo 后续可以给超时时间 可取消任务 等优化
     *
     * @param params    参数组
     * @param functions 需要并发的业务函数组
     * @return List<Object> 有序的结果组
     */
    public List<Object> executeConcurrentGroupTask(List<Object> params, List<Function<Object, Object>> functions) {
        assert CollUtil.isNotEmpty(params) && CollUtil.isNotEmpty(functions);
        assert params.size() == functions.size();
        // 不让开过多的任务数
        assert functions.size() <= 50;

        List<CompletableFuture> completableFutureList = new ArrayList<>();
        Object[] rescontainer = new Object[params.size()];

        for (int i = 0; i < params.size(); i++) {
            Object param = params.get(i);
            int finalI = i;
            Function<Object, Object> objectObjectFunction = functions.get(i);
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                Object apply = objectObjectFunction.apply(param);
                System.out.println(finalI);
                rescontainer[finalI] = apply;
            }, threadPoolTaskExecutor);
            completableFutureList.add(voidCompletableFuture);
        }

        CompletableFuture[] cfsArrays = ArrayUtil.toArray(completableFutureList, CompletableFuture.class);
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(cfsArrays);

        try {
            //阻塞 等待所有任务完成
            allFutures.get();
        } catch (InterruptedException e) {
            log.error(" 并发执行任务组 执行出错 线程被打断InterruptedException e {}", e.getMessage());
        } catch (ExecutionException e) {
            log.error(" 并发执行任务组 执行出错 ExecutionException e{} ", e.getMessage());
        }

        return Arrays.asList(rescontainer);

    }

    /**
     * 执行一个将来的任务
     *
     * @param task 任务
     */
    public Future getFuture(Callable task) {
        return threadPoolTaskExecutor.submit(task);
    }

    /**
     * 执行将来的任务 并阻塞 然后返回结果
     *
     * @param task 任务
     */
    public <v> v getFutureResult(Callable<v> task) throws ExecutionException, InterruptedException {
        Future<v> submit = threadPoolTaskExecutor.submit(task);
        return submit.get();
    }

    /**
     * 执行任务 并阻塞 然后返回结果 可以设置超时时间
     *
     * @param task 任务
     */
    public <v> v getFutureResult(Callable<v> task, long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
        Future<v> submit = threadPoolTaskExecutor.submit(task);
        return submit.get(timeout, unit);
    }


    /**
     * 停止任务线程池
     */
    public void shutdown() {
        threadPoolTaskExecutor.shutdown();
    }

    /**
     * 执行自定义任务
     *
     * @param bizHandler
     */
    public void execute(BizHandler bizHandler) {
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                bizHandler.doCustom();
            }
        });
    }


    /**
     * @param bizHandler
     * @param asyncLockStrategy
     * @param asyncBiz
     * @see AsyncLockStrategy
     * @see AsyncBiz
     */
    public void execute(BizHandler bizHandler, AsyncLockStrategy asyncLockStrategy, AsyncBiz asyncBiz) {
        String prefix = "async_lock_";
        StringBuilder sb = new StringBuilder();
        String key = null;
        Long userId = LoginContext.getLoginCacheUserBO().getId();
        switch (asyncLockStrategy) {
            case LOCK_BIZ:
                key = prefix + asyncBiz.getCode();
                break;
            case LOCK_USER:
                if (userId == null) {
                    return;
                }
                key = prefix + userId;
                break;
            case LOCK_USER_BIZ:
                if (userId == null) {
                    return;
                }
                key = prefix + userId + "_" + asyncBiz.getCode();
                break;
        }
        if (lockCache.getIfPresent(key) != null) {
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR, asyncLockStrategy.tips);
        }

        lockCache.put(key, System.currentTimeMillis());
        sb.append(key);
        threadPoolTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (bizHandler.validBefore()){
                        bizHandler.doCustom();
                    }
                } catch (Exception e) {
                    log.error("异步任务异常", e);
                } finally {
                    lockCache.invalidate(sb.toString());
                }

            }
        });
    }

    private synchronized void checkAndLock(AsyncLockStrategy asyncLockStrategy, AsyncBiz asyncBiz) {
        String prefix = "async_lock_";
        String key = null;
        Long userId = LoginContext.getLoginCacheUserBO().getId();
        if (userId == null) {
            return;
        }
        switch (asyncLockStrategy) {
            case LOCK_BIZ:
                key = prefix + asyncBiz.getCode();
                break;
            case LOCK_USER:
                key = prefix + userId;
                break;
            case LOCK_USER_BIZ:
                key = prefix + userId + "_" + asyncBiz.getCode();
                break;
            default:
                break;
        }

        if (lockCache.getIfPresent(key) != null) {
            ErrorCode.throwBusinessException(ErrorCode.CUSTOM_ERROR, asyncLockStrategy.tips);
        }

        lockCache.put(key, System.currentTimeMillis());
    }

    /**
     * 异步业务类型
     */
    public enum AsyncBiz implements CodeMsgEnumInterface<String, String> {
        BROKERAGE_SETTLE("BROKERAGE_SETTLE","提成结算"),

        PACKAGING_EVALUATE_REMAIN("PACKAGING_EVALUATE_REMAIN","打包评价提醒消息")
        ;

        private String key;
        private String desc;

        AsyncBiz(String key, String desc) {
            this.key = key;
            this.desc = desc;
        }

        @Override
        public String getCode() {
            return key;
        }

        @Override
        public String getMsg() {
            return desc;
        }
    }

    /**
     * 异步锁策略
     */
    public enum AsyncLockStrategy implements CodeMsgEnumInterface<String, String> {
        LOCK_BIZ("全局锁业务", "全局等待该业务", "正在执行中,请耐心等待后再尝试!"),
        LOCK_USER("锁用户", "该用户只能提交一个异步任务", "您还有其他任务执行，请等待！"),
        LOCK_USER_BIZ("锁用户的业务", "该用户该业务只能提交一个异步任务", "正在努力执行中，请勿重复提交!"),
        ;

        private String strategy;
        private String msg;
        private String tips;

        AsyncLockStrategy(String strategy, String msg) {
            this.strategy = strategy;
            this.msg = msg;
        }

        AsyncLockStrategy(String strategy, String msg, String tips) {
            this.strategy = strategy;
            this.msg = msg;
            this.tips = tips;
        }

        @Override
        public String getCode() {
            return strategy;
        }

        @Override
        public String getMsg() {
            return msg;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }
    }

    public interface BizHandler {
        /**
         * 前序校验
         * 在异步任务之前
         */
        Boolean validBefore();

        Object doCustom();
    }
}
