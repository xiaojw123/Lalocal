package com.lalocal.lalocal.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.view.WheelView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/14.
 */
public class WheelDialog extends Dialog implements View.OnClickListener {


    private Context context;
    private OnWheelSelectedListener listener;
    private Country selectedItem;
    private View view;


    public WheelDialog(Context context) {
        super(context, R.style.prompt_dialog);
        this.context = context;

    }

    public void setEnalbeView(View view) {
        this.view = view;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(context).inflate(R.layout.wheeldialog_layout, null);
        setContentView(view, params);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TextView save_tv = (TextView) findViewById(R.id.wheeldialog_save_tv);
        WheelView wv = (WheelView) view.findViewById(R.id.wheeldialog_wlv);
        List<Country> areaItems = DataSupport.findAll(Country.class);
        if (areaItems != null && areaItems.size() > 0) {
            selectedItem = areaItems.get(0);
            wv.setOffset(2);
            wv.setItems(areaItems);
            wv.setSeletion(0);
            wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, Country item) {
                    selectedItem = item;

                }
            });
        }
        save_tv.setOnClickListener(this);


    }

    public void setOnWheelDialogSelectedListener(OnWheelSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (listener != null) {
            listener.onSelected(selectedItem);
        }

    }

    @Override
    public void show() {
        super.show();
        if (view != null) {
            view.setEnabled(false);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (view != null) {
            view.setEnabled(true);
        }
    }

    public interface OnWheelSelectedListener {
        void onSelected(Country item);

    }


}
