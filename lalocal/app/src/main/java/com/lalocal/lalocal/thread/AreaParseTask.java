package com.lalocal.lalocal.thread;

import android.content.Context;
import android.text.TextUtils;
import android.util.JsonReader;

import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.util.PinyinComparator;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
* 解析areaJson生成list*/
public class AreaParseTask extends Thread {
    Context context;
    List<String> keylist = new ArrayList<>();
    List<Country> countryList = new ArrayList<>();

    public AreaParseTask(Context context) {
        this.context = context;
    }


    @Override
    public void run() {
        parseAssertData();
        PinyinComparator comparator = new PinyinComparator();
        Collections.sort(keylist, comparator);
        List<Country> items = new ArrayList<>();
        for (String key : keylist) {
            for (Country c : countryList) {
                if (key.equals(c.getName())) {
                    if (key.equals("中国")) {
                        items.add(0, c);
                    } else {
                        items.add(c);
                    }
                }
            }

        }
        try {
        DataSupport.saveAll(items);
        }catch (Exception e){

        }
    }


    public void parseAssertData() {
        InputStream is;
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
}