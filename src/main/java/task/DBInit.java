package task;

import java.io.File;
import java.net.URL;

public class DBInit {
    /*
     * 初始化数据库 并且读取sql文件，在执行sql语句来初始化表
     *数据库文件约定好放在target/everything-like
     *
     * */
//    public static void init(){
//        //获取target编译问价夹路径
//        //根据classLorder.getResource()
//
//        URL classURL=DBInit.class.getClassLoader().getResource("./");
//        //获取target文件夹的父目录路径
//        String dir=new File(classURL.getPath()).getParent();
//        System.out.println(classURL.getPath());
//        String url ="jdbc:sqlite://"+dir+File.separator+"everything-like.db";
//        //new SqliteDataSource(),把这个对象的ur设置 进去，才会创建这个文件
//        //如果文件已经存在那么就会读取这个文件
//        System.out.println(url);



    public static void main(String[] args) {
//        init();
    }
}
