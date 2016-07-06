package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by xiaojw on 2016/6/2.
 */
public class CustomEditText extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {
    public static final int TYPE_1 = 101;
    public static final int TYPE_2 = 102;
    EditText editText;
    Button clearBtn;
    Button selecedBtn;
    boolean isClearBtnVisible = true;
    boolean isFilterSpace;
    TextView lightText;
    boolean isEnd;


    public CustomEditText(Context context) {
        this(context, null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText);
        String hintText = a.getString(R.styleable.CustomEditText_text_hint);
        int maxLen = a.getInt(R.styleable.CustomEditText_maxLen, -1);
        String dights = a.getString(R.styleable.CustomEditText_dights);
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.custom_edit_layout, this);
        editText = (EditText) findViewById(R.id.input_edit);
        clearBtn = (Button) findViewById(R.id.clear_btn);
        if (maxLen != -1) {
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLen)});
        }
        if (!TextUtils.isEmpty(dights)) {
            editText.setKeyListener(DigitsKeyListener.getInstance(dights));
        }

        if (!TextUtils.isEmpty(hintText)) {
            editText.setHint(" " + hintText);
        }
        clearBtn.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
    }

    public void setDefaultSelectionEnd(boolean isEnd) {
        this.isEnd = isEnd;

    }

    public void setEidtType(int type) {
        if (type == TYPE_1) {
            editText.setBackgroundResource(R.drawable.email_bound_edit_bg);
        }
    }

    public void setTextVisible(boolean flag) {
        if (!flag) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }

    public void setSelectedButton(Button button) {
        selecedBtn = button;
    }

    public void setClearButtonVisible(boolean isClearBtnVisible) {
        this.isClearBtnVisible = isClearBtnVisible;

    }

    public void setText(String text) {
        editText.setText(text);
    }

    public String getText() {
        return editText.getText().toString();
    }


    @Override
    public void onClick(View v) {
        AppLog.print("onClick___");
        if (!isEidtTextEmpty()) {
            editText.setText("");
            clearBtn.setVisibility(GONE);
        }
        if (isEnd) {
            isEnd = false;
            int len = 0;
            Editable editable = editText.getText();
            if (editable != null) {
                String text = editable.toString();
                if (!TextUtils.isEmpty(text)) {
                    len = text.length();
                }
            }
            editText.setSelection(len);
        }
    }

    public boolean isEidtTextEmpty() {
        String text = editText.getText().toString();
        return TextUtils.isEmpty(text);
    }

    public void setFilterSpace(boolean isFilterSpace) {
        this.isFilterSpace = isFilterSpace;
    }


    public void setLightText(TextView lightText) {
        this.lightText = lightText;

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        AppLog.print("beforeTextChanged____");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        AppLog.print("onTextChanged___");
        if (lightText != null) {
            if (s != null && s.toString() != null && s.toString().length() > 0) {
                lightText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            } else {
                lightText.setTextColor(getResources().getColor(R.color.color_66));
            }
        }


        if (isFilterSpace) {
            if (s.toString().contains(" ")) {
                String[] str = s.toString().split(" ");
                String str1 = "";
                for (int i = 0; i < str.length; i++) {
                    str1 += str[i];
                }
                editText.setText(str1);
                editText.setSelection(start);
            }

        }
    }


    @Override
    public void afterTextChanged(Editable s) {
        AppLog.print("afterTextChanged___");

        if (editText.hasFocus()) {
            if (!isClearBtnVisible && !TextUtils.isEmpty(s.toString())) {
                clearBtn.setVisibility(VISIBLE);
            } else {
                clearBtn.setVisibility(GONE);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
        AppLog.print("onFoucs____hasFocus==="+hasFocus);


            if (selecedBtn != null) {
                selecedBtn.setSelected(true);
            }
            if (!isClearBtnVisible && !isEidtTextEmpty()) {
                AppLog.print("onFocusChange___1");
                if (clearBtn.getVisibility() != VISIBLE) {
                    clearBtn.setVisibility(VISIBLE);
                }
            }
        } else {
            if (selecedBtn != null) {
                selecedBtn.setSelected(false);
            }
            AppLog.print("onFocusChange___2");
            if (clearBtn.getVisibility() == VISIBLE) {
                clearBtn.setVisibility(GONE);
            }
        }

    }
}
