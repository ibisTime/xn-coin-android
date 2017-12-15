package com.cdkj.baseim.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdkj.baseim.R;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baseim.model.Message;
import com.cdkj.baseim.ui.CircleImageView;
import com.cdkj.baselibrary.utils.ImgUtils;

import java.util.List;


/**
 * 聊天界面adapter
 */
public class ChatAdapter extends ArrayAdapter<Message> {

    private final String TAG = "ChatAdapter";

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;

    private Context context;
    private ImUserInfo imUserInfo;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ChatAdapter(Context context, int resource, List<Message> objects, ImUserInfo imUserInfo) {
        super(context, resource, objects);
        resourceId = resource;

        this.context = context;
        this.imUserInfo = imUserInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null){
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }else{
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            viewHolder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);
            viewHolder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            viewHolder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);
            viewHolder.sending = (ProgressBar) view.findViewById(R.id.sending);
            viewHolder.error = (ImageView) view.findViewById(R.id.sendError);
            viewHolder.sender = (TextView) view.findViewById(R.id.sender);
            viewHolder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            viewHolder.systemMessage = (TextView) view.findViewById(R.id.systemMessage);

            // 头像
            viewHolder.leftAvatarTxt = (TextView) view.findViewById(R.id.leftAvatar_txt);
            viewHolder.leftAvatarPic = (CircleImageView) view.findViewById(R.id.leftAvatar_pic);
            viewHolder.rightAvatarTxt = (TextView) view.findViewById(R.id.rightAvatar_txt);
            viewHolder.rightAvatarPic = (CircleImageView) view.findViewById(R.id.rightAvatar_pic);

            view.setTag(viewHolder);
        }

        if (position < getCount()){
            setAvatar();

            final Message data = getItem(position);
            data.showMessage(viewHolder, getContext());
        }
        return view;
    }

    private void setAvatar() {

        Log.e("ChatAdapter"," setAvatar()");
        Log.e("getRightImg",imUserInfo.getRightImg());
        Log.e("getRightName",imUserInfo.getRightName());
        Log.e("getLeftImg",imUserInfo.getLeftImg());
        Log.e("getLeftName",imUserInfo.getLeftName());
        Log.e("getIdentify",imUserInfo.getIdentify());

        // 设置右头像
        ImgUtils.loadAvatar(context,
                imUserInfo.getRightImg(),
                imUserInfo.getRightName(),
                viewHolder.rightAvatarPic,
                viewHolder.rightAvatarTxt);


        // 设置左头像
        ImgUtils.loadAvatar(context,
                imUserInfo.getLeftImg(),
                imUserInfo.getLeftName(),
                viewHolder.leftAvatarPic,
                viewHolder.leftAvatarTxt);

        setNotifyOnChange(true);
    }


    public class ViewHolder{
        public String leftName = imUserInfo.getLeftName();

        public RelativeLayout leftMessage;
        public RelativeLayout rightMessage;
        public RelativeLayout leftPanel;
        public RelativeLayout rightPanel;
        public ProgressBar sending;
        public ImageView error;
        public TextView sender;
        public TextView systemMessage;
        public TextView rightDesc;

        // 头像
        public TextView leftAvatarTxt;
        public CircleImageView leftAvatarPic;
        public TextView rightAvatarTxt;
        public CircleImageView rightAvatarPic;
    }
}
