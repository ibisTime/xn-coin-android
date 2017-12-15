package com.cdkj.baseim.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 李先俊 on 2017/10/30.
 */

public class ImUserInfo implements Parcelable {

    private String eventTag;

    private String identify; // 聊天id，如果是单聊就是对方Id，群聊则是群组Id

    private String leftImg;  // 聊天对象头像
    private String leftName;  // 聊天对象姓名

    private String rightImg; // 我的头像
    private String rightName; // 对象姓名

    public String getEventTag() {
        return eventTag;
    }

    public void setEventTag(String eventTag) {
        this.eventTag = eventTag;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getLeftImg() {
        return leftImg;
    }

    public void setLeftImg(String leftImg) {
        this.leftImg = leftImg;
    }

    public String getRightImg() {
        return rightImg;
    }

    public void setRightImg(String rightImg) {
        this.rightImg = rightImg;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public String getLeftName() {
        return leftName;
    }

    public void setLeftName(String leftName) {
        this.leftName = leftName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.leftImg);
        dest.writeString(this.leftName);
        dest.writeString(this.identify);
        dest.writeString(this.rightImg);
        dest.writeString(this.rightName);
    }

    public ImUserInfo() {
    }

    protected ImUserInfo(Parcel in) {
        this.leftImg = in.readString();
        this.leftName = in.readString();
        this.identify = in.readString();
        this.rightImg = in.readString();
        this.rightName = in.readString();
    }

    public static final Creator<ImUserInfo> CREATOR = new Creator<ImUserInfo>() {
        @Override
        public ImUserInfo createFromParcel(Parcel source) {
            return new ImUserInfo(source);
        }

        @Override
        public ImUserInfo[] newArray(int size) {
            return new ImUserInfo[size];
        }
    };
}
