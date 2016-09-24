package com.lalocal.lalocal.model;

public class CycleVpEntity {

	// 广告ID
	private String id;
	//详情url
	private String curl;
	// 图片url
	private String iurl;
	// 标题
	private String title;



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurl() {
		return curl;
	}

	public void setCurl(String curl) {
		this.curl = curl;
	}

	public String getIurl() {
		return iurl;
	}

	public void setIurl(String iurl) {
		this.iurl = iurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CycleVpEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CycleVpEntity(String id, String curl, String iurl, String title) {
		super();
		this.id = id;
		this.curl = curl;
		this.iurl = iurl;
		this.title = title;
	}

}
