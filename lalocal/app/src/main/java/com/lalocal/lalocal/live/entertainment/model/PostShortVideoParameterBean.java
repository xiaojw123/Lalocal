package com.lalocal.lalocal.live.entertainment.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * Created by ${WCJ} on 2017/1/11.
 */
public class PostShortVideoParameterBean extends DataSupport implements Parcelable {
    private String title;
    private String photo;
    private String description;
    private String address;
    private String latitude;
    private String longitude;
    private String videoUrl;
    private String filename;
    private String token;
    private   byte[] bytesImg;
    private String direction;
    private int progress;
    private Bitmap bitmap;
    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }



    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }


    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    private String originUrl;
    private String size;
    private String duration;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    private String videoPath;
    public byte[] getBytesImg() {
        return bytesImg;
    }

    public void setBytesImg(byte[] bytesImg) {
        this.bytesImg = bytesImg;
    }



    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public static Creator<PostShortVideoParameterBean> getCREATOR() {
        return CREATOR;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }



    public PostShortVideoParameterBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeString(this.description);
        dest.writeString(this.address);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeString(this.videoUrl);
        dest.writeString(this.filename);
        dest.writeString(this.token);
        dest.writeByteArray(this.bytesImg);
        dest.writeString(this.direction);
        dest.writeInt(this.progress);
        dest.writeParcelable(this.bitmap, flags);
        dest.writeString(this.originUrl);
        dest.writeString(this.size);
        dest.writeString(this.duration);
        dest.writeString(this.videoPath);
    }

    protected PostShortVideoParameterBean(Parcel in) {
        this.title = in.readString();
        this.photo = in.readString();
        this.description = in.readString();
        this.address = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.videoUrl = in.readString();
        this.filename = in.readString();
        this.token = in.readString();
        this.bytesImg = in.createByteArray();
        this.direction = in.readString();
        this.progress = in.readInt();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.originUrl = in.readString();
        this.size = in.readString();
        this.duration = in.readString();
        this.videoPath = in.readString();
    }

    public static final Creator<PostShortVideoParameterBean> CREATOR = new Creator<PostShortVideoParameterBean>() {
        @Override
        public PostShortVideoParameterBean createFromParcel(Parcel source) {
            return new PostShortVideoParameterBean(source);
        }

        @Override
        public PostShortVideoParameterBean[] newArray(int size) {
            return new PostShortVideoParameterBean[size];
        }
    };
}
