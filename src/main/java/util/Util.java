package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String DATA_PATTERN="yyyy-MM-dd HH:mm:ss";
    /**
     * 解析文件大小的中文描述
     *
     * */
    public static String parseSize(long size) {
    String[] danweis={"B","KB","MB","GB","PB","TB"};
    int idx=0;
    while(size>1024&& idx<danweis.length-1){//避免数组越界

        size/=size;
        idx++;
    }
        return size+danweis[idx];
    }

    /**
     * 解析日期为中文描述
     *
     * */
    public static String parseDate(Date lastModified) {

        return new SimpleDateFormat(DATA_PATTERN).format(lastModified);
    }

    public static void main(String[] args) {
        System.out.println(parseSize(100));
        System.out.println(parseDate(new Date()));
    }


}
