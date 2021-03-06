package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.cunoraz.gifview.library.GifView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.live.entertainment.adapter.PhotoAdapter;
import com.lalocal.lalocal.live.entertainment.img.GlideImageLoader;
import com.lalocal.lalocal.live.entertainment.listener.GlidePauseOnScrollListener;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.ImgTokenResult;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.QiniuUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.PauseOnScrollListener;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * create by Wangjie
 *
 * 举报Activity
 *
 * 举报氛围用户举报和主播举报，其中主播举报可以多次，用户举报只能对同一名用户举报一次
 *
 */
public class ReportActivity extends BaseActivity {

    @BindView(R.id.et_report)
    EditText mEtReport;
    @BindView(R.id.rb_uncivilized)
    RadioButton mRbUncivilized;
    @BindView(R.id.rb_illegal)
    RadioButton mRbIllegal;
    @BindView(R.id.rb_other)
    RadioButton mRbOther;
    @BindView(R.id.gv_report_pic)
    GridView mGvReportPic;
    @BindView(R.id.btn_confirm_report)
    Button mBtnConfirmReport;
    @BindView(R.id.btn_close_report)
    ImageButton btnCloseReport;
    @BindView(R.id.layout_close)
    RelativeLayout layoutClose;
    @BindView(R.id.loading)
    GifView loading;

    // 图片适配器
    private PhotoAdapter mPhotoAdapter;

    // 主题配置
    private ThemeConfig mThemeConfig;
    // 图片加载器
    private cn.finalteam.galleryfinal.ImageLoader mGlidImgLoader;
    // 滚动监听事件
    private PauseOnScrollListener mPauseOnScrollListener;
    // 功能配置
    private FunctionConfig mFunctionConfig;
    // 核心配置
    private CoreConfig mCoreConfig;

    // 选中的图片列表，用于显示
    private List<PhotoInfo> mPhotoList = new ArrayList<>();
    // 选中的图片列表，用于上传
    private List<PhotoInfo> mUploadPhotoList = new ArrayList<>();
    // 成功上传的图片名称列表
    private List<String> mUploadSuccess = new ArrayList<>();

    private ContentLoader mContentLoader;

    // -intent传入数据
    private String mUserId;
    private String mMasterName;
    private String mChannelId;

    // 举报理由
    private String mContent;

    // 标记当前传第几张图片
    private int mCurUpload = 0;
    // 标记当前选中要上传的图片
    private int mCurSelectPic = 0;

    // 选中按钮的选中标签
    private int mSelected = 0;

    // 文件名称的数组
    private String[] mPhotos;

    // 举报来自（主播/用户）
    private int mReportFrom = Constants.REPORT_CHANNEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        // 使用ButterKnife框架
        ButterKnife.bind(this);
        // 解析intent传入数据
        parseIntent();
        // 初始化视图
        initView();
        // 初始化ContentLoader
        initContentLoader();
    }

    /**
     * 解析intent传入数据
     */
    private void parseIntent() {
        // 获取意图
        Intent getData = getIntent();

        // -获取bundle
        if (getData == null) {
            return;
        }
        Bundle bundle = getData.getExtras();

        // -获取key的值
        if (bundle == null) {
            return;
        }
        // 获取被举报的用户id
        mUserId = bundle.getString(Constants.KEY_USER_ID);
        // 获取被举报的用户昵称
        mMasterName = bundle.getString(Constants.KEY_MASTER_NAME);
        // 获取当前所在的直播间id
        mChannelId = bundle.getString(Constants.KEY_CHANNEL_ID);
        // 获取举报来源
        mReportFrom = bundle.getInt(Constants.KEY_REPORT_FROM);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        // 初始化单选按钮
        initRadio();
        // 初始化选中的图片列表
        initSelectedPhoto();
    }

    /**
     * 初始化ContentLoader
     */
    private void initContentLoader() {
        try {
            mContentLoader = new ContentLoader(this);
            mContentLoader.setCallBack(new MyCallBack());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化单选按钮
     */
    private void initRadio() {
        final List<RadioButton> btns = new ArrayList<>();
        btns.add(mRbUncivilized);
        btns.add(mRbIllegal);
        btns.add(mRbOther);

        // 初始选中的按钮
        selectRadioBtn(btns, 0);

        for (int i = 0; i < btns.size(); i++) {
            final int finalI = i;
            btns.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectRadioBtn(btns, finalI);
                }
            });
        }
    }

    /**
     * 选择单选按钮
     *
     * @param btns
     * @param selected
     */
    private void selectRadioBtn(List<RadioButton> btns, int selected) {
        mSelected = selected;
        for (int i = 0; i < btns.size(); i++) {
            if (selected == i) {
                btns.get(i).setButtonDrawable(R.drawable.only_checkbox_sel);
                btns.get(i).setPadding(DensityUtil.dip2px(ReportActivity.this, 8), 0, 0, 0);

                if (i == 2) {
                    mEtReport.setVisibility(View.VISIBLE);
                } else {
                    mEtReport.setVisibility(View.GONE);
                }
            } else {
                btns.get(i).setButtonDrawable(R.drawable.only_checkbox_unsel);
                btns.get(i).setPadding(DensityUtil.dip2px(ReportActivity.this, 8), 0, 0, 0);
            }
        }
    }

    /**
     * 初始化选中的图片列表
     */
    private void initSelectedPhoto() {
        // 添加占位图片
        addEmptyPhoto(mPhotoList);

        // 初始化适配器
        mPhotoAdapter = new PhotoAdapter(ReportActivity.this, mPhotoList);
        // 设置适配器
        mGvReportPic.setAdapter(mPhotoAdapter);
        // “添加”图片点击事件
        mGvReportPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取最后的下标
                int lastIndex = mPhotoAdapter.getCount() - 1;
                // 如果当前点击的是最后一张图片(“添加”图片)
                if (position == lastIndex) {
                    MobHelper.sendEevent(ReportActivity.this, MobEvent.LIVE_USER_REPORT_PICTURE);
                    // 启动GalleryFinal
                    startGlideGalleryFinal();
                }
            }
        });
    }

    /**
     * 添加一个占位图片
     *
     * @param list
     */
    private void addEmptyPhoto(List<PhotoInfo> list) {
        // 声明图片类
        PhotoInfo photoInfo = new PhotoInfo();
        // 设置图片的id
        photoInfo.setPhotoId(-999);
        // 设置标记
        photoInfo.setPhotoPath(Constants.FLAG_ADD_PIC);
        // 起到占位的作用
        list.add(photoInfo);
    }

    /**
     * 启动GalleryFinal
     */
    private void startGlideGalleryFinal() {
        // 获取标题栏背景颜色
        int colorTitleBarBg = ContextCompat.getColor(ReportActivity.this, R.color.titleBarBgColor);
        // 标题栏文字颜色
        int colorTitleBarText = ContextCompat.getColor(ReportActivity.this, R.color.titleBarTextColor);
        // 浮动按钮常规颜色
        int colorFabNormal = ContextCompat.getColor(ReportActivity.this, R.color.color_ffaa2a);
        // 浮动按钮点击颜色
        int colorFabPressed = ContextCompat.getColor(ReportActivity.this, R.color.color_e29428);
        // 图标颜色
        int colorIcon = ContextCompat.getColor(ReportActivity.this, R.color.color_191000);

        // 设置主题
        mThemeConfig = new ThemeConfig.Builder()
                .setTitleBarBgColor(colorTitleBarBg) // 设置标题栏背景颜色
                .setTitleBarTextColor(colorTitleBarText)    // 设置标题栏文字颜色
                .setFabNormalColor(colorFabNormal)  // 设置浮动按钮常规颜色
                .setFabPressedColor(colorFabPressed)    // 设置浮动按钮点击颜色
                .setTitleBarIconColor(colorIcon) // 设置标题栏图标颜色
                .build();

        // 初始化图片加载器
        mGlidImgLoader = new GlideImageLoader();

        // 初始化监听事件
        mPauseOnScrollListener = new GlidePauseOnScrollListener(false, true);

        // 初始化功能配置
        FunctionConfig.Builder funConBuilder = new FunctionConfig.Builder();
        // 设置最多可选择5张照图片
        funConBuilder.setMutiSelectMaxSize(5);
        // 设置图片不可编辑
        funConBuilder.setEnableEdit(false);
        // 设置图片不可旋转
        funConBuilder.setEnableRotate(false);
        // 设置图片不可裁剪
        funConBuilder.setEnableCrop(false);
        // 设置不可通过照相选择照片
        funConBuilder.setEnableCamera(false);
        // 不过滤图片，而是将之前选中的图片设置为选中状态
        funConBuilder.setSelected(mPhotoList);
        // 设置可预览
        funConBuilder.setEnablePreview(true);
        // 功能配置
        mFunctionConfig = funConBuilder.build();

        // 初始化核心配置
        mCoreConfig = new CoreConfig.Builder(ReportActivity.this, mGlidImgLoader, mThemeConfig)
                .setFunctionConfig(mFunctionConfig) // 添加功能配置
                .setPauseOnScrollListener(mPauseOnScrollListener) // 滑动停止加载事件
                .setNoAnimcation(true) // 无特效动画
                .build();

        // 实例化GalleryFinal
        GalleryFinal.init(mCoreConfig);
        // 多图片选择打开相册
        GalleryFinal.openGalleryMuti(Constants.REQUEST_CODE_GALLERY, mFunctionConfig,
                mOnHandlerResultCallback);
        // 初始化图片加载器
        initImageLoader(ReportActivity.this);
    }

    /**
     * 初始化图片加载器
     *
     * @param context
     */
    private void initImageLoader(Context context) {
        // 图片加载器配置
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
        // 设置线程优先级
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        // 禁止内存缓存
        config.denyCacheImageMultipleSizesInMemory();
        // 设置磁盘缓存文件名生成器
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        // 设置磁盘缓存大小
        config.diskCacheSize(20 * 1024 * 1024);
        // 设置任务进程执行顺序：先进后出
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        // 调试使用，若是发布版，需要移除代码
        config.writeDebugLogs();

        // 初始化图片加载器
        ImageLoader.getInstance().init(config.build());
    }

    /**
     * 回调处理
     */
    private GalleryFinal.OnHanlderResultCallback mOnHandlerResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            // 将返回的图片赋给上传的图片列表
            mUploadPhotoList.clear();
            mUploadPhotoList.addAll(resultList);
            // 没有图片返回的时候，也可以进行操作：清空之前选中的图片

            // 添加占位图片
            addEmptyPhoto(resultList);

            // 清除原来列表中的图片
            mPhotoList.clear();
            // 返回图片列表
            mPhotoList.addAll(resultList);
            // 刷新页面
            mPhotoAdapter.notifyDataSetChanged();
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            // 错误提示
            Toast.makeText(ReportActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick({R.id.btn_close_report, R.id.btn_confirm_report})
    void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.btn_close_report:
                MobHelper.sendEevent(this, MobEvent.LIVE_USER_REPORT_CANCEL);
                ReportActivity.this.finish();
                break;
            case R.id.btn_confirm_report:
                loading.setVisibility(View.VISIBLE);
                // 举报
                report();
                MobHelper.sendEevent(this, MobEvent.LIVE_USER_REPORT_SURE);
                break;
        }
    }

    /**
     * 举报
     */
    private void report() {
        if (mSelected == 0) {
            mContent = "不文明直播";
        } else if (mSelected == 1) {
            mContent = "触犯法律法规";
        } else if (mSelected == 2) {
            mContent = mEtReport.getText().toString().trim();
            if (TextUtils.isEmpty(mContent)) {
                Toast.makeText(ReportActivity.this, "请输入有效的举报原因", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // -上传图片
        int size = mUploadPhotoList.size();
        // 如果无图片，直接上传举报借口
        if (size == 0) {
            // 初始化图片名称字符串数组
            mPhotos = new String[0];
            // 将举报信息上传服务器
            mContentLoader.getChannelReport(mReportFrom, mContent, mPhotos, mUserId, mMasterName, mChannelId);
            return;
        }
        for (int i = 0; i < size; i++) {
            // 获取图片token
            mContentLoader.getImgToken();
        }
    }

    /**
     * 回调事件类
     */
    private class MyCallBack extends ICallBack {
        @Override
        public void onImgToken(ImgTokenBean imgTokenBean) {
            super.onImgToken(imgTokenBean);

            if (imgTokenBean.getReturnCode() == 0) {
                ImgTokenResult result = imgTokenBean.getResult();
                if (result == null) {
                    Toast.makeText(ReportActivity.this, "获取Token失败", Toast.LENGTH_SHORT).show();
                    resetReport();
                    return;
                }

                // 获取文件名
                final String fileName = result.getFilename();
                // 获取
                String token = result.getToken();
                // 获取文件信息对象
                PhotoInfo photoInfo = mUploadPhotoList.get(mCurSelectPic);
                // 上传下标
                mCurSelectPic++;
                // 获取文件路径
                String filePath = photoInfo.getPhotoPath();

                QiniuUtils.uploadSimpleFile(filePath, fileName, token, new QiniuUtils.OnUploadListener() {
                    @Override
                    public void onUploadSuccess(ResponseInfo info) {
                        // 将文件名称添加到名称数组中
                        mUploadSuccess.add(fileName);
                    }

                    @Override
                    public void onUploadFail(ResponseInfo info) {

                    }

                    @Override
                    public void afterUpload(ResponseInfo info) {
                        // 当前上传的图片标记
                        mCurUpload++;
                        // 如果图片上传完毕
                        if (mCurUpload == mUploadPhotoList.size()) {
                            // 标签重置
                            mCurUpload = 0;
                            mCurSelectPic = 0;
                            // 上传成功的图片数量
                            int picSize = mUploadSuccess.size();
                            // 将上传成功的图片列表传入数组
                            mPhotos = new String[picSize];
                            // 列表转数组
                            for (int i = 0; i < picSize; i++) {
                                mPhotos[i] = mUploadSuccess.get(i);
                            }
                            // 清空上传成功的列表
                            mUploadSuccess.clear();
                            // 将举报信息上传服务器
                            mContentLoader.getChannelReport(mReportFrom, mContent, mPhotos, mUserId, mMasterName, mChannelId);
                        }
                    }
                });
            } else {
                loading.setVisibility(View.GONE);
                Toast.makeText(ReportActivity.this, "访问数据接口失败，请检查网络", Toast.LENGTH_SHORT).show();
                resetReport();
            }

        }

        /**
         * 上传简单文件
         *
         * @param filePath 文件路径
         * @param fileName 指定上传到七牛云上后图片的文件名，后台获取token的时候有fileName
         * @param token    从服务端获取的token
         * @return 图片是否上传成功
         */
        public void uploadSimpleFile(String filePath, final String fileName, String token) {
            UploadManager uploadManager = new UploadManager();
            uploadManager.put(filePath, fileName, token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject response) {
                            if (info.statusCode == 200) {
                                // 将文件名称添加到名称数组中
                                mUploadSuccess.add(fileName);
                            }
                            // 当前上传的图片标记
                            mCurUpload++;
                            // 如果图片上传完毕
                            if (mCurUpload == mUploadPhotoList.size()) {
                                // 标签重置
                                mCurUpload = 0;
                                mCurSelectPic = 0;
                                // 上传成功的图片数量
                                int picSize = mUploadSuccess.size();
                                // 将上传成功的图片列表传入数组
                                mPhotos = new String[picSize];
                                // 列表转数组
                                for (int i = 0; i < picSize; i++) {
                                    mPhotos[i] = mUploadSuccess.get(i);
                                }
                                // 清空上传成功的列表
                                mUploadSuccess.clear();
                                // 将举报信息上传服务器
                                mContentLoader.getChannelReport(mReportFrom, mContent, mPhotos, mUserId, mMasterName, mChannelId);
                            }
                        }
                    }, null);
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
            resetReport();
        }

        /**
         * 重置举报
         */
        private void resetReport() {
            mCurUpload = 0;
            mCurSelectPic = 0;
            loading.setVisibility(View.GONE);
        }

        @Override
        public void onGetChannelReport(String json) {
            super.onGetChannelReport(json);
            try {
                // json解析
                JSONObject response = new JSONObject(json);
                // 获取json中的message
                String message = response.getString("message");
                // 隐藏加载
                loading.setVisibility(View.GONE);
                // 如果上传成功
                if ("success".equals(message)) {
                    // 显示举报成功
                    Toast.makeText(ReportActivity.this, "举报成功", Toast.LENGTH_SHORT).show();
                    // 退出举报页面
                    ReportActivity.this.finish();
                } else {
                    // 显示举报失败信息
                    Toast.makeText(ReportActivity.this, "您已举报过此内容，请勿重复举报", Toast.LENGTH_SHORT).show();
                    // 退出举报页面
                    ReportActivity.this.finish();
                }
            } catch (JSONException e) {
                // 举报信息返回失败提示
                Toast.makeText(ReportActivity.this, "举报响应出错了，请稍后再试", Toast.LENGTH_SHORT).show();
            } finally {
                resetReport();
            }
        }
    }
}
