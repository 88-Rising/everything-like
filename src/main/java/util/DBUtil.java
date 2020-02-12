package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import task.DBInit;

import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

/*
 * 提供数据库功能
 *
 **/
public class DBUtil {

    //获取数据库连接池的功能
    //使用单例模式 多线程安全版本
    /*
    * 多线程操作存在 原子性 可见性（主内存拷贝到工作内存） 有序性
    * synchronized保证三个特性 volatile保证可见性和有序性
    *
    * */
    private static volatile DataSource DATA_SOURCE;
    private static DataSource getDataSource(){
        if(DATA_SOURCE==null){//只进行读取操作 要保证可见性使用volatile
            synchronized (DBUtil.class){//可能多个线程在这里阻塞 只有一个线程可以往下执行
                //如果不在此判断可能所有进入的线程都进来创建这个对象
                //提高效率保证安全 要保证建立的是同一个对象
                if(DATA_SOURCE==null){

                    SQLiteConfig config=new SQLiteConfig();
                    config.setDateStringFormat(Util.DATA_PATTERN);
                    //初始化操作
                    DATA_SOURCE=new SQLiteDataSource();//使用synchronized 保证指令不会重排序在创建对象的时候禁止指令重排序
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUrl());
                }

            }
        }
        return DATA_SOURCE;
    }
    private  static  String getUrl(){
        //获取target编译文件夹路径
        //根据classLorder.getResource()

        URL classURL= DBInit.class.getClassLoader().getResource("./");
        //获取target文件夹的父目录路径
        String dir=new File(classURL.getPath()).getParent();
        System.out.println(classURL.getPath());
        String url ="jdbc:sqlite://"+dir+File.separator+"everything-like.db";
        //new SqliteDataSource(),把这个对象的ur设置 进去，才会创建这个文件
        //如果文件已经存在那么就会读取这个文件
        System.out.println("获取路径"+url);
        return url;
    }

    /**
     * 提供获取数据库的连接方法
     * 从数据库连接池DataSource.getConnection()来获取数据库连接
     * */
    public static Connection getConnection() throws SQLException {

            return getDataSource().getConnection();


    }

    public static void main(String[] args) throws SQLException {
        System.out.println(getConnection());
    }

}
