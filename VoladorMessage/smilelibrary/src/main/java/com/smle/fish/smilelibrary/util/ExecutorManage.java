package com.smle.fish.smilelibrary.util;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;


/**
* 创建时间: 2020/4/9
* 创建者: yj
* 版本:
* 功能描述
*/
public class ExecutorManage {
    /**
     * 总共多少任务（根据CPU个数决定创建活动线程的个数,这样取的好处就是可以让手机承受得住）
     */
    private static final int count = Runtime.getRuntime().availableProcessors() * 3 + 2;

    /**
     * 每次只执行一个任务的线程池
     */
    public static ExecutorService singleTaskExecutor = null;

    /**
     * 每次执行限定个数个任务的线程池
     */
    public static ExecutorService limitedTaskExecutor = null;

    /**
     * 所有任务都一次性开始的线程池
     */
    public static ExecutorService allTaskExecutor = null;

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行
     */
    public static ExecutorService scheduledTaskExecutor = null;

    /**
     * 创建一个可在指定时间里执行任务的线程池，亦可重复执行（不同之处：使用工程模式）
     */
    public static ExecutorService scheduledTaskFactoryExecutor = null;

    private List<AsyncTask> mTaskList = null;

    /**
     * 任务是否被取消
     */
    private boolean isCancled = false;

    /**
     * 是否点击并取消任务标示符
     */
    private boolean isClick = false;

    /**
     * 线程工厂初始化方式一
     */
    private ThreadFactory tf = Executors.defaultThreadFactory();

    /**
     *
     */
    private static List<Future<Boolean>> futures = new ArrayList<>();

    static {
        singleTaskExecutor = Executors.newSingleThreadExecutor();// 每次只执行一个线程任务的线程池
        limitedTaskExecutor = Executors.newFixedThreadPool(4);// 限制线程池大小为5的线程池
        allTaskExecutor = Executors.newCachedThreadPool(); // 一个没有限制最大线程数的线程池
        scheduledTaskExecutor = Executors.newScheduledThreadPool(3);// 一个可以按指定时间可周期性的执行的线程池
        scheduledTaskFactoryExecutor = Executors.newFixedThreadPool(count);// 按指定工厂模式来执行的线程池
    }

    public static class OnlyRunnable implements Runnable {
        private static OnlyRunnable instance;

        @Override
        public void run() {

        }
    }

    /**
     * 添加任务
     *
     * @param runnable
     */
    public static void addAllTask(Runnable runnable) {
        allTaskExecutor.submit(runnable);
    }

    public static void allTaskListener(final IAllTaskListener iAllTaskListener) {
        singleTaskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (iAllTaskListener != null) {
                    allTaskExecutor.shutdown();
                    while (true) {
                        if (allTaskExecutor.isTerminated()) {
                            iAllTaskListener.allTaskComplete();
                            break;
                        }
                    }
                }
            }
        });


    }

    /**
     * 线程状态监听
     */
    public interface IAllTaskListener {
        void allTaskComplete();
    }

}
