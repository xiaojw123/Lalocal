package com.lalocal.lalocal.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.entertainment.activity.LiveLocationActivity;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.ImgTokenResult;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.PhotoSelectDialog;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/12/22.
 */
public class PostVideoActivity extends BaseActivity implements  PhotoSelectDialog.OnDialogClickListener, CustomTitleView.onBackBtnClickListener{

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    @BindView(R.id.post_back)
    ImageView postBack;
    @BindView(R.id.post_title_content)
    TextView postTitleContent;
    @BindView(R.id.reply_title)
    RelativeLayout replyTitle;
    @BindView(R.id.post_cover_iv)
    ImageView postCoverIv;
    @BindView(R.id.post_hint_title)
    TextView postHintTitle;
    @BindView(R.id.post_content_layout)
    RelativeLayout postContentLayout;
    @BindView(R.id.post_title)
    EditText postTitle;
    @BindView(R.id.post_laoction)
    TextView postLaoction;
    @BindView(R.id.post_video_light_spot)
    EditText postVideoLightSpot;
    @BindView(R.id.post_text_count)
    TextView postTextCount;
    @BindView(R.id.post_alter_ok_tv)
    TextView postAlterOkTv;
    private String intentLocation;
    private String intentTitle;
    private String intentPhoto;
    private File tempFile;
    Bitmap bitmap=null;
    private ContentLoader contentService;
    private byte[] bytesImg;
    private boolean isSuccess;
    private int historyId;
    private String dataLoaction;
    private String intentVideoInfo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                crop(Uri.fromFile(tempFile));
            } else {
                CommonUtil.showToast(this, "未找到存储卡，无法存储照片", Toast.LENGTH_SHORT);
            }

        }else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                try {
                    bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        postCoverIv.setImageBitmap(bitmap);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bytesImg = bos.toByteArray();
                        bos.flush();
                        bos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        if(requestCode==KeyParams.LOCATION_REQUESTCODE&&resultCode==KeyParams.LOCATION_RESULTCODE){
            dataLoaction = data.getStringExtra(KeyParams.POST_GET_LOCATION);
            if(dataLoaction !=null){
                postLaoction.setText(dataLoaction);
            }

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_video_layout);
        ButterKnife.bind(this);
        parseIntent();
        initView();
        contentService = new ContentLoader(this);
        contentService.setCallBack(new MyCallBack());

    }

    private void parseIntent() {
        // 获取Intent
        Intent intent = getIntent();
        if (intent == null)
            return;
        // 获取bundle
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            intentLocation = bundle.getString(KeyParams.POST_LOCATION);
            intentTitle = bundle.getString(KeyParams.POST_TITLE);
            intentPhoto = bundle.getString(KeyParams.POST_PHOTO);
            historyId = bundle.getInt(KeyParams.POST_HISTORY_ID);
            intentVideoInfo = bundle.getString(KeyParams.POST_VIDEO_INFO);
        }
    }

    private void initView() {
        DrawableUtils.loadingImg(this,postCoverIv,intentPhoto);
        postTitle.setText(intentTitle);
        if(intentTitle!=null){
            postTitle.setSelection(intentTitle.length());
        }
        postLaoction.setText(intentLocation);
        if(intentVideoInfo!=null&&intentVideoInfo.trim().length()>0){
            postVideoLightSpot.setText(intentLocation);
        }
        postVideoLightSpot.addTextChangedListener(watcher);

    }

    @OnClick({R.id.post_back,R.id.post_laoction,R.id.post_cover_iv, R.id.post_alter_ok_tv})
    public void clickBtn(View view) {
        switch (view.getId()){
            case R.id.post_back:
                finish();
                break;
            case R.id.post_laoction:
                //TODO go location activity
                Intent intent = new Intent(this, LiveLocationActivity.class);
                startActivityForResult(intent,KeyParams.LOCATION_REQUESTCODE);
                break;
            case R.id.post_cover_iv:
                //更换封面
                PhotoSelectDialog dialog = new PhotoSelectDialog(this);
                dialog.setButtonClickListener(this);
                dialog.show();
                break;
            case R.id.post_alter_ok_tv:
                AppLog.i("TAG","回放修f改提交");
                //提交修改内容
                if(bitmap!=null){
                    contentService.getImgToken();
                }else{
                    contentService.getAlterPlayBack(historyId,postTitle.getText().toString(),null,postVideoLightSpot.getText().toString(),dataLoaction==null?intentLocation:dataLoaction);
                }

                break;
        }
    }

    @Override
    public void onButtonClickListner(Dialog dialog, View view) {
        dialog.dismiss();
        int id = view.getId();
        switch (id) {
            case R.id.photoalbum_btn://相册
                searchPhotoBum();
                break;
            case R.id.photograph_btn:
                requestUserPermission(Manifest.permission.CAMERA);

                break;
        }
    }

    @Override
    public void onBackClick() {

    }


    private class MyCallBack extends ICallBack{

        @Override
        public void onImgToken(ImgTokenBean imgTokenBean) {
            super.onImgToken(imgTokenBean);
            ImgTokenResult result = imgTokenBean.getResult();
            String filename = result.getFilename();
            String token = result.getToken();
            upLoadQiNiu(bytesImg,filename, token);
        }

        @Override
        public void onAlterHistoryPlayBack(int returnCode) {
            super.onAlterHistoryPlayBack(returnCode);
            if(returnCode==0){
                setResult(KeyParams.POST_RESULTCODE,new Intent());
                finish();
            }else {
                Toast.makeText(PostVideoActivity.this,"修改失败，请重新操作",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void upLoadQiNiu(byte[] bytesImg, final String filename, String token) {

        UploadManager uploadManager = new UploadManager();
        uploadManager.put(bytesImg, filename, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        AppLog.i("TAG","key:"+key+"     fileName:"+filename+"    info:"+info.statusCode+(response==null?"     空":response.toString()));
                        if (info.statusCode == 200) {
                            contentService.getAlterPlayBack(historyId,postTitle.getText().toString(),filename,postVideoLightSpot.getText().toString(),dataLoaction==null?intentLocation:dataLoaction);

                        }else {
                            Toast.makeText(PostVideoActivity.this,"图片上传失败，请重新操作",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, null);
    }


    private TextWatcher watcher = new TextWatcher(){

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            int length = s.toString().length();
            postTextCount.setText(length+"/50");
        }
    };
    private void searchPhotoBum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,
                PHOTO_REQUEST_GALLERY);
    }

    private void photoGraph() {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            tempFile = new File(Environment
                    .getExternalStorageDirectory(),
                    "header.jpg");
            Uri uri = Uri.fromFile(tempFile);
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent,
                    PHOTO_REQUEST_CAREMA);
        } else {
            CommonUtil.showToast(this, "未找到存储卡，无法存储照片",
                    Toast.LENGTH_SHORT);
        }


    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        AppLog.print("onRequestPermissionsResult____");
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnMPermissionGranted(PERMISSION_STGAT_CODE)
    public void onPermissionGranted() {
        photoGraph();
    }

    @OnMPermissionDenied(PERMISSION_STGAT_CODE)
    public void onPermissionDenied() {
        Toast.makeText(this, "权限被拒绝，无法继续往下执行", Toast.LENGTH_SHORT).show();

    }


}
