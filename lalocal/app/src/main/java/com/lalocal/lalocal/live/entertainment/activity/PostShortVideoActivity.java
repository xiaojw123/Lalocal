package com.lalocal.lalocal.live.entertainment.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.VideoContinueUploadActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.entertainment.model.PostShortVideoParameterBean;
import com.lalocal.lalocal.live.entertainment.model.ShortVideoTokenBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.ShortVideoDialog;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
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
 * Created by ${WCJ} on 2017/1/10.
 */
public class PostShortVideoActivity extends BaseActivity implements PhotoSelectDialog.OnDialogClickListener, ShortVideoDialog.OnDialogClickListener {

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    public static final int VIDEO_REQUSET = 4;
    public static final int CONTINUE_UPLOAD_REQUSET_CODE=404;
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
    @BindView(R.id.short_video_cover_iv)
    ImageView shortVideoCoverIv;
    @BindView(R.id.short_video_cover_tv)
    TextView shortVideoCoverTv;
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
    @BindView(R.id.short_video_cover_layout)
    RelativeLayout shortVideoCoverLayout;

    Bitmap bitmap = null;
    @BindView(R.id.video_size)
    TextView videoSize;
    private byte[] bytesImg;
    private File tempFile;
    boolean chooseVideo = false;
    private int limitSize;
    private int duration;
    private int videosSize;
    private int videoDuration;
    private PostShortVideoParameterBean bean;
    private String dataLoaction;
    private String videoPath;
    private String videoUrl;
    private String filename;
    private String token;
    private ContentLoader contentLoader;
    private Bitmap videoThumbnail;

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

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                try {
                    bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        shortVideoCoverIv.setImageBitmap(bitmap);
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
        } else if (requestCode == VIDEO_REQUSET && resultCode == RESULT_OK) {
            chooseVideo = true;
            Uri uri = data.getData();

            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                String v_path = cursor.getString(1); // 图片文件路径
                String v_size = cursor.getString(2); // 图片大小
                String v_name = cursor.getString(3); // 图片文件名
                AppLog.d("TAG", "v_path:" + v_path + " :    v_size :" + v_size + "   :   v_name:" + v_name);
                //用于获取视频的缩略图
                videoThumbnail = ThumbnailUtils.createVideoThumbnail(v_path, MediaStore.Video.Thumbnails.MICRO_KIND);
                videoPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                AppLog.d("TAG", "v_path:" + v_path + " :    v_size :" + v_size + "   :   videoPath:" + videoPath);
                if (videoThumbnail != null) {
                    postCoverIv.setImageBitmap(videoThumbnail);
                } else {
                    AppLog.i("TAG", "生成视频缩略图失败");
                }
                videosSize = (int) cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
                videoDuration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));

                Bundle bundle=new Bundle();
                bundle.putString(KeyParams.VIDEO_PREVIEW_PATH,videoPath);
                Intent intent = new Intent(PostShortVideoActivity.this, ShortVideoPreviewActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,KeyParams.VIDEO_PREVIEW_REQUESTCODE);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }

        } else if (requestCode == KeyParams.LOCATION_REQUESTCODE && resultCode == KeyParams.LOCATION_RESULTCODE) {
            dataLoaction = data.getStringExtra(KeyParams.POST_GET_LOCATION);
            if (dataLoaction != null) {
                postLaoction.setText(dataLoaction);
            }
        }else if(requestCode==KeyParams.VIDEO_PREVIEW_REQUESTCODE&&resultCode==RESULT_OK){
            String saveFilePath = data.getStringExtra("saveFilePath");
            String videoDirection = data.getStringExtra("videoDirection");
            if(bean!=null&&saveFilePath!=null){
                bean.setVideoPath(saveFilePath);
               bean.setDirection(videoDirection);
            }
            AppLog.d("TAG","压缩视频路劲："+saveFilePath);
        }

        if(requestCode==CONTINUE_UPLOAD_REQUSET_CODE){
            if(resultCode==KeyParams.UPLOAD_SHORT_VIEW_RESULTCODE){
                setResult(KeyParams.UPLOAD_SHORT_VIEW_RESULTCODE,null);
            }else if(resultCode==KeyParams.UPLOAD_SHORT_VIEW_SUCCESS_RESULTCODE_){
                setResult(KeyParams.UPLOAD_SHORT_VIEW_SUCCESS_RESULTCODE_,null);
            }
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_short_video_layout);
        ButterKnife.bind(this);
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getShortVideoToken();
        bean = new PostShortVideoParameterBean();
    }

    @OnClick({R.id.post_back, R.id.post_laoction, R.id.post_cover_iv, R.id.post_alter_ok_tv, R.id.post_video_light_spot, R.id.short_video_cover_iv, R.id.post_content_layout})
    public void clickBtn(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.post_back:
                if (!chooseVideo && postTitle.getText().toString().trim().length() < 1 && postLaoction.equals(getString(R.string.please_choose_location))) {
                    finish();
                } else {
                    CustomChatDialog customDialog = new CustomChatDialog(this);
                    customDialog.setContent("当前有编辑内容，请确认是否放弃编辑");
                    customDialog.setCancelable(false);
                    customDialog.setCancelBtn("取消", null);
                    customDialog.setSurceBtn("确认", new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            finish();
                        }
                    });
                    customDialog.show();
                }

                break;
            case R.id.post_cover_iv:
                ShortVideoDialog shortVideoDialog = new ShortVideoDialog(this);
                shortVideoDialog.setButtonClickListener(this);
                shortVideoDialog.show();
                break;
            case R.id.short_video_cover_iv:
                //更换封面
                PhotoSelectDialog dialog = new PhotoSelectDialog(this);
                dialog.setButtonClickListener(this);
                dialog.show();
                break;
            case R.id.post_laoction:
                //todo 去选择地理位置activity
                intent = new Intent(this, LiveLocationActivity.class);
                startActivityForResult(intent, KeyParams.LOCATION_REQUESTCODE);
                break;
            case R.id.post_alter_ok_tv:
                String content = null;
                if (!chooseVideo) {
                    content = getString(R.string.please_chooose_video);
                }
                if (postTitle.getText().toString().trim().length() < 1) {
                    content = getString(R.string.please_edit_title);
                }
                if (postLaoction.equals(getString(R.string.please_choose_location))) {
                    content = getString(R.string.please_choose_location);
                }
                if (content != null) {
                    showHintDialog(content);
                } else {
                    //TODO 发布视频
                    bean.setTitle(postTitle.getText().toString().trim());
                    bean.setDescription(postVideoLightSpot.getText().toString().trim());
                    bean.setDirection("0");
                    bean.setAddress(dataLoaction);
                    bean.setLatitude(CommonUtil.LATITUDE);
                    bean.setLongitude(CommonUtil.LONGITUDE);
                    bean.setFilename(filename);
                    bean.setToken(token);
                    bean.setBytesImg(bytesImg);
                    bean.setOriginUrl(videoUrl);
                    bean.setSize(String.valueOf(videosSize));
                    bean.setDuration(String.valueOf(videoDuration));
                    bean.setBitmap(videoThumbnail);

                    if(bean.getVideoPath()!=null){
                        if(bitmap==null){
                            intent = new Intent(this, VideoContinueUploadActivity.class);
                            intent.putExtra(KeyParams.SHORT_VIDEO_PARAMETER, bean);
                            startActivityForResult(intent,CONTINUE_UPLOAD_REQUSET_CODE);
                        }else {
                            contentLoader.getImgToken();
                        }
                    }
                }
                break;

        }
    }


    public class MyCallBack extends ICallBack {
        @Override
        public void onShortVideo(ShortVideoTokenBean shortVideoTokenBean) {
            limitSize = shortVideoTokenBean.getLimitSize();
            videoSize.setText("视频最大限制" + limitSize + "M");
            videoUrl = shortVideoTokenBean.getUrl();
            filename = shortVideoTokenBean.getFilename();
            token = shortVideoTokenBean.getToken();

        }

        @Override
        public void onImgToken(ImgTokenBean imgTokenBean) {
            super.onImgToken(imgTokenBean);
            if(bytesImg!=null){
                upLoadQiNiu(bytesImg,imgTokenBean.getResult().getFilename(),imgTokenBean.getResult().getToken());
            }
        }
    }

    private void upLoadQiNiu(byte[] bytesImg, final String filename, String token) {
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(bytesImg, filename, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        boolean ok = info.isOK();
                        AppLog.i("TAG",ok==true?"上传成功":"上传失败");
                        Intent  intent = new Intent(PostShortVideoActivity.this, VideoContinueUploadActivity.class);
                        if(info.isOK()){
                            bean.setPhoto(filename);
                        }
                        intent.putExtra(KeyParams.SHORT_VIDEO_PARAMETER, bean);
                        startActivityForResult(intent,CONTINUE_UPLOAD_REQUSET_CODE);

                    }
                }, null);
    }


    private void showHintDialog(String content) {
        CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setTitle(getString(R.string.live_hint));
        customDialog.setContent(content);
        customDialog.setCancelable(false);
        customDialog.setOkBtn(getString(R.string.ok_video), null);
        customDialog.show();
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
    public void onClickListner(Dialog dialog, View view) {
        dialog.dismiss();
        searchVideoBum();
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

    private void searchPhotoBum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,
                PHOTO_REQUEST_GALLERY);
    }

    private void searchVideoBum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*");
        startActivityForResult(intent, VIDEO_REQUSET);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
