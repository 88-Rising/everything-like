package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.text.Format;
import java.util.Arrays;

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
                String[] pinyins=PinyinHelper
                        .toHanyuPinyinStringArray(chars[i],FORMAT);
                if(pinyins==null||pinyins.length==0){
                    result[i]=new String[]{String.valueOf(chars[i])};
                }else if(fullSpell){//全拼
                    result[i]=pinyins;
                }else{//拼音首字母
                    String[] array=new String[pinyins.length];
                    for(int j=0;j<pinyins.length;j++){
                        array[i]=String.valueOf(pinyins[j].charAt(0));
                    }
                    result[i]=array;
                }

            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                result[i]=new String[]{String.valueOf(chars[i])};
            }

        }
        return result;

    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(get("中华人民共和国")));
        System.out.println(Arrays.toString(get("中华1人民b共和A国")));

    }
}
