package ir.tahasystem.music.app.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ConnectionThreadPoolSingle {

    private final int CORE = 1;
    private final int MAX = 1;
    private final int TIMEOUT=30;

    private  ThreadPoolExecutor executorPool;
    private MyMonitorThreadSingle monitor;

    private static Object mutex = new Object();
    private static ConnectionThreadPoolSingle aConnectionThreadPool = null;


    public static ConnectionThreadPoolSingle getInstance() {
        if (aConnectionThreadPool == null)
            synchronized (mutex) {
                if (aConnectionThreadPool == null) {
                    aConnectionThreadPool = new ConnectionThreadPoolSingle();
                } else {
                    return aConnectionThreadPool;
                }

            }
        return aConnectionThreadPool;
    }

    private ConnectionThreadPoolSingle() {
        init();
    }


    public  void init (){

        RejectedExecutionHandlerImplSingle rejectionHandler = new RejectedExecutionHandlerImplSingle();

        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        executorPool = new ThreadPoolExecutor(CORE, MAX, TIMEOUT, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100),
                threadFactory, rejectionHandler);

        monitor = new MyMonitorThreadSingle(executorPool, 5);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();

        executorPool.prestartAllCoreThreads();

    }

    public void AddTask(Runnable aRunnable) {
        if (executorPool == null)
            init();

        executorPool.execute(aRunnable);

    }

    public void AddQueue(Runnable aRunnable) {

        if (executorPool == null)
            init();

        try {
            executorPool.getQueue().put(aRunnable);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Future<StringBuilder> AddCallable(Callable<StringBuilder> aCallable) {

        if (executorPool == null)
            init();

        return executorPool.submit(aCallable);
    }

    public void ShutDown() {

        if (executorPool != null)
            executorPool.shutdown();
        if (monitor != null)
            monitor.shutdown();

        //executorPool=null;
        //monitor=null;
        //ConnectionThreadPool=null;

        System.out.println("#####Finished all threads#####");


    }

    public void ResetThreadPoolExecutor(int core) {

        executorPool.setCorePoolSize(core);
        executorPool.setMaximumPoolSize(core);

    }

}
