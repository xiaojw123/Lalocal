package com.lalocal.lalocal.activity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.entertainment.model.PostShortVideoParameterBean;
import com.lalocal.lalocal.live.entertainment.model.ShortVideoTokenBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.qiniu.android.common.Zone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;
import com.qiniu.android.storage.persistent.FileRecorder;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${WCJ} on 2017/1/11.
 */
public class VideoContinueUploadActivity extends BaseActivity {
    @BindView(R.id.post_back)
    ImageView postBack;
    @BindView(R.id.post_title_content)
    TextView postTitleContent;
    @BindView(R.id.reply_title)
    RelativeLayout replyTitle;
    @BindView(R.id.video_continue_cover)
    ImageView videoContinueCover;
    @BindView(R.id.upload_progress)
    ProgressBar uploadProgress;
    @BindView(R.id.upload_cover_layout)
    RelativeLayout uploadCoverLayout;
    @BindView(R.id.upload_hint_title)
    TextView uploadHintTitle;
    @BindView(R.id.upload_location)
    TextView uploadLocation;
    @BindView(R.id.upload_time)
    TextView uploadTime;
    @BindView(R.id.upload_content)
    TextView uploadContent;
    @BindView(R.id.post_alter_ok_tv)
    TextView postAlterOkTv;
    @BindView(R.id.post_status_iv)
    ImageView postStatusIv;
    private ContentLoader contentLoader;

    private UploadManager uploadManager;
    private volatile boolean isCancelled = false;
    private PostShortVideoParameterBean parameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_continue_layout);
        ButterKnife.bind(this);
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        configQiNiu();
        initData();
    }


    private void initData() {
        parameter = getIntent().getParcelableExtra(KeyParams.SHORT_VIDEO_PARAMETER);
        if (parameter != null) {
            int progress = parameter.getProgress();
            uploadProgress.setProgress(progress);
            uploadHintTitle.setText(parameter.getTitle());
            uploadLocation.setText(parameter.getAddress());
            uploadTime.setText(parameter.getDuration());
            uploadContent.setText(parameter.getDescription());
            videoContinueCover.setImageBitmap(BitmapFactory.decodeByteArray(parameter.getBytesImg(), 0, parameter.getBytesImg().length));
            AppLog.d("TAG", parameter.getBitmap() == null ? "孔公告 公告公告" : "发的发快捷回复空间很舒服");
            if (progress == 0) {
                postAlterOkTv.setVisibility(View.GONE);
                upLoadVideoQiNiu(parameter);
            } else {
                postStatusIv.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.post_back, R.id.post_alter_ok_tv})
    public void clickBtn(View view) {
        switch (view.getId()) {
            case R.id.post_back:
                saveDataBase();
                setResult(KeyParams.UPLOAD_SHORT_VIEW_RESULTCODE,null);
                finish();
                break;
            case R.id.post_alter_ok_tv:
                contentLoader.getShortVideoToken();
                postAlterOkTv.setText("上传中...");
                postAlterOkTv.setClickable(false);
                break;
        }
    }

    public class MyCallBack extends ICallBack {

        @Override
        public void onShortVideo(ShortVideoTokenBean shortVideoTokenBean) {
            String token = shortVideoTokenBean.getToken();
            parameter.setToken(token);
            upLoadVideoQiNiu(parameter);
        }

        @Override
        public void onShortVideoComplete(String json) {
            super.onShortVideoComplete(json);
            List<PostShortVideoParameterBean> all = DataSupport.findAll(PostShortVideoParameterBean.class);

            if (all != null && all.size() > 0) {
                for (PostShortVideoParameterBean bean : all) {
                    AppLog.d("TAG", "据库getFilename：" + bean.getFilename());
                    if (bean.getFilename().equals(parameter.getFilename())) {
                        DataSupport.deleteAll(PostShortVideoParameterBean.class, "filename=?", parameter.getFilename());
                        break;
                    }
                }
            }
            setResult(KeyParams.UPLOAD_SHORT_VIEW_SUCCESS_RESULTCODE_,null);
            finish();
        }
    }

    boolean isUploadOk = false;
    int progress = 0;

    private void upLoadVideoQiNiu(final PostShortVideoParameterBean parameter) {

        AppLog.d("TAG", "视频文件路径：" + parameter.getVideoPath() + "      :" + parameter.getFilename() + "      :" + parameter.getToken());
        uploadManager.put(parameter.getVideoPath(), parameter.getFilename(), parameter.getToken(),
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        if (info.isOK()) {
                            AppLog.i("TAG", "短视频上传完成");
                            isUploadOk = true;
                            postStatusIv.setImageResource(R.drawable.successful_icon);
                            contentLoader.getShortVideo(parameter);
                        } else {
                            if(info.statusCode==-1004){//网络错误
                                postStatusIv.setVisibility(View.VISIBLE);
                                postStatusIv.setImageResource(R.drawable.sendagain_ic);
                                postAlterOkTv.setVisibility(View.VISIBLE);
                                postAlterOkTv.setText("重新上传");

                            }

                            AppLog.i("TAG", "短视频上传失败   " + info.statusCode + " key :" + "  info:" + info.toString());
                        }
                    }
                }, new UploadOptions(null, null, false,
                        new UpProgressHandler() {
                            public void progress(String key, double percent) {
                                progress = (int) (percent * 1000);
                                uploadProgress.setProgress(progress);
                            }
                        }, new UpCancellationSignal() {
                    @Override
                    public boolean isCancelled() {
                        return isCancelled;
                    }
                }));
    }

    private void configQiNiu() {
        //断点上传
        String dirPath = "/storage/emulated/0/Download";
        Recorder recorder = null;
        try {
            File f = File.createTempFile("qiniu_point", ".tmp");
            AppLog.d("TAG", "断点上传:" + f.getAbsolutePath().toString());
            dirPath = f.getParent();
            recorder = new FileRecorder(dirPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String dirPath1 = dirPath;
        KeyGenerator keyGen = new KeyGenerator() {
            public String gen(String key, File file) {
                String path = key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
                AppLog.d("TAG", "KeyGenerator:" + path);
                File f = new File(dirPath1, UrlSafeBase64.encodeToString(path));
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(f));
                    String tempString = null;
                    int line = 1;
                    try {
                        while ((tempString = reader.readLine()) != null) {
                            AppLog.d("TAG", "line " + line + ": " + tempString);
                            line++;
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        AppLog.d("TAG", "  Auto-generated catch block:");
                        e.printStackTrace();

                    } finally {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    AppLog.d("TAG", "  Auto-generated catch block:FileNotFoundException");
                    e.printStackTrace();
                }
                return path;
            }
        };
        Configuration config = new Configuration.Builder()
                .recorder(recorder, keyGen)
                .zone(Zone.httpAutoZone)
                .build();

        uploadManager = new UploadManager(config);
    }

  public  void   saveDataBase(){
      if (!isUploadOk) {
          List<PostShortVideoParameterBean> all = DataSupport.findAll(PostShortVideoParameterBean.class);
          AppLog.d("TAG", "科技考核反馈哈空间后方可j:  " + parameter.getFilename());
          if (all != null && all.size() > 0) {
              for (PostShortVideoParameterBean bean : all) {
                  AppLog.d("TAG", "据库getFilename：" + bean.getFilename());
                  if (bean.getFilename().equals(parameter.getFilename())) {
                      AppLog.d("TAG", "科技考核反馈哈空间后方可j");
                      DataSupport.deleteAll(PostShortVideoParameterBean.class, "filename=?", parameter.getFilename());
                      break;
                  }
              }
          }
          AppLog.d("TAG","柏村视频，。。。。。。。");
          parameter.setProgress(progress);
          parameter.save();
      }
    }

    //201701121634217575204984150.mp4
    @Override
    protected void onPause() {
        super.onPause();
     //   isCancelled = true;
        AppLog.d("TAG","上传短视频走了。。。。onPause");
     //   saveDataBase();
    }

    @Override
    public void onBackPressed() {
        isCancelled = true;
        saveDataBase();
        setResult(KeyParams.UPLOAD_SHORT_VIEW_RESULTCODE,null);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
