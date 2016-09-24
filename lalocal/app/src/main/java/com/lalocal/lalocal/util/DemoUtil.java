package com.lalocal.lalocal.util;

import com.lalocal.lalocal.model.CycleVpEntity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;



public class DemoUtil {

	public static String getDetailTime(String miao) {
		long temp = Long.parseLong(miao);
		TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
		Locale loc = new Locale("zh", "CN");
		Date d = new Date(temp);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", loc);
		df.setTimeZone(tz);

		return df.format(d);
	}

	public static List<Map<String, Object>> demoData() {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 20; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name",  "jjijoi");
			map.put("imgurl", "http://i12.tietuku.com/5c3c305ea1262ae7.jpg");
			data.add(map);
		}
		return data;
	}


	public static String[] imageUrls = { "http://img.taodiantong.cn/v55183/infoimg/2013-07/130720115322ky.jpg",
			"http://pic30.nipic.com/20130626/8174275_085522448172_2.jpg",
			"http://pic18.nipic.com/20111215/577405_080531548148_2.jpg",
			"http://pic15.nipic.com/20110722/2912365_092519919000_2.jpg",
			"http://pic.58pic.com/58pic/12/64/27/55U58PICrdX.jpg" };
	public static List<CycleVpEntity> cycData() {
		List<CycleVpEntity> list = new ArrayList<CycleVpEntity>();
		for (int i = 0; i < imageUrls.length; i++) {
			CycleVpEntity cyc = new CycleVpEntity();
			cyc.setIurl(imageUrls[i]);
			cyc.setCurl("www.baidu.com");
			cyc.setTitle("464646");
			list.add(cyc);
		}
		return list;
	}

}
