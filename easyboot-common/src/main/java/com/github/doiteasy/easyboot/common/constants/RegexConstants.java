package com.github.doiteasy.easyboot.common.constants;

import java.util.regex.Pattern;


public class RegexConstants {

    /**
     * 非零字符串
     **/
    public static final Pattern ZERO_PATTERN = Pattern.compile("^(0[.]?[0]*)");
    public static final Pattern IP_PATTERN = Pattern.compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
    public static final Pattern NAME = Pattern.compile("^(?!_)(?!.*?_$)[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
    public static final Pattern DIGITAL = Pattern.compile("^(([0-9]\\d*)(\\.\\d{1,5})?)$|(0\\.0?([1-9]\\d?))$");
    public static final Pattern AMOUNT_OR_DIGITAL = Pattern.compile("-?[0-9]+([,.][0-9]{1,50})?");
    public static final Pattern SCIENTIFIC = Pattern.compile("^[+-]?[\\d]+([.][\\d]*)([Ee][+-]?[\\d]+)$");
    /**
     * 身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)|(^\\d{17}([0-9]|x|X){1}$)";
    /**
     * 手机号
     */
    public static final String REGEX_MOBILE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";
    /**
     * 日期
     */
    public static final String REGEX_DATE = "^\\d{4}(\\-)\\d{2}(\\-)\\d{2}$";
    /**
     * 手机号+86
     */
    public static final String REGEX_MOBILE2 = "^([^0-9]?86)(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8};?$";
    /**
     * 中文
     */
    public static final String REGEX_CHINESE = "[\\u4e00-\\u9fa5]+";
    /**
     * 邮箱
     */
    public static final String EMAIL_ADDRESS_PATTERN = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
    /**
     * 护照
     */
    public static final String REGEX_PASSPORT_NO = "^1[45][0-9]{7}|G[0-9]{8}|P[0-9]{7}|S[0-9]{7,8}|D[0-9]+$";
    /**
     * ip
     */
    public static final String REGEX_IP = "^((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";



}
