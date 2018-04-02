package com.cdkj.bcoin.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.util.DeviceUtils;

import java.util.List;

import static com.cdkj.bcoin.util.DeviceUtils.getScreenWith;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<BaseCoinModel> list;

    private MyItemClickListener mItemClickListener;


    public RecyclerViewAdapter(Context context, List<BaseCoinModel> list) {
        this.list = list;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_coin_recycle, null);
        ViewHolder vh = new ViewHolder(view, mItemClickListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(list.get(position).isChoose()){
            holder.tvCoin.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.lineCoin.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
        }else{
            holder.tvCoin.setTextColor(context.getResources().getColor(R.color.black));
            holder.lineCoin.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.tvCoin.setText(list.get(position).getSymbol());


        RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) holder.tvCoin.getLayoutParams();
        //获取当前控件的布局对象
        params.width = getScreenWith(context) / 5; //设置当前控件布局的宽度为屏幕的五分之一
        params.height = DeviceUtils.dip2px(context,45f); //设置当前控件布局的宽度为屏幕的五分之一
        holder.tvCoin.setLayoutParams(params);//将设置好的布局参数应用到控件中

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvCoin;
        public TextView lineCoin;

        private MyItemClickListener mListener;

        public ViewHolder(View view, MyItemClickListener mListener) {
            super(view);
            tvCoin = view.findViewById(R.id.tv_coin);
            lineCoin = view.findViewById(R.id.line_coin);

            this.mListener = mListener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(mListener != null){
                mListener.OnItemClick(view.findViewById(R.id.tv_coin),getPosition());
            }
        }
    }

    public interface MyItemClickListener{
        void OnItemClick(View view, int position);
    }


    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }


}
