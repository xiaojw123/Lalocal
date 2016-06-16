package com.lalocal.lalocal.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.util.PinyinComparator;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaojw on 2016/6/14.
 */
public class WheelDialog extends Dialog implements View.OnClickListener {
    private List<String> keylist = new ArrayList<>();
    private List<Country> countryList = new ArrayList<>();

    private List<Country> items = new ArrayList<>();


    private Context context;
    private OnWheelSelectedListener listener;
    private Country selectedItem;

    public WheelDialog(Context context) {
        super(context, R.style.prompt_dialog);
        this.context = context;
        parseAssertData();
        PinyinComparator comparator = new PinyinComparator();
        Collections.sort(keylist, comparator);
        items = new ArrayList<>();
        for (String key : keylist) {
            for (Country c : countryList) {
                if (key.equals(c.getName())) {
                    items.add(c);
                }
            }

        }
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
        wv.setOffset(0);
        wv.setItems(items);
        wv.setSeletion(0);
        wv.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, Country item) {
                selectedItem = item;

            }
        });
        save_tv.setOnClickListener(this);


    }

    public void parseAssertData() {
        InputStream is = null;
        try {
            is = context.getAssets().open("areaCode.json", Context.MODE_PRIVATE);
            int length = is.available();
            byte[] buffer = new byte[length];
            is.read(buffer);
            String temp = new String(buffer);

            Reader response = new StringReader(temp.toString());
            parseResponse(response);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void parseResponse(Reader response) throws IOException {
        JsonReader reader = new JsonReader(response);
        reader.beginArray();
        while (reader.hasNext()) {
            reader.beginObject();
            String name;
            String country = "", code_plus = "";
            while (reader.hasNext()) {
                name = reader.nextName();
                if ("country".equals(name)) {
                    country = reader.nextString();
                } else if ("code_plus".equals(name)) {
                    code_plus = reader.nextString();
                } else {
                    reader.skipValue();
                }
            }
            if (!TextUtils.isEmpty(country) && !TextUtils.isEmpty(code_plus)) {
                keylist.add(country);
                Map<String, String> codeMap = new HashMap<>();
                codeMap.put(country, code_plus);
                Country c = new Country();
                c.setName(country);
                c.setCodePlus(code_plus);
                countryList.add(c);
            }
            reader.endObject();
        }
        reader.endArray();
        reader.close();
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

    public interface OnWheelSelectedListener {
        void onSelected(Country item);

    }


}
