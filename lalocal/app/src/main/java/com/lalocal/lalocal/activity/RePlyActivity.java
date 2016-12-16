package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/12/15.
 */
public class RePlyActivity extends BaseActivity {
    @BindView(R.id.reply_back)
    ImageView replyBack;
    @BindView(R.id.reply_title_content)
    TextView replyTitleContent;
    @BindView(R.id.reply_send)
    TextView replySend;
    @BindView(R.id.reply_title)
    LinearLayout replyTitle;
    @BindView(R.id.reply_content)
    EditText replyContent;
    @BindView(R.id.input_count)
    TextView inputCount;
    private String titleContent;
    private int length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_activity);
        ButterKnife.bind(this);
        String title = getIntent().getStringExtra(KeyParams.REPLY_TITLE);
        replyTitleContent.setText(title);
        replyContent.addTextChangedListener(watcher);
    }

    boolean setColor = true;
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            titleContent = replyContent.getText().toString();
            length = titleContent.length();
            if (length > 0) {
                if (setColor) {
                    setColor = false;
                    replySend.setTextColor(getResources().getColor(R.color.color_ffaa2a));
                }
            } else {
                setColor=true;
                replySend.setTextColor(getResources().getColor(R.color.color_70190f00));
            }
            inputCount.setText(length + "/140");
        }
    };


    @OnClick({R.id.reply_send,R.id.reply_back})
    public void clickBtn(View v) {
        switch (v.getId()){
            case R.id.reply_send:
                if(length>0){
                    Intent intent=new Intent();
                    intent.putExtra(KeyParams.REPLY_CONTENT,titleContent);
                    setResult(KeyParams.REPLY_RESULTCODE,intent);
                    finish();
                }
                break;
            case R.id.reply_back:
                finish();
                break;


        }

    }
}
