package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;

import static com.lalocal.lalocal.view.CustomEditText.filterPassword;

/**
 * Created by xiaojw on 2016/10/14.
 */

public class MyEditText extends FrameLayout implements View.OnFocusChangeListener, TextWatcher, View.OnClickListener {
    EditText inputEdit;
    TextView titleTv;
    String title;
    boolean clearEable;
    ImageView clearBtn, showPsswordBtn;
    Button selecedBtn;
    boolean isFilterSpace;
    boolean isPsw;

    public MyEditText(Context context) {
        this(context, null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyEditText);
        title = a.getString(R.styleable.MyEditText_my_title);
        isPsw = a.getBoolean(R.styleable.MyEditText_is_password, false);
        String hintText = a.getString(R.styleable.MyEditText_hint);
        int maxLen = a.getInt(R.styleable.MyEditText_my_maxLen, -1);
        clearEable = a.getBoolean(R.styleable.MyEditText_clearEanble, false);
        Drawable drawable = a.getDrawable(R.styleable.MyEditText_my_background);
        int width = (int) a.getDimension(R.styleable.MyEditText_my_width, -1);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.my_editext_layout, this, true);
        titleTv = (TextView) findViewById(R.id.my_editext_title);
        inputEdit = (EditText) findViewById(R.id.my_editext_edit);
        clearBtn = (ImageView) findViewById(R.id.my_editext_clear);
        showPsswordBtn = (ImageView) findViewById(R.id.my_editext_showpassword_btn);
        if (width != -1) {
            FrameLayout.LayoutParams params = (LayoutParams) inputEdit.getLayoutParams();
            params.width = width;
        }
        if (drawable != null) {
            inputEdit.setBackgroundDrawable(drawable);
        }
        if (maxLen != -1) {
            inputEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        }
        if (!TextUtils.isEmpty(hintText)) {
            inputEdit.setHint(hintText);
        }
        if (isPsw) {
            //隐藏
            inputEdit.setSelected(false);
            inputEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
            showPsswordBtn.setVisibility(VISIBLE);
        } else {
            showPsswordBtn.setVisibility(GONE);
        }
        titleTv.setText(title);
        showPsswordBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        inputEdit.setOnFocusChangeListener(this);
        inputEdit.addTextChangedListener(this);
    }


    public void setText(String text) {
        inputEdit.setText(text);
    }

    public String getText() {
        return inputEdit.getText().toString();
    }


    public void setFilterSpace(boolean isFilterSpace) {
        this.isFilterSpace = isFilterSpace;
    }

    public void setSelectedButton(Button button) {
        selecedBtn = button;
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            v.setSelected(true);
            if (selecedBtn != null) {
                selecedBtn.setSelected(true);
            }
            titleTv.setVisibility(VISIBLE);
            if (clearEable && !isEidtTextEmpty()) {
                if (clearBtn.getVisibility() != VISIBLE) {
                    clearBtn.setVisibility(VISIBLE);
                }
            }
        } else {
            v.setSelected(false);
            if (selecedBtn != null) {
                selecedBtn.setSelected(false);
            }
            titleTv.setVisibility(INVISIBLE);
        }

    }

    public boolean isEidtTextEmpty() {
        String text = inputEdit.getText().toString();
        return TextUtils.isEmpty(text);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String editable = inputEdit.getText().toString();
        String inputText = editable.toString();
        if (inputText.length() > 0) {
            titleTv.setVisibility(VISIBLE);
        } else {
            titleTv.setVisibility(INVISIBLE);
        }
        if (isPsw) {

            String str = filterPassword(inputText);
            if (!editable.equals(str)) {
                inputEdit.setText(str);
                //设置新的光标所在位置
                inputEdit.setSelection(str.length());
            }
        }
//        if (isFilterSpace) {
        if (s.toString().contains(" ")) {
            String[] str = s.toString().split(" ");
            String str1 = "";
            for (int i = 0; i < str.length; i++) {
                str1 += str[i];
            }
            inputEdit.setText(str1);
            inputEdit.setSelection(start);
        }
//        }

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (inputEdit.hasFocus()) {
            if (clearEable && !TextUtils.isEmpty(s.toString())) {
                clearBtn.setVisibility(VISIBLE);
            } else {
                clearBtn.setVisibility(GONE);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.my_editext_showpassword_btn:
                if (v.isSelected()) {
                    v.setSelected(false);
                    inputEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    v.setSelected(true);
                    inputEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }

                break;

            case R.id.my_editext_clear:
                if (!isEidtTextEmpty()) {
                    inputEdit.setText("");
                    clearBtn.setVisibility(GONE);
                }
                break;
        }
    }
}
