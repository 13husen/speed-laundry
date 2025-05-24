package com.speedlaundry.admin.base;

import com.blankj.utilcode.util.SPUtils;

public class NotificationConstant {
    public static final String RIDE_CODE = "ride_code";
    public static final String LAST_MESSAGE = "last_message";
    public static final String UNREAD_MESSAGE = "unreadMessage";
    public static final String TITLE_NOTIFICATION = "title_notification";
    public static final String MESSAGE_NOTIFICATION = "message_notification";
    public static final String UNREAD_NOTIFICATION = "unread_notification";

    public static String getRideCode() {
        return SPUtils.getInstance().getString(NotificationConstant.RIDE_CODE, "");
    }

    public static String getLastMessage() {
        return SPUtils.getInstance().getString(NotificationConstant.LAST_MESSAGE, "");
    }

    public static int getUnreadMessage() {
        return SPUtils.getInstance().getInt(NotificationConstant.UNREAD_MESSAGE, 0);
    }

    public static String getTitleNotification() {
        return SPUtils.getInstance().getString(NotificationConstant.TITLE_NOTIFICATION, "");
    }

    public static String getMessageNotification() {
        return SPUtils.getInstance().getString(NotificationConstant.MESSAGE_NOTIFICATION);
    }

    public static int getUnreadNotification() {
        return SPUtils.getInstance().getInt(NotificationConstant.UNREAD_NOTIFICATION, 0);
    }

    public static void clearNotificationShow() {
        SPUtils.getInstance().remove(NotificationConstant.TITLE_NOTIFICATION);
        SPUtils.getInstance().remove(NotificationConstant.MESSAGE_NOTIFICATION);
    }
}
