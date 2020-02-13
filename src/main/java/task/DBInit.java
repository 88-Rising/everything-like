package task;

import java.io.*;
import java.net.URL;
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


    }


    public static void main(String[] args) {
        String[] sqls=readSQL();
        for(String sql:sqls){
            System.out.println(sql);
        }
        System.out.println("--------");
        System.out.println(Arrays.toString(readSQL()));

        //        init();
    }
}
