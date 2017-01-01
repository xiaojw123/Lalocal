package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.model.PlayBackMsgResultBean;
import com.lalocal.lalocal.util.DensityUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/12/19.
 */
public class PlayBackMsgAdapter extends BaseAdapter {
    Context context;
    List<PlayBackMsgResultBean.ResultBean> msgListResult;
    int userId;

    public PlayBackMsgAdapter(Context context, List<PlayBackMsgResultBean.ResultBean> msgListResult,int userId) {
        this.context = context;
        this.msgListResult = msgListResult;
        this.userId=userId;
    }

    public void addItemMsg(List<PlayBackMsgResultBean.ResultBean> msgListResult){
        this.msgListResult=msgListResult;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return msgListResult == null ? 0 : msgListResult.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            convertView = View.inflate(context, R.layout.message_item_text, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.messageItemTextItem.setBackgroundResource(R.drawable.live_master_im_item_bg);
        holder.messageItemTextItem.setPadding(0, DensityUtil.dip2px(context,8),0, DensityUtil.dip2px(context,8));
        PlayBackMsgResultBean.ResultBean resultBean = msgListResult.get(position);

        if(resultBean!=null){
            String fromNick = resultBean.getFromNick();
            String content = resultBean.getContent();
            if(fromNick!=null&&content!=null){
                if(resultBean.getUserId()== UserHelper.getUserId(context)){
                    fromNick="我";
                }else if(resultBean.getUserId()==userId&&resultBean.getUserId()!= UserHelper.getUserId(context)){
                    fromNick="主播";
                    holder.messageItemTextItem.setBackgroundResource(R.drawable.live_im_master_bg);
                }
                holder.messageItemName.setText(textviewSetContent(fromNick,content));
            }
        }




        return convertView;
    }

    private SpannableStringBuilder textviewSetContent(String nickName, String content) {
        int nameLength = nickName.length() + 1;
        int contentLength = content.length();
        SpannableStringBuilder style = new SpannableStringBuilder(nickName + ":" + content);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.live_im_item_name_color)), 0, nameLength - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#99190f00")), nameLength - 1, nameLength + contentLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }
    static class ViewHolder {
        @BindView(R.id.message_item_name)
        TextView messageItemName;
        @BindView(R.id.message_item_text_item)
        LinearLayout messageItemTextItem;
        @BindView(R.id.message_item_text_wang)
        LinearLayout messageItemTextWang;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
