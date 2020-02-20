import java.util.concurrent.atomic.AtomicInteger;
/*
* JDK(Java Development Kit) java开发工具包
* JDK提供使用java开发工具包含了JRE
* 其中的开发工具：编译工具javac.exe 打包工具jar.exe
*
* JRE(Java Runtime Enviroment)java运行环境
* 包含了java虚拟机(JVM Java Virtual Machine)和Java程序所需的核心类库等
* 如果只需要运行开发好的程序计算机中只用安装JRE
* 使用JDK的开发工具完成java程序交给JRE运行
*
* */
public class ThreadTest {
    //引入线程安全测试
    //线程安全计数器
    private static volatile AtomicInteger COUNT=new AtomicInteger();

    public static void main(String[] args) {
        for(int i=0;i<20;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0;j<10000;j++){
                        COUNT.incrementAndGet();//++i
//                        COUNT.getAndIncrement();//i++

                    }
                }
            }).start();

        }
        while(Thread.activeCount()>1){
            Thread.yield();
            System.out.println(COUNT.get());
        }

    }
}
