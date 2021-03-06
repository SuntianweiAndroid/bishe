package com.bishe.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用户偏好 保存数据  应用卸载后数据丢失
 */
public class MySharedPreferences {

    private static SharedPreferences.Editor editor;
    private static SharedPreferences sharedPreferences;

    private static MySharedPreferences MySharedPreferences;

    public MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("myproject", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    public static MySharedPreferences getInstance(Context context) {
        if (MySharedPreferences == null) {
            MySharedPreferences = new MySharedPreferences(context);
        }
        return MySharedPreferences;
    }

    public static void clearAll() {
        sharedPreferences.edit().clear().commit();
    }

    public static void setName(String str) {
        editor.putString("name", str);
        editor.commit();
    }

    public static String getName() {
        return sharedPreferences.getString("name", "");
    }

    public static void setPwd(String str) {
        editor.putString("pwd", str);
        editor.commit();
    }

    public static String getPwd() {
        return sharedPreferences.getString("pwd", "");
    }

    public static void setIslogin(boolean islogin) {
        editor.putBoolean("islogin", islogin);
        editor.commit();
    }

    /**
     * 获取登录状态
     * @return
     */
    public static boolean getIslogin() {
        return sharedPreferences.getBoolean("islogin", false);
    }

    /**
     * 设置经期时间
     * @param jingqi
     */
    public static void setJingqiTime(int jingqi) {
        editor.putInt("jingqi", jingqi);
        editor.commit();
    }
    /**
     * 获取经期时间
     */
    public static int getJingqiTime() {
        return sharedPreferences.getInt("jingqi", 0);
    }

    /**
     * 设置周期时间
     * @param jingqi
     */
    public static void setZhouqiTime(int jingqi) {
        editor.putInt("Zhouqi", jingqi);
        editor.commit();
    }

    /**
     * 获取周期时间
     * @return
     */
    public static int getZhouqiTime() {
        return sharedPreferences.getInt("Zhouqi", 0);
    }

    /**
     * 设置日期时间
     * @param jingqi  long类型
     */
    public static void setRiqiTime(long jingqi) {
        editor.putLong("Riqi", jingqi);
        editor.commit();
    }

    /**
     * 获取日期
     * @return  long类型时间
     */
    public static long getRiqiTime() {
        return sharedPreferences.getLong("Riqi", 0);
    }

    /**
     * 设置日期时间
     * @param jingqi 年月日时间
     */
    public static void setRiqiTime2(String jingqi) {
        editor.putString("Riqi", jingqi);
        editor.commit();
    }
    /**
     * 获取日期
     * @return  年与日时间
     */
    public static String getRiqiTime2() {
        return sharedPreferences.getString("Riqi", "");
    }


    public static void setJiLuState(String jilustate) {
        editor.putString("jilustate", jilustate);
        editor.commit();
    }

    public static void setJiLuTime(long jilutime) {
        editor.putLong("jilutime", jilutime);
        editor.commit();
    }

    public static long getJiLuTime() {
        return sharedPreferences.getLong("jilutime", 0L);
    }

    public static String getJiLuState() {
        return sharedPreferences.getString("jilustate", "");
    }

    public static void setJingqiState(String jingqistate) {
        editor.putString("jingqistate", jingqistate);
        editor.commit();
    }

    public static String getJingqiState() {
        return sharedPreferences.getString("jingqistate", "");
    }

    public static void setaiaiState(String aiaitate) {
        editor.putString("aiaitate", aiaitate);
        editor.commit();
    }

    public static String getaiaiState() {
        return sharedPreferences.getString("aiaitate", "");
    }

    public static void setxinqingState(String xinqingstate) {
        editor.putString("xinqingstate", xinqingstate);
        editor.commit();
    }

    public static String getxinqingState() {
        return sharedPreferences.getString("xinqingstate", "");
    }
}
