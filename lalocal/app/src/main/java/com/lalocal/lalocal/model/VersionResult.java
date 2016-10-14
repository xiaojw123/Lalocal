package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by android on 2016/7/14.
 */
public class VersionResult implements Parcelable {


    /**
     * forceFlag : true
     * apiUrl : http://api.lalocal.cn/api
     * checkUpdate : true
     * downloadUrl : http://media.lalocal.cn/app/lalocal_2_1_0.apk
     * msg : ["测试,第二条"]
     */

    private boolean forceFlag;
    private String apiUrl;
    private boolean checkUpdate;
    private String downloadUrl;
    private List<String> msg;

    public boolean isForceFlag() {
        return forceFlag;
    }

    public void setForceFlag(boolean forceFlag) {
        this.forceFlag = forceFlag;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isCheckUpdate() {
        return checkUpdate;
    }

    public void setCheckUpdate(boolean checkUpdate) {
        this.checkUpdate = checkUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }

    public VersionResult() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.forceFlag ? (byte) 1 : (byte) 0);
        dest.writeString(this.apiUrl);
        dest.writeByte(this.checkUpdate ? (byte) 1 : (byte) 0);
        dest.writeString(this.downloadUrl);
        dest.writeStringList(this.msg);
    }

    protected VersionResult(Parcel in) {
        this.forceFlag = in.readByte() != 0;
        this.apiUrl = in.readString();
        this.checkUpdate = in.readByte() != 0;
        this.downloadUrl = in.readString();
        this.msg = in.createStringArrayList();
    }

    public static final Creator<VersionResult> CREATOR = new Creator<VersionResult>() {
        @Override
        public VersionResult createFromParcel(Parcel source) {
            return new VersionResult(source);
        }

        @Override
        public VersionResult[] newArray(int size) {
            return new VersionResult[size];
        }
    };
}
