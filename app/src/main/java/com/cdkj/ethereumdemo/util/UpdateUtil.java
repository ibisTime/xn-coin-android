package com.cdkj.ethereumdemo.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;


public class UpdateUtil {

    public static void startWeb(Context context, String url) {

        if (context == null || TextUtils.isEmpty(url)) {
            return;
        }

        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            if (!hasPreferredApplication(context, intent)) {
                intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "请检查下载链接", Toast.LENGTH_SHORT).show();
        } finally {

        }
    }

    //如果info.activityInfo.packageName为android,则没有设置,否则,有默认的程序.
    //判断系统是否设置了默认浏览器
    public static boolean hasPreferredApplication(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }
}
