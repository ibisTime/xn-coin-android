package com.cdkj.baseim.event;


import com.cdkj.baseim.model.CustomMessage;
import com.cdkj.baseim.model.Message;
import com.cdkj.baseim.model.MessageFactory;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.imsdk.ext.message.TIMMessageRevokedListener;
import com.tencent.imsdk.ext.message.TIMUserConfigMsgExt;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Observable;

/**
 * 消息通知事件，上层界面可以订阅此事件
 */
public class MessageEvent extends Observable implements TIMMessageListener, TIMMessageRevokedListener {


    private volatile static MessageEvent instance;

    private MessageEvent(){
        //注册消息监听器
        TIMManager.getInstance().addMessageListener(this);

    }

    public TIMUserConfig init(TIMUserConfig config) {
        TIMUserConfigMsgExt ext = new TIMUserConfigMsgExt(config);
        ext.setMessageRevokedListener(this);
        return ext;
    }

    public static MessageEvent getInstance(){
        if (instance == null) {
            synchronized (MessageEvent.class) {
                if (instance == null) {
                    instance = new MessageEvent();
                }
            }
        }
        return instance;
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {

        for (TIMMessage item : list){
            setChanged();
            notifyObservers(item);

        }
        return false;
    }

    /**
     * 主动通知新消息
     */
    public void onNewMessage(TIMMessage message){

        setChanged();
        notifyObservers(message);

    }

    /**
     * 本地新消息通知处理
     * @param msg
     */
    public void newMessageNotify(TIMMessage msg){
        if (MessageFactory.getMessage(msg) instanceof CustomMessage)
            return;

        String senderStr;

        Message message = MessageFactory.getMessage(msg);

        if (message == null)
            return;

        senderStr = message.getSender();

        if (senderStr.equals("admin")){ // 系统消息
            // 更新订单详情数据
            EventBus.getDefault().post(EventTags.IM_MSG_UPDATE_ORDER);
        }else {
            // 更新订单列表消息状态
            EventBus.getDefault().post(EventTags.IM_MSG_UPDATE);
        }
    }

    /**
     * 清理消息监听
     */
    public void clear(){
        instance = null;
    }

    @Override
    public void onMessageRevoked(TIMMessageLocator timMessageLocator) {
        setChanged();
        notifyObservers(timMessageLocator);
    }
}
