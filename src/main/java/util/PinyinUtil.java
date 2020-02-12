package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.Format;

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
}
