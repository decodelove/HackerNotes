package com.logincore.hackernotes.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by admin on 2017/6/13 0013.
 */

public class SysUtil {

    public static String TAG = "SysUtil";

    public static String getRatio(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        return width + "*" + height;
    }

    public static boolean isLowMemory(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(info);
        return info.lowMemory;
    }

    /**
     * 获取最大App内存
     *
     * @param context
     * @return
     */
    public static long getMaxAppMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        return activityManager.getMemoryClass();
    }

    public static String[] getMemoryInfo(Activity activity) {// 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        String[] array = new String[4];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            array[0] = Formatter.formatFileSize(activity, mi.totalMem);
        } else {
            array[0] = "";
        }
        array[1] = Formatter.formatFileSize(activity, mi.availMem);
        array[2] = Formatter.formatFileSize(activity, mi.threshold);
        array[3] = mi.lowMemory + "";
        return array;// 将获取的内存大小规格化
    }

    /**
     * 获取总内存
     *
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context) {
        String str1 = "/proc/meminfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i(TAG, "---" + str2);
                if (str2 != null && str2.contains("MemTotal")) {
                    String[] split = str2.split(":");
                    String[] strings = split[1].trim().split(" ");
                    str2 = strings[strings.length - 2].trim();
                    Log.i(TAG, "---" + str2);
                    return Long.valueOf(str2);
                }
            }
        } catch (IOException e) {
            Log.i(TAG, "获取系统内存大小失败");
        }
        return 0;
    }

    /**
     * 获取当前App占用内存
     *
     * @param context
     * @return
     */
    public static int[] getRunningAppProcessInfo(Context context) {
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 进程ID号
        int pid = android.os.Process.myPid();
        // 用户ID
        int uid = android.os.Process.myUid();
        // 占用的内存
        int[] pids = new int[]{pid};
        Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
        int[] ss = new int[]{memoryInfo[0].dalvikPrivateDirty, memoryInfo[0].dalvikPss, memoryInfo[0].dalvikSharedDirty, memoryInfo[0].nativePrivateDirty, memoryInfo[0].nativePss, memoryInfo[0].nativeSharedDirty, memoryInfo[0].otherPrivateDirty, memoryInfo[0].otherPss, memoryInfo[0].otherSharedDirty};
        int memorySize = memoryInfo[0].dalvikPrivateDirty;
        return ss;
    }

    //sdCard大小
    public static String getSDCardMemory(Activity activity, int index) {
        long[] sdCardInfo = new long[2];
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long bSize = sf.getBlockSize();
            long bCount = sf.getBlockCount();
            long availBlocks = sf.getAvailableBlocks();
            sdCardInfo[0] = bSize * bCount;//总大小
            sdCardInfo[1] = bSize * availBlocks;//可用大小
        }
        return Formatter.formatFileSize(activity, sdCardInfo[index]);
    }

    /**
     * 获取Cpu名称
     *
     * @return
     */
    public static String getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            while ((str2 = localBufferedReader.readLine()) != null) {
                Log.i(TAG, str2);
                if (str2 != null && str2.contains("Hardware")) {
                    String name = "";
                    String[] split = str2.split(":");
                    name = split[1].trim();
                    return name;
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return "unknow";
    }

    // 获取CPU最大频率（单位KHZ）
    // "/system/bin/cat" 命令行
    // "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
    public static String getMaxCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    // 获取CPU最小频率（单位KHZ）
    public static String getMinCpuFreq() {
        String result = "";
        ProcessBuilder cmd;
        try {
            String[] args = {"/system/bin/cat",
                    "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1) {
                result = result + new String(re);
            }
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    /**
     * 获取当前CPU占比
     * 在实际测试中发现，有的手机会隐藏CPU状态，不会完全显示所有CPU信息，例如MX5，所有建议只做参考
     *
     * @return
     */
/*    public static String getCPURateDesc() {
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++) {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum)) {
                    if (str.toLowerCase(Locale.ENGLISH).startsWith("cpu")) {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find()) {
                            try {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0) {
                        firstCPUNum = currentCPUNum;
                        try {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1]) {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]);
        }
        return String.format("cpu:%.2f", rate);
    }*/

    //格式化
    public static String formatSize(long size) {
        String suffix = null;
        float fSize = 0;

        if (size >= 1024) {
            suffix = "KB";
            fSize = size / 1024;
            if (fSize >= 1024) {
                suffix = "MB";
                fSize /= 1024;
            }
            if (fSize >= 1024) {
                suffix = "GB";
                fSize /= 1024;
            }
        } else {
            fSize = size;
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
        StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
        if (suffix != null)
            resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public static String formatSize(int size) {
        return formatSize(size * 1024L);
    }


    public static Map<String, String> getPackageInfo(Context context) {
        Map<String, String> packageInfoMap = new HashMap<String, String>();
        PackageManager packageManager = context.getPackageManager();
        try {
            List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfoList.size(); i++) {
                PackageInfo packageInfo = packageInfoList.get(i);
                //过滤指定的app
                String tempPackageName = packageInfo.packageName;
                //过滤掉系统app
//            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
//                continue;
//            }
                String appName = (String) packageInfo.applicationInfo.loadLabel(packageManager);
                packageInfoMap.put(tempPackageName, appName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packageInfoMap;
    }

    public static boolean isDeviceRooted() {
        Process process = null;
        DataOutputStream os = null;
        try {
            LogUtils.i("notes","to exec su");
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            LogUtils.i("notes","exitValue=" + exitValue);
            return exitValue == 0;
        } catch (Exception e) {
            LogUtils.i("notes","Unexpected error - Here is what I know: " + e.getMessage());
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是否root。
     *
     * @return true 为root
     */
    public static boolean isRootSystem() {
        long time = System.currentTimeMillis();
        boolean isRootSystem = false;
        File f = null;
        final String kSuSearchPaths[] = {"/system/bin/", "/system/xbin/",
                "/system/sbin/", "/sbin/", "/vendor/bin/"};
        try {
            for (String kSuSearchPath : kSuSearchPaths) {
                f = new File(kSuSearchPath + "su");
                if (f.exists()) {
                    return isRootSystem = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LogUtils.i("notes","是否root:" + isRootSystem + ",获取耗时：" + (System.currentTimeMillis() - time));
        }
        return isRootSystem = false;
    }

    /**
     * 判断是否存在光传感器来判断是否为模拟器
     * 部分真机也不存在温度和压力传感器。其余传感器模拟器也存在。
     *
     * @return true 为模拟器
     */
    public static Boolean isEmulatorBYLightSensor(Context context) {
        boolean isEmulatorBYLightSensor = false;
        long time = System.currentTimeMillis();
        try {
            SensorManager sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
            Sensor sensor8 = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); //光
            return isEmulatorBYLightSensor = null == sensor8;
        } catch (Exception e) {
            e.printStackTrace();
            return isEmulatorBYLightSensor = true;
        } finally {
            LogUtils.i("notes","是否模拟器:" + isEmulatorBYLightSensor + ",获取耗时：" + (System.currentTimeMillis() - time));
        }
    }

    @Deprecated
    public static void setFuLLSrcreen(Activity activity) {
        setFuLLScreen(activity);
    }

    public static void setFuLLScreen(Activity activity) {
        setFuLLScreen(activity.getWindow());
    }

    public static void setFuLLScreen(Window window) {
        // 钻孔屏手机设置
        if (Build.VERSION.SDK_INT >= 28) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            window.setAttributes(layoutParams);
            LogUtils.i("notes","layoutInDisplayCutoutMode=" + layoutParams.layoutInDisplayCutoutMode);
        }
        // 隐藏虚拟按键，并且全屏
        final View decorView = window.getDecorView();
        int visibility = decorView.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            visibility = View.GONE;
        } else if (Build.VERSION.SDK_INT >= 19) {
            visibility |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
        final int i = visibility;
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                decorView.setSystemUiVisibility(i);
            }
        });
        decorView.setSystemUiVisibility(visibility);
    }
}