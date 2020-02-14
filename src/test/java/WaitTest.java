import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class WaitTest {

    /*
    * 等待所有线程执行完毕
    * 1.CountDownLatch:初始化一个数值，可以使用countDown对数值进行i--的操作 也可以await()操作 这时候会阻塞 并且一直等待 直到i=0
    * 2.Semaphone：release()进行一定数量的许可颁发，acquire()阻塞并等待许可数量满足
    * 相对来说 semaphone功能更强大更灵活
    * */
    private static int COUNT=5;
    private static CountDownLatch LATCH=new CountDownLatch(5);
    private static Semaphore SEMAPHORE=new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<COUNT;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
//                    LATCH.countDown();//对初始化的值(5)进行i--
                    SEMAPHORE.release();//颁发许可证，无参代表颁发一个1个许可证
                }
            }).start();

        }
        //main 在所有子线程执行完毕之后，在运行以下代码
//        LATCH.await();//await()会阻塞并且一直等待，直到LATCH的值为0
       SEMAPHORE.acquire(5);//无参代表请求资源数量为1，也可以指定数量的资源
        System.out.println(Thread.currentThread().getName());
    }
}
