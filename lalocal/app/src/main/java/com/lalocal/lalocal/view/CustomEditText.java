package com.lalocal.lalocal.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
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

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/6/2.
 */
public class CustomEditText extends FrameLayout implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    EditText editText;
    Button clearBtn;
    Button selecedBtn;
    boolean isClearBtnVisible = true;

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
        a.recycle();
        LayoutInflater.from(context).inflate(R.layout.custom_edit_layout, this);
        editText = (EditText) findViewById(R.id.input_edit);
        clearBtn = (Button) findViewById(R.id.clear_btn);
        if (!TextUtils.isEmpty(hintText)) {
            editText.setHint(hintText);
        }
        clearBtn.setOnClickListener(this);
        editText.addTextChangedListener(this);
        editText.setOnFocusChangeListener(this);
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

    public String getText() {
        return editText.getText().toString();
    }


    @Override
    public void onClick(View v) {

        if (!isEidtTextEmpty()) {
            editText.setText("");
            clearBtn.setVisibility(GONE);
        }
    }

    public boolean isEidtTextEmpty() {
        String text = editText.getText().toString();
        return TextUtils.isEmpty(text);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    public void afterTextChanged(Editable s) {
        if (!isClearBtnVisible && !TextUtils.isEmpty(s.toString())) {
            clearBtn.setVisibility(VISIBLE);
        } else {
            clearBtn.setVisibility(GONE);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (selecedBtn != null) {
                selecedBtn.setSelected(true);
            }
            if (!isClearBtnVisible && !isEidtTextEmpty()) {
                if (clearBtn.getVisibility() != VISIBLE) {
                    clearBtn.setVisibility(VISIBLE);
                }
            }
        } else {
            if (selecedBtn != null) {
                selecedBtn.setSelected(false);
            }
            if (clearBtn.getVisibility() == VISIBLE) {
                clearBtn.setVisibility(GONE);
            }
        }

    }
}
