package com.lalocal.lalocal.util;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import java.util.Comparator;

public class PinyinComparator implements Comparator<Object> {
    /**
     * 比较两个字符串
     */
    public int compare(Object o1, Object o2) {
        String str1 = getPingYin((String) o1);
        String str2 = getPingYin((String) o2);
        int flag = str1.compareTo(str2);
        return flag;
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public String getPingYin(String inputString) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = inputString.trim().toCharArray();// 把字符串转化成字符数组  
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                // \\u4E00是unicode编码，判断是不是中文  
                if (java.lang.Character.toString(input[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    // 将汉语拼音的全拼存到temp数组  
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(
                            input[i], format);
                    // 取拼音的第一个读音  
                    output += temp[0];
                }
                // 大写字母转化成小写字母
                else if (input[i] > 'A' && input[i] < 'Z') {
                    output += java.lang.Character.toString(input[i]);
                    output = output.toLowerCase();
                }
//                output += java.lang.Character.toString(input[i]);
                output += ",";
            }
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return output;
    }
}  
