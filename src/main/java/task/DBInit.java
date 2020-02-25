package task;

import util.DBUtil;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DBInit {
    /*
     * 初始化数据库 并且读取sql文件，在执行sql语句来初始化表
     * 钓友DBUtil.getConnection();就可以完成初始化
     *数据库文件约定好放在target/everything-like

     * */
    public static String[] readSQL(){
        try {
            //通过ClassLoader获取流
            InputStream is=DBInit.class.getClassLoader()
                    .getResourceAsStream("init.sql");
            //字节流转化为字符流，需要通过字节字符转换流来操作
            BufferedReader br=new BufferedReader(new InputStreamReader(is,"UTF-8"));
            StringBuilder sb=new StringBuilder();
            String line;
            while((line =br.readLine())!=null){
                if(line.contains("--")){
                    line =line.substring(0,line.indexOf("--"));

                }
                sb.append(line);
            }
            String[] sqls=sb.toString().split(";");
            return sqls;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取sql文件错误",e);
        }

    }
    public static void init() {
        //数据库jdbc操作sql语句的执行
        //1.建立数据库连接Connection
        //2.创建sql语句执行对象Statement
        //3.执行sql语句
        //4.如果是查询操作，获取结果集ResultSet,处理结果集
        //5.释放资源
        Connection connection= null;
        Statement statement=null;
        try{
            //1.建立数据库连接Connection
            connection =DBUtil.getConnection();
            //2.创建sql语句执行对象Statement
            statement =connection.createStatement();
            String[] sqls=readSQL();
            for(String sql:sqls){
                System.out.println("执行sql:"+sql);
                //3.执行sql语句
                statement.executeUpdate(sql);

            }
            //4.如果是查询操作，获取结果集ResultSet,处理结果集

        }catch (Exception e){
             e.printStackTrace();
             throw new RuntimeException("初始化数据库表操作失败",e);

        }finally{
            //5.释放资源
            DBUtil.close(connection,statement);
        }

    }


    public static void main(String[] args) {
//        String[] sqls=readSQL();
//        for(String sql:sqls){
//            System.out.println(sql);
//        }
//        System.out.println("--------");
//        System.out.println(Arrays.toString(readSQL()));

               init();
    }
}
