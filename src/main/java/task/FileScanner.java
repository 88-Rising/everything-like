package task;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileScanner {
/*
* 1.核心线程数：始终运行的线程数量
* 2.最大线程数：有新任务，并且当前运行线程数小于最大线程数，会创建新的线程来处理任务
* 3-4.超过3这个数量，4这个时间单位，2-1（最大线程数-核心线程数）这些线程数就会关闭
* 相当于正式工加上临时工
* 5.工作的阻塞队列：能存放的最大任务数（交给线程处理的任务）
* 6.如果超出工作队列的长度，任务要进行处理的方式
*     1.CallerRunPolicy:谁指派的任务让谁自己去执行
*     2.AbortPolicy：抛出异常
*     3.Discardpolicy:对其最新的任务
*     4.DiscardOldPolicy:对其最老的任务
* */
//    private ThreadPoolExecutor pool= new ThreadPoolExecutor(3,3,0,TimeUnit.MICROSECONDS
//         ,new LinkedBlockingQueue<>(),
//          new ThreadPoolExecutor.AbortPolicy()
//    );
    private ExecutorService pool=Executors.newFixedThreadPool(4);
    //快捷创建线程池的方式
//    private ExecutorService exe=Executors.newFixedThreadPool(4);
    //计数器 不传入数值 表示初始值为0
    private volatile AtomicInteger count=new AtomicInteger();

    //线程等待的锁对象
    private Object lock=new Object();//1,使用加锁进行等待
    private CountDownLatch latch=new CountDownLatch(1);//2.使用await()阻塞等待
    private Semaphore semaphore=new Semaphore(0);//3.acquire()阻塞等待

    private ScanCallback callback;

    public FileScanner(ScanCallback callback) {
        this.callback=callback;
    }

    //扫描文件目录
    public void scan(String path){
    //递归扫描 多级任务进行扫描
        count.incrementAndGet();//计数器++i 启动根目录扫描任务
     doScan(new File(path));

    }

    private void doScan(File dir){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    callback.callback(dir);
                    File[] children = dir.listFiles();//下一级文件和文件夹
                    if (children != null) {
                        for (File child : children) {
                            if (child.isDirectory()) {//如果是文件夹递归处理
//                                System.out.println("文件夹" + child.getParent());
                                count.incrementAndGet();//计数器++i 启动子文件夹扫描任
                                System.out.println("当前任务数："+count.get());
                                doScan(child);
                            }
                        }
                            // else {//如果是文件，待做的工作
                            //  //TODO
                            ////   System.out.println("文件" + child.getParent());
                //           }
                    }

                }finally {//保证线程计数不管是否出现异常 都能进行-1操作
                    int r=count.decrementAndGet();//减操作
                    if(r==0){
                        //第一种实现方式
//                        synchronized (lock){
//                            lock.notify();
//                        }
                        //第二种实现
//                        latch.countDown();
                        //第三种实现
                        semaphore.release();
                    }
                }

            }
        });

    }

    //等待scan方法结束
    /*
    * 多线程任务等待：Thread.start();
    * 1.join():需要使用线程Thread类的引用对象
    * 2.wait();线程间的等待
    *
    * */
    public void waitFinish() throws InterruptedException {
      //第一种实现
//   synchronized (lock){
//       lock.wait();
//   }
      //第二种实现
//      latch.await();
      //第三种是实现
        try {
            semaphore.acquire();
        }finally {
            //阻塞等待直到任务完成 完成之后需要关闭线程池
            shutdown();
        }


    }

    /*
    * 关闭线程池
    * */
    public void shutdown(){
        System.out.println("关闭线程池....");
//      pool.shutdown(); //内部实现原理：通过内部Thread.interrupt()来中断
        //shutdown():表达的含义是：新传入的任务不再接收了但是现在目前的任务（所有线程所执行的任务加上工作队的任务）还要执行完毕

      pool.shutdownNow(); //通过Thread.stop()来停止线程
        //新传入的任务shuntdownNow也不再接收了 目前任务（所有线程中执行的任务）判断是否能够停止，如果可以停止就结束任，如果不能就执行完再停止
          //工作队列中的任务是直接丢弃的
    }

    public static void main(String[] args) throws InterruptedException {
//        Thread t=new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println(Thread.currentThread().getName());//后执行
//            }
//        });
//        t.start();
//        System.out.println(Thread.currentThread().getName());//先执行
        Object obj=new Object();
        Thread t2=new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());//先执行
                synchronized (obj){
                    obj.notify();
                }
            }
        });
        t2.start();
        //把join方法改造成线程等待，改为wait()方法实现
//        t2.join();
        synchronized (obj){
            obj.wait();
        }

        System.out.println(Thread.currentThread().getName());//后执行
    }
}
