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
* 5.工作的阻塞队列
* 6.如果超出工作队列的长度，任务要进行处理的方式
* */
    private ThreadPoolExecutor pool= new ThreadPoolExecutor(3,3,0,TimeUnit.MICROSECONDS
         ,new LinkedBlockingDeque<>(),new ThreadPoolExecutor.CallerRunsPolicy()
    );

    //快捷创建线程池的方式
//    private ExecutorService exe=Executors.newFixedThreadPool(4);

    //计数器 不传入数值 表示初始值为0
    private volatile AtomicInteger count=new AtomicInteger();

    //线程等待的锁对象
    private Object lock=new Object();

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
                File[] children=dir.listFiles();//下一级文件和文件夹
                if(children!=null){
                    for(File child:children){
                       if(child.isDirectory()){//如果是文件夹递归处理
                           System.out.println("文件夹"+child.getParent());
                           count.incrementAndGet();//计数器++i 启动子文件夹扫描任务
                           doScan(child);
                       }else{//如果是文件，待做的工作
                           //TODO
                           System.out.println("文件"+child.getParent());
                       }
                    }
                }
                int r=count.decrementAndGet();//减操作
                if(r==0){
                    synchronized (lock){
                        lock.notify();
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
    public void waitFinish() throws InterruptedException{
        synchronized (lock){
            lock.wait();
        }

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
