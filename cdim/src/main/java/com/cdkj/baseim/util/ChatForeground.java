package com.cdkj.baseim.util;

/**
 * Created by lei on 2018/1/24.
 */

public class ChatForeground {

    //单例
    private static ChatForeground instance = new ChatForeground();

    //用于判断聊天界面是否在前台
    private boolean foreground = false;

    // 聊天id，如果是单聊就是对方Id，群聊则是群组Id
    private String identify;

    public static ChatForeground get(){
        return instance;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public boolean isForeground(){
        return foreground;
    }

    public void setForeground(boolean foreground){
        this.foreground = foreground;
    }




}
