package com.qq.googleplay.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * ============================================================
 * Copyright：Google有限公司版权所有 (c) 2017
 * Author：   陈冠杰
 * Email：    815712739@qq.com
 * GitHub：   https://github.com/JackChen1999
 * 博客：     http://blog.csdn.net/axi295309066
 * 微博：     AndroidDeveloper
 * <p>
 * Project_Name：GooglePlay
 * Package_Name：com.qq.googleplay
 * Version：1.0
 * time：2016/2/16 13:33
 * des ：${TODO}
 * gitVersion：$Rev$
 * updateAuthor：$Author$
 * updateDate：$Date$
 * updateDes：${TODO}
 * ============================================================
 **/
public class EmojiUtil {
    public static String convetToHtml(String str) {
        String regex = "\\[e\\](.*?)\\[/e\\]";
        Matcher matcher = Pattern.compile(regex).matcher(str);
        String emo = "";
        while (matcher.find()) {
            emo = matcher.group();
            str = str.replaceFirst(regex, "<img src=\"emoji_0x"
                    + emo.substring(emo.indexOf("]") + 1, emo.lastIndexOf("[")) + "\"/>");
        }
        return str;
    }
}
