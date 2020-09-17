package com.texsmart.utility;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TextUtility {
    public TextUtility() {
    }

    public static int charType(char c) {
        return charType(String.valueOf(c));
    }

    public static int charType(String str) {
        if (str != null && str.length() > 0) {
            if ("零○〇一二两三四五六七八九十廿百千万亿壹贰叁肆伍陆柒捌玖拾佰仟".contains(str)) {
                return 11;
            }

            byte[] b;
            try {
                b = str.getBytes("GBK");
            } catch (UnsupportedEncodingException var6) {
                b = str.getBytes();
                var6.printStackTrace();
            }

            byte b1 = b[0];
            byte b2 = b.length > 1 ? b[1] : 0;
            int ub1 = getUnsigned(b1);
            int ub2 = getUnsigned(b2);
            if (ub1 < 128) {
                if (ub1 <= 32) {
                    return 17;
                }

                if ("*\"!,.?()[]{}+=/\\;:|".indexOf((char)b1) != -1) {
                    return 6;
                }

                if ("0123456789".indexOf((char)b1) != -1) {
                    return 9;
                }

                return 5;
            }

            if (ub1 == 162) {
                return 10;
            }

            if (ub1 == 163 && ub2 > 175 && ub2 < 186) {
                return 9;
            }

            if (ub1 == 163 && (ub2 >= 193 && ub2 <= 218 || ub2 >= 225 && ub2 <= 250)) {
                return 8;
            }

            if (ub1 == 161 || ub1 == 163) {
                return 6;
            }

            if (ub1 >= 176 && ub1 <= 247) {
                return 7;
            }
        }

        return 17;
    }

    public static boolean isAllChinese(String str) {
        return str.matches("[\\u4E00-\\u9FA5]+");
    }

    public static boolean isAllNonChinese(byte[] sString) {
        int nLen = sString.length;
        int i = 0;

        while(i < nLen) {
            if (getUnsigned(sString[i]) < 248 && getUnsigned(sString[i]) > 175) {
                return false;
            }

            if (sString[i] < 0) {
                i += 2;
            } else {
                ++i;
            }
        }

        return true;
    }

    public static boolean isAllSingleByte(String str) {
        assert str != null;

        for(int i = 0; i < str.length(); ++i) {
            if (str.charAt(i) > 128) {
                return false;
            }
        }

        return true;
    }

    public static int cint(String str) {
        if (str != null) {
            try {
                int i = new Integer(str);
                return i;
            } catch (NumberFormatException var2) {
            }
        }

        return -1;
    }

    public static boolean isAllNum(String str) {
        if (str == null) {
            return false;
        } else {
            int i = 0;
            if ("±+-＋－—".indexOf(str.charAt(0)) != -1) {
                ++i;
            }

            while(i < str.length() && "０１２３４５６７８９".indexOf(str.charAt(i)) != -1) {
                ++i;
            }

            char ch;
            if (i > 0 && i < str.length()) {
                ch = str.charAt(i);
                if ("·∶:，,．.／/".indexOf(ch) != -1) {
                    ++i;

                    while(i < str.length() && "０１２３４５６７８９".indexOf(str.charAt(i)) != -1) {
                        ++i;
                    }
                }
            }

            if (i >= str.length()) {
                return true;
            } else {
                while(i < str.length() && "0123456789".indexOf(str.charAt(i)) != -1) {
                    ++i;
                }

                if (i > 0 && i < str.length()) {
                    ch = str.charAt(i);
                    if (',' == ch || '.' == ch || '/' == ch || ':' == ch || "∶·，．／".indexOf(ch) != -1) {
                        ++i;

                        while(i < str.length() && "0123456789".indexOf(str.charAt(i)) != -1) {
                            ++i;
                        }
                    }
                }

                if (i < str.length() && "百千万亿佰仟%％‰".indexOf(str.charAt(i)) != -1) {
                    ++i;
                }

                return i >= str.length();
            }
        }
    }

    public static boolean isAllIndex(byte[] sString) {
        int nLen = sString.length;

        int i;
        for(i = 0; i < nLen - 1 && getUnsigned(sString[i]) == 162; i += 2) {
        }

        if (i >= nLen) {
            return true;
        } else {
            while(i < nLen && sString[i] > 64 && sString[i] < 91 || sString[i] > 96 && sString[i] < 123) {
                ++i;
            }

            return i >= nLen;
        }
    }

    public static boolean isAllLetter(String text) {
        for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z')) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllLetterOrNum(String text) {
        for(int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            if ((c < 'a' || c > 'z') && (c < 'A' || c > 'Z') && (c < '0' || c > '9')) {
                return false;
            }
        }

        return true;
    }

    public static boolean isAllDelimiter(byte[] sString) {
        int nLen = sString.length;

        int i;
        for(i = 0; i < nLen - 1 && (getUnsigned(sString[i]) == 161 || getUnsigned(sString[i]) == 163); i += 2) {
        }

        return i >= nLen;
    }

    public static boolean isAllChineseNum(String word) {
        String chineseNum = "零○一二两三四五六七八九十廿百千万亿壹贰叁肆伍陆柒捌玖拾佰仟∶·．／点";
        String prefix = "几数上第";
        String surfix = "几多余来成倍";
        boolean round = false;
        if (word == null) {
            return false;
        } else {
            char[] temp = word.toCharArray();

            for(int i = 0; i < temp.length; ++i) {
                if (word.startsWith("分之", i)) {
                    ++i;
                } else {
                    char tchar = temp[i];
                    if (i == 0 && prefix.indexOf(tchar) != -1) {
                        round = true;
                    } else if (i == temp.length - 1 && !round && surfix.indexOf(tchar) != -1) {
                        round = true;
                    } else if (chineseNum.indexOf(tchar) == -1) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public static int getCharCount(String charSet, String word) {
        int nCount = 0;
        if (word != null) {
            String temp = word + " ";

            for(int i = 0; i < word.length(); ++i) {
                String s = temp.substring(i, i + 1);
                if (charSet.indexOf(s) != -1) {
                    ++nCount;
                }
            }
        }

        return nCount;
    }

    public static int getUnsigned(byte b) {
        return b > 0 ? b : b & 255;
    }

    public static boolean isYearTime(String snum) {
        if (snum != null) {
            int len = snum.length();
            String first = snum.substring(0, 1);
            if (isAllSingleByte(snum) && (len == 4 || len == 2 && (cint(first) > 4 || cint(first) == 0))) {
                return true;
            }

            if (isAllNum(snum) && (len >= 3 || len == 2 && "０５６７８９".indexOf(first) != -1)) {
                return true;
            }

            if (getCharCount("零○一二三四五六七八九壹贰叁肆伍陆柒捌玖", snum) == len && len >= 2) {
                return true;
            }

            if (len == 4 && getCharCount("千仟零○", snum) == 2) {
                return true;
            }

            if (len == 1 && getCharCount("千仟", snum) == 1) {
                return true;
            }

            if (len == 2 && getCharCount("甲乙丙丁戊己庚辛壬癸", snum) == 1 && getCharCount("子丑寅卯辰巳午未申酉戌亥", snum.substring(1)) == 1) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInAggregate(String aggr, String str) {
        if (aggr != null && str != null) {
            str = str + "1";

            for(int i = 0; i < str.length(); ++i) {
                String s = str.substring(i, i + 1);
                if (aggr.indexOf(s) == -1) {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }

    public static boolean isDBCCase(String str) {
        if (str == null) {
            return false;
        } else {
            str = str + " ";

            for(int i = 0; i < str.length(); ++i) {
                String s = str.substring(i, i + 1);
                boolean var3 = false;

                int length;
                try {
                    length = s.getBytes("GBK").length;
                } catch (UnsupportedEncodingException var5) {
                    var5.printStackTrace();
                    length = s.getBytes().length;
                }

                if (length != 1) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isSBCCase(String str) {
        if (str == null) {
            return false;
        } else {
            str = str + " ";

            for(int i = 0; i < str.length(); ++i) {
                String s = str.substring(i, i + 1);
                boolean var3 = false;

                int length;
                try {
                    length = s.getBytes("GBK").length;
                } catch (UnsupportedEncodingException var5) {
                    var5.printStackTrace();
                    length = s.getBytes().length;
                }

                if (length != 2) {
                    return false;
                }
            }

            return true;
        }
    }

    public static boolean isDelimiter(String str) {
        return str != null && ("-".equals(str) || "－".equals(str));
    }

    public static boolean isUnknownWord(String word) {
        return word != null && word.indexOf("未##") == 0;
    }

    public static double nonZero(double frequency) {
        return frequency == 0.0D ? 0.001D : frequency;
    }

    public static char[] long2char(long x) {
        char[] c = new char[]{(char)((int)(x >> 48)), (char)((int)(x >> 32)), (char)((int)(x >> 16)), (char)((int)x)};
        return c;
    }

    public static String long2String(long x) {
        char[] cArray = long2char(x);
        StringBuilder sbResult = new StringBuilder(cArray.length);
        char[] var4 = cArray;
        int var5 = cArray.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            char c = var4[var6];
            sbResult.append(c);
        }

        return sbResult.toString();
    }

    public static String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    public static boolean isChinese(char c) {
        String regex = "[\\u4e00-\\u9fa5]";
        return String.valueOf(c).matches(regex);
    }

    public static int count(String keyword, String srcText) {
        int count = 0;
        int leng = srcText.length();
        int j = 0;

        for(int i = 0; i < leng; ++i) {
            if (srcText.charAt(i) == keyword.charAt(j)) {
                ++j;
                if (j == keyword.length()) {
                    ++count;
                    j = 0;
                }
            } else {
                i -= j;
                j = 0;
            }
        }

        return count;
    }

    public static void writeString(String s, DataOutputStream out) throws IOException {
        out.writeInt(s.length());
        char[] var2 = s.toCharArray();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            char c = var2[var4];
            out.writeChar(c);
        }

    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static String join(String delimiter, Collection<String> stringCollection) {
        StringBuilder sb = new StringBuilder(stringCollection.size() * (16 + delimiter.length()));
        Iterator var3 = stringCollection.iterator();

        while(var3.hasNext()) {
            String str = (String)var3.next();
            sb.append(str).append(delimiter);
        }

        return sb.toString();
    }

    public static String combine(String... termArray) {
        StringBuilder sbSentence = new StringBuilder();
        String[] var2 = termArray;
        int var3 = termArray.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String word = var2[var4];
            sbSentence.append(word);
        }

        return sbSentence.toString();
    }

    public static String join(Iterable<? extends CharSequence> s, String delimiter) {
        Iterator<? extends CharSequence> iter = s.iterator();
        if (!iter.hasNext()) {
            return "";
        } else {
            StringBuilder buffer = new StringBuilder((CharSequence)iter.next());

            while(iter.hasNext()) {
                buffer.append(delimiter).append((CharSequence)iter.next());
            }

            return buffer.toString();
        }
    }
}

