package com.lalocal.lalocal.easemob;


import android.content.Context;
import android.text.TextUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.easeui.widget.chatrow.EaseChatRow;
import com.easemob.exceptions.EaseMobException;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.DrawableUtils;

import org.json.JSONObject;

public class ChatRowPictureText extends EaseChatRow {

    ImageView mImageView;
    TextView mTextViewTitle;
    TextView mTextViewprice;
    TextView mTextViewDes;
    TextView mChatTextView;

    public ChatRowPictureText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }


    @Override
    protected void onInflatView() {
        if (DemoHelper.getInstance().isPictureTxtMessage(message)) {
            inflater.inflate(message.direct == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_message
                    : R.layout.em_row_sent_picture_new, this);
        }
    }

    @Override
    protected void onFindViewById() {
        mTextViewTitle = (TextView) findViewById(R.id.tv_send_title);
        mTextViewprice = (TextView) findViewById(R.id.tv_send_price_new);
        mTextViewDes = (TextView) findViewById(R.id.tv_send_des);
        mImageView = (ImageView) findViewById(R.id.iv_sendPicture_add);
        mChatTextView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    protected void onUpdateView() {

    }

    @Override
    protected void onSetUpView() {
        TextMessageBody txtBody = (TextMessageBody) message.getBody();
        if (message.direct == EMMessage.Direct.RECEIVE) {
            // 设置内容
            mChatTextView.setText(txtBody.getMessage());
//			// 设置长按事件监听
//			mChatTextView.setOnLongClickListener(new OnLongClickListener() {
//				@Override
//				public boolean onLongClick(View v) {
//					activity.startActivityForResult(
//							(new Intent(activity, ContextMenuActivity.class)).putExtra("position", position).putExtra(
//									"type", EMMessage.Type.TXT.ordinal()), ChatFragment.REQUEST_CODE_CONTEXT_MENU);
//					return true;
//				}
//			});
            return;
        }
        try {
            JSONObject jsonMsgType = message.getJSONObjectAttribute("msgtype");
            if (jsonMsgType.has("order")) {
                JSONObject entitryJobj = jsonMsgType.optJSONObject("order");
                String title = entitryJobj.optString("title");
                String des = entitryJobj.optString("des");
                String imgUrl = entitryJobj.optString("img_url");
                String price = entitryJobj.optString("price");
                if (!TextUtils.isEmpty(title)) {
                    mTextViewTitle.setText(title);
                }
                if (!TextUtils.isEmpty(des)) {
                    mTextViewDes.setVisibility(VISIBLE);
                    mTextViewDes.setText(des);
                } else {
                    mTextViewDes.setVisibility(GONE);
                }
                if (!TextUtils.isEmpty(price)) {
                    mTextViewprice.setText(price);
                }
                if (!TextUtils.isEmpty(imgUrl)) {
                    DrawableUtils.displayImg(context, mImageView, imgUrl);
                }
            }
        } catch (EaseMobException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onBubbleClick() {

    }

}
