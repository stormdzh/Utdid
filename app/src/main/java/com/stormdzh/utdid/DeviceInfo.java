package com.stormdzh.utdid;

/**
 * @Description: 描述
 * @Author: dzh
 * @CreateDate: 2020-07-16 18:23
 */

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

public class DeviceInfo {
    private static final String TAG = DeviceInfo.class.getSimpleName();
    private static Context mContext = null;

    public static void init(Context context) {
        mContext = context;
    }

    public static String getDeviceModel() {
        return Build.MODEL;
    }

    public static String getDeviceID() {
        return Build.SERIAL;
    }

    public static String getOSver() {
        String oSVer = "";
        try {
            oSVer += Build.VERSION.BASE_OS + "_";
            oSVer += Build.VERSION.RELEASE;
        } catch (Exception e) {
            oSVer += "_" + e.getMessage();
        }
        return oSVer;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getSn() {
        //if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
        //    return "";
        //}

        try {
            TelephonyManager TelephonyMgr = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            return TelephonyMgr.getDeviceId();
        } catch (Exception ex) {
            return "";
        }
    }


    public static int getMemTotal() {
        if (mContext != null) {
            try {
                final ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
                activityManager.getMemoryInfo(info);
                int totalMem = (int) (info.totalMem / 1024);
                return totalMem;
            } catch (Exception e) {
                //当android4.0以下，totalMem不支持
                return 0;
            }
        } else
            return 0;
    }

    public static double getMemRatio() {
        if (mContext != null) {
            try {
                final ActivityManager activityManager = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
                activityManager.getMemoryInfo(info);
                if (info.totalMem != 0) {
                    return ((double) info.availMem) / ((double) info.totalMem);
                }
            } catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }

    public static double getCpuRatio() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();
            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {
            }

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            Log.e(TAG, "get cpu usage failed!!!");
//            ex.printStackTrace();
            return 0;
        }

//        return 0;
    }

    public static String getCpuArch() {
        return System.getProperty("os.arch");
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return 语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }


    public static String getIMEI(Context context) {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                imei = tm.getDeviceId();
            } else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    public static String getRefIMEI() {
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);

            Method method = tm.getClass().getMethod("getImei");
            imei = (String) method.invoke(tm);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static String JudgeSIM() {
        String text="";
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        //获取当前SIM卡槽数量
        int phoneCount = tm.getPhoneCount();
        //获取当前SIM卡数量
        int activeSubscriptionInfoCount = SubscriptionManager.from(mContext).getActiveSubscriptionInfoCount();
        List<SubscriptionInfo> activeSubscriptionInfoList = SubscriptionManager.from(mContext).getActiveSubscriptionInfoList();
        if(activeSubscriptionInfoList == null){
            return "";
        }
        for(SubscriptionInfo subInfo : activeSubscriptionInfoList){
            Log.d("utdid","sim卡槽位置："+subInfo.getSimSlotIndex());
            text=text+"sim卡槽位置："+subInfo.getSimSlotIndex()+"\n";
            try {
                Method method = tm.getClass().getMethod("getImei",int.class);
                String imei = (String) method.invoke(tm,subInfo.getSimSlotIndex());
                Log.d("utdid","sim卡imei："+imei);
                text=text+"sim卡："+subInfo.getSimSlotIndex()+"  imei号："+imei+"\n";
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        }
        Log.d("utdid","卡槽数量：" + phoneCount);
        Log.d("utdid","当前SIM卡数量：" + activeSubscriptionInfoCount);

        return text;
    }


    @SuppressLint({"MissingPermission", "ObsoleteSdkInt", "HardwareIds"})
    public static String getIMEI(){
        if(mContext==null) return "";
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
            if(tm == null) return "";
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //5.0
                imei = tm.getDeviceId();
            } else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;

    }

}
