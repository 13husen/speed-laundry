package com.speedlaundry.admin.base;

import com.blankj.utilcode.util.SPUtils;

/**
 * Created by jikun on 17/5/23.
 */

public class Constant {


    private static final String WS = "ws://";
    private static final String HTPP = "http://";
    private static final String IP = "119.23.51.14";
    private static final String PORT = "8080";

    //192.168.2.3
    public static String getBaseUrl() {

        return HTPP + IP + ":" + PORT + "/";
    }

    public static final String API_KEY = "AIzaSyAQK8O6mCLLitO-KwstuvgURcBQx374YOo";
    public static final String API_KEY_WEB = "AIzaSyAjFGkWzS_QNBKefy_lVmG_-vGsYViOVfg";
    public static String getWsBaseUrl() {
        return WS + IP + ":" + PORT + "/";
    }

    public static final String NOTIFICATION_SOUND = "notificationSound";
    public static final String NOTIFICATION_TITLE = "notifcationTitle";

    public static String getNotificationSound() {
        return SPUtils.getInstance().getString(NOTIFICATION_SOUND);
    }

    public static String getNotificationTitle() {
        return SPUtils.getInstance().getString(NOTIFICATION_TITLE);
    }
}
