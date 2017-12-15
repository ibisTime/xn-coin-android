package com.cdkj.baseim.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.cdkj.baseim.R;
import com.cdkj.baseim.adapter.ChatAdapter;
import com.cdkj.baseim.databinding.ActivityChatBinding;
import com.cdkj.baseim.event.RefreshEvent;
import com.cdkj.baseim.model.CustomMessage;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baseim.model.ImageMessage;
import com.cdkj.baseim.model.Message;
import com.cdkj.baseim.model.MessageFactory;
import com.cdkj.baseim.model.TextMessage;
import com.cdkj.baseim.presenter.ChatPresenter;
import com.cdkj.baseim.ui.ChatInput;
import com.cdkj.baseim.util.MediaUtil;
import com.cdkj.baseim.viewfeatures.ChatView;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.CameraPhotoListener;
import com.cdkj.baselibrary.utils.CameraHelper;
import com.cdkj.baselibrary.utils.LogUtil;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.ext.message.TIMMessageDraft;
import com.tencent.imsdk.ext.message.TIMMessageExt;
import com.tencent.imsdk.ext.message.TIMMessageLocator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

import static com.cdkj.baselibrary.utils.CameraHelper.CAPTURE_PHOTO_CODE;
import static com.cdkj.baselibrary.utils.CameraHelper.CAPTURE_WALBUM_CODE;

/**
 * 单聊界面 C2C：腾讯聊天类型 代表单聊
 * Created by cdkj on 2017/10/30.
 */

public class ChatC2CActivity extends AbsBaseActivity implements ChatView {

    private ActivityChatBinding mBinding;

    private ChatPresenter presenter;
    private CameraHelper cameraHelper;

    private List<Message> messageList= new ArrayList<>();
    private ChatAdapter mMsgAdapter;

    private CompositeDisposable mTitleSubscription;

    private static final int IMAGE_PREVIEW = 400;

    private ImUserInfo imUserInfo;
    private String orderCode;

    /**
     * @param context
     * @param
     */
    public static void open(Context context, ImUserInfo imUserInfo) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ChatC2CActivity.class);
        intent.putExtra("imUserInfo", imUserInfo);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_chat, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle("联系对方");
        setTopLineState(true);
        setSubLeftImgState(true);

        cameraHelper = new CameraHelper(this, new CameraPhotoListener() {
            @Override
            public void onPhotoSuccessful(int requestCode, String path) {

                if (requestCode == CAPTURE_PHOTO_CODE || requestCode == CAPTURE_WALBUM_CODE) {
                    disMissLoading();
                    ImImagePreviewActivity.open(ChatC2CActivity.this, path, IMAGE_PREVIEW);
                }
            }

            @Override
            public void onPhotoFailure(int requestCode, String msg) {
                disMissLoading();
                showToast(msg);
            }

            @Override
            public void noPermissions(int requestCode) {
                disMissLoading();
                showToast("无相机权限");
            }
        });

        mTitleSubscription = new CompositeDisposable();
        if (getIntent() == null)
            return;

        imUserInfo = getIntent().getParcelableExtra("imUserInfo");

        if (imUserInfo == null)

        mBinding.inputPanel.setChatView(this);
        presenter = new ChatPresenter(this, imUserInfo.getIdentify(), TIMConversationType.C2C);


        initMsgList();
        presenter.start();
    }

    /**
     * 初始化消息列表
     */
    private void initMsgList() {
        mMsgAdapter = new ChatAdapter(this, R.layout.item_message, messageList, imUserInfo);
        mBinding.msgList.setAdapter(mMsgAdapter);
        mBinding.msgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBinding.inputPanel.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        mBinding.msgList.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    presenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });

        registerForContextMenu(mBinding.msgList);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()){
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        }else if (message.getMessage().isSelf()){
            menu.add(0, 4, Menu.NONE, getString(R.string.chat_pullback));
        }
        if (message instanceof ImageMessage /*|| message instanceof FileMessage*/){
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                mMsgAdapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                presenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            case 4:
                presenter.revokeMessage(message.getMessage());
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //退出聊天界面时输入框有内容，保存草稿
        if (mBinding.inputPanel.getText().length() > 0) {
            TextMessage message = new TextMessage(mBinding.inputPanel.getText());
            presenter.saveDraft(message.getMessage());
        } else {
            presenter.saveDraft(null);
        }
        RefreshEvent.getInstance().onRefresh();
        presenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.stop();
        cameraHelper.clear();
    }

    /**
     * 显示消息
     *
     * @param message
     */

    @Override
    public void showMessage(TIMMessage message) {
        LogUtil.E("消息发送成功");
        if (message == null) {
            mMsgAdapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (imUserInfo != null) {
//                    mMessage.setLeftLogo(imUserInfo.getLeftImg());
//                    mMessage.setRightLogo(imUserInfo.getRightImg());
                }

                if (mMessage instanceof CustomMessage) {
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType) {
                        case TYPING:
//                            mBaseBinding.titleView.setMidTitle(getString(R.string.chat_typing));
//                            mTitleSubscription.clear();
//                            mTitleSubscription.add(Observable.interval(3000, TimeUnit.MILLISECONDS)
//                                    .subscribeOn(AndroidSchedulers.mainThread())
//                                    .observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new Consumer<Long>() {
//                                        @Override
//                                        public void accept(Long aLong) throws Exception {
//                                            mBaseBinding.titleView.setMidTitle(mUserName);
//                                        }
//                                    }));

                            break;
                        default:
                            break;
                    }
                } else {
                    if (messageList.size() == 0) {
                        mMessage.setHasTime(null);
                    } else {
                        mMessage.setHasTime(messageList.get(messageList.size() - 1).getMessage());
                    }
                    messageList.add(mMessage);
                    mMsgAdapter.notifyDataSetChanged();
                    mBinding.msgList.setSelection(mMsgAdapter.getCount() - 1);
                }

            }
        }

    }

    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i) {
            Message mMessage = MessageFactory.getMessage(messages.get(i));

            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted)
                continue;
            if (imUserInfo != null) {
//                mMessage.setLeftLogo(imUserInfo.getLeftImg());
//                mMessage.setRightLogo(imUserInfo.getRightImg());
            }

            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1) {
                mMessage.setHasTime(messages.get(i + 1));
                messageList.add(0, mMessage);
            } else {
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        mMsgAdapter.notifyDataSetChanged();
        mBinding.msgList.setSelection(newMsgNum);
    }

    @Override
    public void showRevokeMessage(TIMMessageLocator timMessageLocator) {
        for (Message msg : messageList) {
            TIMMessageExt ext = new TIMMessageExt(msg.getMessage());
            if (ext.checkEquals(timMessageLocator)) {
                mMsgAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void clearAllMessage() {
        messageList.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {

        LogUtil.E("消息发送失败" + code + message);

        long id = message.getMsgUniqueId();
        for (Message msg : messageList) {
            if (msg.getMessage().getMsgUniqueId() == id) {
                switch (code) {
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc(getString(R.string.chat_content_bad));
                        mMsgAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    /**
     * 相册
     */
    @Override
    public void sendImage() {
        cameraHelper.startAlbum();
    }

    /**
     * 拍照
     */
    @Override
    public void sendPhoto() {
        cameraHelper.startCamera();
    }

    @Override
    public void sendText() {
        Message message = new TextMessage(mBinding.inputPanel.getText());
        presenter.sendMessage(message.getMessage());
        mBinding.inputPanel.setText("");
    }

    @Override
    public void sendFile() {

    }

    @Override
    public void startSendVoice() {

    }

    @Override
    public void endSendVoice() {

    }

    @Override
    public void sendVideo(String fileName) {

    }

    @Override
    public void cancelSendVoice() {

    }

    @Override
    public void sending() {
        Message message = new CustomMessage(CustomMessage.Type.TYPING);
        presenter.sendOnlineMessage(message.getMessage());
    }

    /**
     * 显示草稿
     */
    @Override
    public void showDraft(TIMMessageDraft draft) {
        mBinding.inputPanel.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public void videoAction() {

    }

    @Override
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == IMAGE_PREVIEW) {
            if (data == null)
                return;

            boolean isOri = data.getBooleanExtra("isOri", false);
            String path = data.getStringExtra("path");
            File file = new File(path);
            if (file.exists()) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                if (file.length() == 0 && options.outWidth == 0) {
                    Toast.makeText(this, getString(R.string.chat_file_not_exist), Toast.LENGTH_SHORT).show();
                } else {
                    if (file.length() > 1024 * 1024 * 10) {
                        Toast.makeText(this, getString(R.string.chat_file_too_large), Toast.LENGTH_SHORT).show();
                    } else {
                        Message message = new ImageMessage(path, isOri);
                        presenter.sendMessage(message.getMessage());
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.chat_file_not_exist), Toast.LENGTH_SHORT).show();
            }
        }else {
            showLoadingDialog();
            cameraHelper.onActivityResult(requestCode,resultCode,data);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        cameraHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
