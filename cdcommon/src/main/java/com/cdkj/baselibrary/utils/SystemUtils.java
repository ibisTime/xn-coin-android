package com.cdkj.baselibrary.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by 李先俊 on 2017/7/27.
 */

public class SystemUtils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    // 粘贴板
    private static ClipboardManager mClipboard = null;

    /**
     * 判断手机操作系统是不是MIUI
     * @return
     */
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }


    public static class BuildProperties {

        private final Properties properties;

        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }

        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }

        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }

        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }

        public String getProperty(final String name) {
            return properties.getProperty(name);
        }

        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }

        public boolean isEmpty() {
            return properties.isEmpty();
        }

        public Enumeration<Object> keys() {
            return properties.keys();
        }

        public Set<Object> keySet() {
            return properties.keySet();
        }

        public int size() {
            return properties.size();
        }

        public Collection<Object> values() {
            return properties.values();
        }

        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }

    }

    /**
     * 获取MAC地址
     * @param mContext
     * @return
     */
    public static String getMacAddress(Context mContext) {

        if(!getAndroidVersion(Build.VERSION_CODES.M)){  //如果手机是6.0以下
            String macStr = "";
            WifiManager wifiManager = (WifiManager) mContext
                    .getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getMacAddress() != null) {
                macStr = wifiInfo.getMacAddress();// MAC地址
            }

            return macStr;
        }


        String address = null;
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            Log.d("TEST_BUG", " interfaceName = " + interfaces );
            while (interfaces.hasMoreElements()) {
                NetworkInterface netWork = interfaces.nextElement();
                // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
                byte[] by = netWork.getHardwareAddress();
                if (by == null || by.length == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (byte b : by) {
                    builder.append(String.format("%02X:", b));
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                String mac = builder.toString();
                Log.d("TEST_BUG", "interfaceName="+netWork.getName()+", mac="+mac);
                // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
                if (netWork.getName().equals("wlan0")) {
                    Log.d("TEST_BUG", " interfaceName ="+netWork.getName()+", mac="+mac);
                    address = mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return address;


    }


    /**
     * 获取设备编号
     */
    public synchronized static String getDeviceId() {

        String uniqueID;
        uniqueID = getUniquePsuedoID();
        if (TextUtils.isEmpty(uniqueID)) {     // 如果上面获取不到值，那么使用第二种方法获取唯一编号
            uniqueID = UUID.randomUUID().toString();   //第一种方法获取设备编号
            if (TextUtils.isEmpty(uniqueID)) {     // 如果上面获取不到值，那么直接获取当前时间作为唯一编号
                uniqueID = (int) (Math.random() * 100) + "-" + System.currentTimeMillis();
            }
        }

        return uniqueID;
    }

    public static String getUniquePsuedoID() {

        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();


            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {

            serial = "serial";
        }

        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }


    public static String getIMEI(Context con) {
        try {
            TelephonyManager tm = (TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE);
            return tm != null ? tm.getDeviceId() : "";
        }catch (Exception e){

        }
        return "";
    }

    /*判断当前版本是不是大于version*/
    public static Boolean getAndroidVersion(int version) {
        if (Build.VERSION.SDK_INT >= version) {
            return true;

        } else {
            return false;
        }
    }


    public static String getIPAddress(final boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4){
                                if(StringUtils.isIP(hostAddress)){
                                    return hostAddress;
                                }else{
                                    return "";
                                }
                            };
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');

                                String ips=index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                                return ips;
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String paste(Context context) {
        // Gets a handle to the clipboard service.
        if (null == mClipboard) {
            mClipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        }

        String resultString = "";
        // 检查剪贴板是否有内容
        if (!mClipboard.hasPrimaryClip()) {
            Toast.makeText(context, "您还没有复制内容", Toast.LENGTH_SHORT).show();
        }
        else {
            ClipData clipData = mClipboard.getPrimaryClip();
            int count = clipData.getItemCount();

            for (int i = 0; i < count; ++i) {

                ClipData.Item item = clipData.getItemAt(i);
                CharSequence str = item
                        .coerceToText(context);

                resultString += str;
            }

        }
        return resultString;
    }

    public static void copy(Context context, String content){
        if (! TextUtils.isEmpty(content)){
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content); //将内容放入粘贴管理器,在别的地方长按选择"粘贴"即可
            ToastUtil.show(context, "密钥已复制");
        }
    }

}
