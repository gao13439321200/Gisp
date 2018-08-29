package com.giiisp.giiisp.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.facebook.stetho.common.LogUtil;
import com.giiisp.giiisp.api.UrlConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
import static com.giiisp.giiisp.api.UrlConstants.RequestUrl.BASE_IMG_URL;


/**
 * Created by liwei on 2017/3/27.
 */

public class ToolString {


    public static final int CHECKCAR = 3; // 选择接单车辆 的返回
    public static final String DECIMALFORMAT = "########0.00"; // 选择接单车辆 的返回
    public static final int CHECKDRIVER = 4; // 选择接单司机 的返回

    public static final int LOCATIONTIME = 1000 * 20; //20s
    public static final int LOCATIONUPTIME = 1000 * 120;//2min

    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYYMMDD = "yyyy年MM月dd日";
    public static final String YYYYMM = "yyyy年MM月";
    public static final String SHOWYYYYMMDD = "yyyy-MM-dd";
    public static int DISTANCE = 1;


    public static final String str_a = "由于货主已超过规定取消订单的时间，需向您支付部分违约金，违约金将发放您的钱包中.";
    public static final String str_b = "货主已取消该货单，请重新选择货源.";
    public static final String str_c = "确认取消订单?";
    public static final String str_d = "确定退出登录";
    public static final String str_e = "尚未提交认证";
    public static final String str_f = "资料正在审核中";
    public static final String str_g = "司机个人信息认证失败";
    public static final String str_h = "资料审核中";
    public static final String str_i = "司机个人信息为未认证状态";
    public static final String str_j = "请开启相机权限";
    public static final String str_k = "请开启手机的读取权限";
    public static final String str_l = "确认删除该司机吗?";
    public static final String str_m = "删除该车辆将会解除与该车辆相关联的司机关系";
    public static final String str_n = "只有在待接单的状态下才可修改信息";
    public static final String str_o = "车辆信息审核中,不可进行修改";
    public static final String str_p = "车辆信息审核未通过，不可进行修改";
    public static final String str_q = "请开启定位权限";
    public static final String str_r = "未登录状态，请登录";
    public static final String str_v = "请充值押金";
    public static final String str_w = "您还有未完成的订单，是否进入?";
    public static final String str_x = "请设置交易密码";
    public static final String str_z = "确定解除绑定微信";
    public static final String str_ab = "需该司机缴纳平台押金后才可接单";
    public static final String str_ac = "需该司机缴纳平台欠款后才可接单";
    public static final String str_ae = "司机钱包余额不足";
    public static final String str_af = "交易密码已错误10次,点击忘记密码找回或者1小时以后再试";
    public static final String str_ag = "退还押金后将无法继续接单,确定要退吗?";
    public static final String str_ah = "钱包欠款未补缴请缴纳后申请";
    public static final String error = "请求超时,请重试";


    private static String strIcon;

    public static String getDistance(int int_distance) {
        if (int_distance > 10000) {
            return (int_distance / 1000) + "k";
        } else {
            return int_distance + "";
        }
    }

    public static String getBankHideCode(String code) {
        int length = code.length();
        return code.substring(0, length - 11) + "*******" + code.substring(length - 2);
    }


    /*
     * 当你输入信用卡号码的时候，有没有担心输错了而造成损失呢？其实可以不必这么担心，
     * 因为并不是一个随便的信用卡号码都是合法的，它必须通过Luhn算法来验证通过. 该校验的过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加.
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和.
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除. 例如，卡号是：5432123456788881
     * 则奇数、偶数位（用红色标出）分布：5432123456788881 奇数位和=35 偶数位乘以2（有些要减去9）的结果：1 6 2 6 1 5 7
     * 7，求和=35. 最后35+35=70 可以被10整除，认定校验通过.
     * 请编写一个程序，从键盘输入卡号，然后判断是否校验通过.通过显示：“成功”，否则显示“失败”. 比如，用户输入：356827027232780
     * 程序输出：成功
     */


    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (null != cardId && cardId.length() > 0) {
            char bit = getBankCardCheckCode(cardId
                    .substring(0, cardId.length() - 1));
            if (bit == 'N') {
                return false;
            }
            return cardId.charAt(cardId.length() - 1) == bit;
        } else {
            return false;
        }
    }

    public static String getreplacephone(String textphone) {
        return textphone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 格式化钱币
     */

//    public static String fromatIcon(double s, int len) {
//        if (s == null || s.length() < 1) {
//            return "";
//        }
//        NumberFormat formater = null;
//        double num = Double.parseDouble(s);
//        if (len == 0) {
//            formater = new DecimalFormat("###,###");
//
//        } else {
//            StringBuffer buff = new StringBuffer();
//            buff.append("###,###.");
//            for (int i = 0; i < len; i++) {
//                buff.append("#");
//            }
//            formater = new DecimalFormat(buff.toString());
//        }
//        return delComma(formater.format(num));
//    }
    public static String fromatIcon(double d) {
        DecimalFormat decimalFormat = new DecimalFormat(DECIMALFORMAT);
        strIcon = decimalFormat.format(d);
        if (isEmpty(strIcon)) {
            return delComma(strIcon);
        } else {
            return "0.00";
        }

    }

    public static String fromatIcon(BigDecimal d) {
        DecimalFormat decimalFormat = new DecimalFormat(DECIMALFORMAT);
        strIcon = decimalFormat.format(d);
        if (isEmpty(strIcon)) {
            return delComma(strIcon);
        } else {
            return "0.00";
        }

    }

    /**
     * 钱包 反格式化的东西
     */
    public static String delComma(String s) {
        String formatString = "";
        if (s != null && s.length() >= 1) {
            formatString = s.replaceAll(",", "");
        }

        return formatString;
    }

    /**
     * 判断 字符串是否为空  为空返回 fasle  不为空返回 true
     *
     * @param text
     * @return
     */
    public static boolean isEmpty(String text) {
        return null == text || "".equals(text);
    }


    /**
     * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,12}$";
    /**
     * 正则表达式:验证验证码(四位验证码)
     */
    public static final String REGEX_VCODE = "^[0-9]{4}$";
    /**
     * 正则表达式:验证验证码(六位验证码)
     */
    public static final String REGEX_VCODE_6 = "^[0-9]{6}$";

    /**
     * 正则表达式:验证手机号
     */
    public static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[3678]|18[0-9]|19[9]|14[57])[0-9]{8}$";

    /**
     * 正则表达式:验证手机号
     */
    public static final String REGEX_MOBILE_ONE = "^1[0-9]{10}$";

    /**
     * 正则表达式:验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{2,10}$";

    /**
     * 正则表达式:验证身份证
     */
    public static final String REGEX_ID_CARD = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";

    /**
     * 正则表达式:验证URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w-] ./?%&=]*)?";

    /**
     * 正则表达式:验证IP地址
     */
    public static final String REGEX_IP_ADDR = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";

    /**
     * 正则表达式:验证数字
     */
    public static final String NUMBER = "^[0-9]+(\\.[0-9]+)?$";
    /**
     * 正则表达式:验证车牌号
     */
    public static final String TRUCKNO = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼台使领A-Z]{1}[A-Z0-9]{5}[A-Z0-9挂学警港澳]{1}$";

    /**
     * 校验用户名
     *
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUserName(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     *
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验验证码
     *
     * @param code
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isVcode(String code) {
        return Pattern.matches(REGEX_VCODE, code);
    }

    /**
     * 校验验证码
     *
     * @param code
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isVcode6(String code) {
        return Pattern.matches(REGEX_VCODE_6, code);
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }


    /**
     * 校验手机号
     *
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobileOne(String mobile) {
        return Pattern.matches(REGEX_MOBILE_ONE, mobile);
    }

    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     *
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校验身份证
     *
     * @param idCard
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校验IP地址
     *
     * @param ipAddress
     * @return
     */
    public static boolean isIPAddress(String ipAddress) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddress);
    }

    /**
     * 校验数字
     *
     * @param text
     * @return
     */
    public static boolean isNumber(String text) {
        return Pattern.matches(NUMBER, text);
    }

    /**
     * 校验车牌号
     *
     * @param text
     * @return
     */
    public static boolean isTruckNo(String text) {
        return Pattern.matches(TRUCKNO, text);
    }


    //验证密码格式
    public static boolean checkPwd(String pwd, String confirmPwd, String code, Context context) {
        if ("".equals(code)) {
            ToastUtils.showShort("请输入验证码");
            return false;
        } else if ("".equals(pwd)) {
            ToastUtils.showShort("请输入密码");
            return false;
        } else if ("".equals(confirmPwd)) {
            ToastUtils.showShort("请输入确认密码");
            return false;
        } else if ("".equals(code)) {
            ToastUtils.showShort("请输入验证码");
            return false;
        } else if (!ToolString.isVcode(code)) {
            ToastUtils.showShort("请输入四位验证码");
            return false;
        } else if (pwd.length() < 6 || pwd.length() > 12 || !ToolString.isPassword(pwd)) {
            ToastUtils.showShort("请输入6到12位字母或数字的密码");
            return false;
        } else if (!confirmPwd.equals(pwd)) {
            ToastUtils.showShort("确认密码和密码不一致");
            return false;
        }
        return true;
    }

    //验证手机格式
    public static boolean checkPhone(String phoneNum, Context context) {
        if ("".equals(phoneNum)) {
            ToastUtils.showShort("请输入手机号");
            return false;
        } else if (phoneNum.length() < 11 || !ToolString.isMobileOne(phoneNum)) {
            ToastUtils.showShort("手机号码有误，请重新输入.");
            return false;
        }
        return true;
    }

    public static SpannableString getContractString() {
        SpannableString spanText = new SpannableString("注册完成代表您已同意《马上来用户协议》");
        spanText.setSpan(new ForegroundColorSpan(Color.BLUE), 10, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }


    public static SpannableString getColorString(String str, int color) {
        SpannableString spanText = new SpannableString(str);
        spanText.setSpan(new ForegroundColorSpan(color), 10, spanText.length(),
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }


    //获取控件上的文字（String）
    public static String getString(View view) {
        if (view instanceof EditText)
            return ((EditText) view).getText().toString().trim();
        else if (view instanceof Button)
            return ((Button) view).getText().toString().trim();
        else if (view instanceof TextView)
            return ((TextView) view).getText().toString().trim();
        return "";
    }

    //获取控件上的文字（int）
    public static int getInt(View view) {
        String text = "";
        if (view instanceof EditText) {
            text = ((EditText) view).getText().toString().trim();
        } else if (view instanceof Button) {
            text = ((Button) view).getText().toString().trim();
        }
        return Integer.parseInt(text);
    }

    //获取具体时间
    public static String getTime(Date date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
        return format.format(date);
    }

    //获取具体时间
    public static String getTime(long date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
//        return format.format(date - 8 * 60 * 60 * 1000);
        return format.format(date);
    }

    //获取具体时间
    public static String getTime(String date, String type) {
        SimpleDateFormat format = new SimpleDateFormat(type);
//        return format.format(date - 8 * 60 * 60 * 1000);
        return format.format(date);
    }


    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {

        // 生成一个MD5加密计算摘要
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串.因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new BigInteger(1, md.digest()).toString(16);


    }

    //处理自动解析中的异常字符串
    public static String checkGsonString(Object data) {
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(data);
    }

    //处理 []
    public static String updatearrayString(String str) {
        if (str.contains("[") | str.contains("]")) {
            return str;
        } else {
            return "[" + str + "]";

        }

    }

    //时间戳转换
    public static String getTimeFromLong(long longTime) {
        //时间戳转化为Sting或Date
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = format.format(longTime);
        Date date = null;
        try {
            date = format.parse(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return format.format(date);
    }

    //获取当前时间，用于区分是否需要读取缓存图片
    public static String getTimeNow() {
        return new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
    }


    public static boolean getTimeDiffer(String startTime) { // 入参时间小于 返回 false    入参时间大于  返回 true
        long diff = 0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(startTime); //开始时间
            Date d2 = df.parse(getTimeFromLong(System.currentTimeMillis()));//结束时间
            diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别
        } catch (Exception e) {
        }
        return diff > 1000 * 60 * 10;
    }

    public static String getTradeType(int type) {
//        交易类型 1充值2支出3收入4提现5退款
        String tradetype = "未知";
        switch (type) {
            case 1:
                tradetype = "充值";
                break;
            case 2:
                tradetype = "支出";
                break;
            case 3:
                tradetype = "收入";
                break;
            case 4:
                tradetype = "提现";
                break;
            case 5:
                tradetype = "退款";
                break;
        }

        return tradetype;
    }

    public static String getTradeStatus(int type) {

//        状态0失败1进行中2成功 ,
        String tradestatus = "处理中";
        switch (type) {
            case 0:
                tradestatus = "失败";
                break;
            case 1:
                tradestatus = "处理中";
                break;
            case 2:
                tradestatus = "成功";
                break;
        }

        return tradestatus;
    }

    public static String getBankType(int type) {

//        银行卡类型1借记卡2信用卡
        String banktype = "借记卡";
        switch (type) {
            case 1:
                banktype = "借记卡";
                break;
            case 2:
                banktype = "信用卡";
                break;
        }

        return banktype;
    }


    public static String getPayType(int type) {

//        1充值2支出3收入4提现5退款
        String banktype = "未知";
        switch (type) {
            case 1:
            case 3:
            case 4:
            case 5:
                banktype = "收入:";
                break;
            case 2:
                banktype = "支出";
                break;
        }

        return banktype;
    }

    //隐藏身份证中间的信息，用*代替
    public static String getPwdIDcardNO(String idcard) {
        return idcard.replaceAll("(\\d{3})\\d{12}(\\d{3})", "$1************$2");
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static boolean isGoodJson(String json) {

        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonParseException e) {
            System.out.println("bad json: " + json);
            return false;
        }
    }

    public static String getAllAddress(String county, String town, String street, String address) {
        StringBuilder stringBuilder = new StringBuilder();
        if (isEmpty(county)) {
            stringBuilder.append(county);
        }
        if (isEmpty(town)) {
            stringBuilder.append(town);
        }
        if (isEmpty(street)) {
            stringBuilder.append(street);
        }
        if (isEmpty(address)) {
            stringBuilder.append(address);
        }
        return stringBuilder.toString();
    }

    public static String getRightTime(String time) {
        String rightTime = time.substring(0, time.length() - 2);
        Log.d("ToolString", "时间：" + rightTime);
        return rightTime;
    }


    public static String getUUID() {
        String uuid = SPUtils.getInstance().getString(UrlConstants.UUID, "");
        if (ToolString.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString().replaceAll("-", "");
            SPUtils.getInstance().put(UrlConstants.UUID, uuid);
        }
        return uuid;
    }

    public static String toJsonStr(Object object) {
        String str_json = JSONObject.toJSONString(object);
        LogUtil.d(TAG, "toJsonStr: " + str_json);
        return str_json;
    }

    public static String getUrl(String url) {
        return BASE_IMG_URL + url;
    }

}
