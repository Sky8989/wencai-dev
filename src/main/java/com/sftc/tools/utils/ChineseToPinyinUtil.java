package com.sftc.tools.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 中文转拼音
 *
 * @author ： CatalpaFlat
 * @date ：Create in 15:45 2017/11/25
 */
public class ChineseToPinyinUtil {
    /**
     * 将文字转为汉语拼音
     *
     * @param chineseLanguage 要转成拼音的中文
     */
    public static String toHanyuPinyin(String chineseLanguage) {
        char[] clChars = chineseLanguage.trim().toCharArray();
        StringBuilder hanyupinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部小写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        try {
            for (char clChar : clChars) {
                // 如果字符是中文,则将中文转为汉语拼音
                if (String.valueOf(clChar).matches("[\u4e00-\u9fa5]+")) {
                    hanyupinyin.append(PinyinHelper.toHanyuPinyinStringArray(clChar, defaultFormat)[0]);
                    // 如果字符不是中文,则不转换
                } else {
                    hanyupinyin.append(clChar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin.toString();
    }

    public static String getFirstLettersUp(String chineseLanguage) {
        return getFirstLetters(chineseLanguage, HanyuPinyinCaseType.UPPERCASE);
    }

    public static String getFirstLettersLo(String chineseLanguage) {
        return getFirstLetters(chineseLanguage, HanyuPinyinCaseType.LOWERCASE);
    }

    private static String getFirstLetters(String chineseLanguage, HanyuPinyinCaseType caseType) {
        char[] clChars = chineseLanguage.trim().toCharArray();
        StringBuilder hanyupinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setCaseType(caseType);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            for (char clChar : clChars) {
                String str = String.valueOf(clChar);
                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                if (str.matches("[\u4e00-\u9fa5]+")) {
                    hanyupinyin.append(PinyinHelper.toHanyuPinyinStringArray(clChar, defaultFormat)[0].substring(0, 1));
                    // 如果字符是数字,取数字
                } else if (str.matches("[0-9]+")) {
                    hanyupinyin.append(clChar);
                    // 如果字符是字母,取字母
                } else if (str.matches("[a-zA-Z]+")) {
                    hanyupinyin.append(clChar);
                    //如果是标点符号的话，带着
                } else {// 否则不转换
                    hanyupinyin.append(clChar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin.toString();
    }

    public static String getPinyinString(String chineseLanguage) {
        char[] clChars = chineseLanguage.trim().toCharArray();
        StringBuilder hanyupinyin = new StringBuilder();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            for (char clChar : clChars) {
                String str = String.valueOf(clChar);
                // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
                if (str.matches("[\u4e00-\u9fa5]+")) {
                    hanyupinyin.append(PinyinHelper.toHanyuPinyinStringArray(
                            clChar, defaultFormat)[0]);
                    // 如果字符是数字,取数字
                } else if (str.matches("[0-9]+")) {
                    // 如果字符是字母,取字母
                    hanyupinyin.append(clChar);
                } else if (str.matches("[a-zA-Z]+")) {
                    hanyupinyin.append(clChar);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin.toString();
    }

    /**
     * 取第一个汉字的第一个字符
     */
    public static String getFirstLetter(String chineseLanguage) {
        char[] clChars = chineseLanguage.trim().toCharArray();
        String hanyupinyin = "";
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        // 输出拼音全部大写
        defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        // 不带声调
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        try {
            String str = String.valueOf(clChars[0]);
            // 如果字符是中文,则将中文转为汉语拼音,并取第一个字母
            if (str.matches("[\u4e00-\u9fa5]+")) {
                hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(
                        clChars[0], defaultFormat)[0].substring(0, 1);
                // 如果字符是数字,取数字
            } else if (str.matches("[0-9]+")) {
                hanyupinyin += clChars[0];
                // 如果字符是字母,取字母
            } else if (str.matches("[a-zA-Z]+")) {
                hanyupinyin += clChars[0];
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            System.out.println("字符不能转成汉语拼音");
        }
        return hanyupinyin;
    }
}

