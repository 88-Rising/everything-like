package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.Format;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PinyinUtil {
    private static final HanyuPinyinOutputFormat FORMAT
            =new HanyuPinyinOutputFormat();

    static{
        //设置拼音小写
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //设置不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置带v字符 如lv绿
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }
    /*
    * 通过一个文件名获取全拼+拼音首字母
    * 中华人民共和--->zhonghuarenminggongheguo/zhrmghg
    *
    * name 文件名
    * 返回 拼音全拼字符串+拼音字母字符串 数组
    * */
    public static String[] get (String name){
       String[] result =new String[2];
       StringBuilder pinyin = new StringBuilder();//全拼
       StringBuilder pinyinFirst=new StringBuilder();//首字母
        //获取汉语拼音
        for(char c:name.toCharArray()){
            try {
                String[] pinyins =PinyinHelper.toHanyuPinyinStringArray(c,FORMAT);
                if(pinyins==null||pinyins.length==0){
                    pinyin.append(c);
                    pinyinFirst.append(c);
                }else{
                    pinyin.append(pinyins[0]);//全拼zhong
                    pinyinFirst.append(pinyins[0].charAt(0));//z


                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }

        }
        result[0]=pinyin.toString();
        result[1]=pinyinFirst.toString();
        return result;


    }
    /*
    和[he,hu,huo,...]长[zhang,chang]
    hezhanghe/hezhanghu/hezhanghuo/......
    * name
    * fullSpell ture表示全拼 false表示拼音首字母
    * return 包含多音字的字符串二维组合：[zhang,chang] [he,hu,huo,..]
    *
    * */
    public static String[][] get(String name,boolean fullSpell ){
     char[] chars=name.toCharArray();
        String[][] result=new String[chars.length][];
        for(int i=0;i<chars.length;i++){
            try {
                //去除音调，“只”[zhi,zhi]
                String[] pinyins=PinyinHelper
                        .toHanyuPinyinStringArray(chars[i],FORMAT);
                if(pinyins==null||pinyins.length==0){
                    result[i]=new String[]{String.valueOf(chars[i])};
                }else{//拼音首字母
                    result[i]=unique(pinyins,fullSpell);
                }

            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                result[i]=new String[]{String.valueOf(chars[i])};
            }

        }
        return result;

    }
    /*
    * 字符串数组去重操作
    *
    * */
    public static String[] unique(String[] array,boolean fullSpell){
        Set<String> set=new HashSet<>();
        for(String s:array){
            if(fullSpell){
                set.add(s);
            }else{
                set.add(String.valueOf(s.charAt(0)));
            }
        }
        return  set.toArray(new String[set.size()]);

    }

    /*
    * 和[he,hu,huo,...]长[zhang,chang]
    * hezhang,hechang,huzhang.....//组合
    * 每个中文字符返回是拼音是字符串数组，每两个中文字符数组合并为以一个字符串数组之后以此类推
    *
    * pinyinArray传入的二维数组 两两组合
    * */

    public static String[] compose(String[][] pinyinArray){
        if(pinyinArray==null||pinyinArray.length==0){
              return null;
        }else if(pinyinArray.length==1){
            return pinyinArray[0];
        } else{
          for(int i=1;i<pinyinArray.length;i++){
             pinyinArray[0]=compose(pinyinArray[0],pinyinArray[i]);

          }
            return pinyinArray[0];
        }

    }

    public static String[] compose(String[] pinyins1,String[] pinyins2){
        String[] result=new String[pinyins1.length*pinyins2.length];
        for(int i=0;i<pinyins1.length;i++){
            for(int j=0;j<pinyins2.length;j++){
              result[i*pinyins2.length+j]=pinyins1[i]+pinyins2[j];

            }

        }
    return result;

    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(get("中华人民共和国")));
        System.out.println(Arrays.toString(get("中华1人民b共和A国")));

        System.out.println(Arrays.toString(compose(get("中华人民共和国",true))));

    }
}
