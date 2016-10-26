package com.lalocal.lalocal.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.ErrorMessage;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.FileUploadUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.PhotoSelectDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class AccountEidt1Activity extends BaseActivity implements View.OnClickListener, PhotoSelectDialog.OnDialogClickListener, CustomTitleView.onBackBtnClickListener {
    public static final int UPDATE_ME_DATA = 301;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final int MODIFY_USER_PROFILE = 4;
    private static final int LOGIN_RQEUST_CODE = 5;
    private File tempFile;
    ImageView personalheader_civ;
    RelativeLayout nickname_rlt;
    TextView nickaname_tv;
    RelativeLayout email_rlt;
    RelativeLayout phone_rlt;
    TextView phone_tv;
    TextView areacode_tv;
    TextView email_tv;
    TextView sex_tv;
    ContentLoader contentService;
    LoginUser user;
    boolean isEmailUpdate;
    Intent backIntent = new Intent();
    Bitmap bitmap;
    CustomTitleView customTitleView;
    Dialog sexSelecorDialog;
    RelativeLayout editsex_rlt;
    TextView description_tv;
    RelativeLayout description_rlt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_eidt1_layout);
        initView();
        initService();
    }

    public int getUserId() {
        return getIntent().getIntExtra(KeyParams.USERID, -1);
    }

    public String getToken() {

        return getIntent().getStringExtra(KeyParams.TOKEN);
    }

    private void initService() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new CallBack());
        contentService.getUserProfile(getUserId(), getToken());

    }


    private void initView() {
        description_tv = (TextView) findViewById(R.id.acount_edit_description_text);
        description_rlt = (RelativeLayout) findViewById(R.id.account_edit_description);
        editsex_rlt = (RelativeLayout) findViewById(R.id.account_edit_sex);
        sex_tv = (TextView) findViewById(R.id.account_edit_sex_text);
        customTitleView = (CustomTitleView) findViewById(R.id.account_eidt1_titleview);
        personalheader_civ = (ImageView) findViewById(R.id.account_edit_personalheader);
        nickname_rlt = (RelativeLayout) findViewById(R.id.account_edit_nickname);
        nickaname_tv = (TextView) findViewById(R.id.account_edit_nickname_text);
        phone_rlt = (RelativeLayout) findViewById(R.id.account_edit_phone);
        areacode_tv = (TextView) findViewById(R.id.account_edit_areacode_text);
        phone_tv = (TextView) findViewById(R.id.acount_edit_phone_text);
        email_rlt = (RelativeLayout) findViewById(R.id.account_edit_email);
        email_tv = (TextView) findViewById(R.id.acount_edit_email_text);
        editsex_rlt.setOnClickListener(this);
        personalheader_civ.setOnClickListener(this);
        nickname_rlt.setOnClickListener(this);
        phone_rlt.setOnClickListener(this);
        email_rlt.setOnClickListener(this);
        description_rlt.setOnClickListener(this);
        customTitleView.setOnBackClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.account_edit_description:
                Intent desIntent = new Intent(this, PersonalIntroActivity.class);
                String des = description_tv.getText().toString();
                if (getResources().getString(R.string.default_description).equals(des)) {
                    des="";
                }
                desIntent.putExtra(KeyParams.DESCRIPTON, des);
                startActivityForResult(desIntent, MODIFY_USER_PROFILE);
                break;

            case R.id.account_edit_sex:
                showSexSelectorDialog();
                break;

            case R.id.sex_selector_man_tv:
                sexSelecorDialog.dismiss();
                contentService.modifyUserProfile(null, 1, null, null, null, user.getId(), getToken());
                break;
            case R.id.sex_selector_woman_tv:
                sexSelecorDialog.dismiss();
                contentService.modifyUserProfile(null, 0, null, null, null, user.getId(), getToken());
                break;
            case R.id.account_edit_personalheader:
                PhotoSelectDialog dialog = new PhotoSelectDialog(this);
                dialog.setButtonClickListener(this);
                dialog.show();
                break;
            case R.id.account_edit_nickname:
                startEditIntent(AccountEidt2Activity.ACTION_NICKNAME_MODIFY);
                break;

            case R.id.account_edit_phone:
                startEditIntent(AccountEidt2Activity.ACTION_PHONE_MODIFY);
                break;
            case R.id.account_edit_email:
                if (user != null && user.getStatus() == 0) {
                    Intent intent = new Intent(this, EmailBoundActivity.class);
                    intent.putExtra(KeyParams.EMAIL, user.getEmail());
                    intent.putExtra(KeyParams.USERID, user.getId());
                    intent.putExtra(KeyParams.TOKEN, getToken());
                    startActivityForResult(intent, MODIFY_USER_PROFILE);
                } else {
                    startEditIntent(AccountEidt2Activity.ACTION_EMAIL_MODIFY);

                }
                break;


        }


    }

    private void startEditIntent(int action) {
        Intent intent = new Intent(this, AccountEidt2Activity.class);
        intent.putExtra(AccountEidt2Activity.ACTION_TYPE, action);
        switch (action) {
            case AccountEidt2Activity.ACTION_NICKNAME_MODIFY:
                intent.putExtra("nickname", nickaname_tv.getText().toString());
                break;
            case AccountEidt2Activity.ACTION_EMAIL_MODIFY:
                intent.putExtra("emailtext", email_tv.getText().toString());
                break;
            case AccountEidt2Activity.ACTION_PHONE_MODIFY:
                intent.putExtra(KeyParams.AREA_Code, areacode_tv.getText().toString());
                intent.putExtra(KeyParams.PHONE, phone_tv.getText().toString());
                break;
        }
        if (user != null) {
            intent.putExtra(KeyParams.USERID, user.getId());
        }
        intent.putExtra(KeyParams.TOKEN, getToken());
        startActivityForResult(intent, MODIFY_USER_PROFILE);
    }


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
                        personalheader_civ.setImageBitmap(bitmap);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        bos.flush();
                        if (user != null) {
                            FileUploadUtil.uploadFile(this, backIntent, bos.toByteArray(), user.getId(), getToken());
                        }
                        if (tempFile != null && tempFile.exists()) {
                            tempFile.delete();
                        }
                        bos.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == MODIFY_USER_PROFILE) {
            if (resultCode == EmailBoundActivity.RESULIT_CODE_BOUND_EMAIL) {
                isEmailUpdate = true;
                contentService.getUserProfile(getUserId(), getToken());
            } else if (resultCode == AccountEidt2Activity.RESULT_CODE_PHONE) {
                String phone = data.getStringExtra(KeyParams.PHONE);
                String areaCode = data.getStringExtra(KeyParams.AREA_Code);
                if (phone != null) {
                    if (areaCode != null) {
                        areacode_tv.setText(areaCode);
                    }
                    phone_tv.setText(phone);
                }
            } else if (resultCode == AccountEidt2Activity.RESULT_CODE_NICKNAME) {
                String nickname = data.getStringExtra(KeyParams.NICKNAME);
                backIntent.putExtra(KeyParams.NICKNAME, nickname);
                nickaname_tv.setText(nickname);
            } else if (resultCode == PersonalIntroActivity.RESULT_CODE_PERINTRO) {
                String descripton = data.getStringExtra(KeyParams.DESCRIPTON);
                if (!TextUtils.isEmpty(descripton)) {
                    description_tv.setText(descripton);
                }
            }

        } else if (requestCode == LOGIN_RQEUST_CODE) {
            setResult(resultCode, data);
            finish();
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


    class CallBack extends ICallBack {
        @Override
        public void onModifyUserProfile(LoginUser user) {
            if (user.isSex()) {
                sex_tv.setText(getResources().getString(R.string.man));
            } else {
                sex_tv.setText(getResources().getString(R.string.woman));
            }
        }

        @Override
        public void onGetUserProfile(LoginUser user) {
            updateUserProfileView(user);
        }

        @Override
        public void onError(VolleyError volleyError) {
            if (ErrorMessage.AUTHOR_FIALED.equals(volleyError.toString())) {
                UserHelper.updateSignOutInfo(AccountEidt1Activity.this);
                Intent intent = new Intent(AccountEidt1Activity.this, LoginActivity.class);
                startActivityForResult(intent, LOGIN_RQEUST_CODE);
            }
        }
    }


    private void updateUserProfileView(LoginUser user) {
        this.user = user;
        if (!isEmailUpdate) {
            DrawableUtils.displayImg(this, personalheader_civ, user.getAvatar(), R.drawable.home_me_personheadnormal);
            nickaname_tv.setText(user.getNickName());
            if (user.isSex()) {
                sex_tv.setText(getResources().getString(R.string.man));
            } else {
                sex_tv.setText(getResources().getString(R.string.woman));
            }
            if (!TextUtils.isEmpty(user.getPhone())) {
                AppLog.print("updateUser___areacode___" + user.getAreaCode());
                areacode_tv.setText(user.getAreaCode());
                phone_tv.setText(user.getPhone());
            }
        } else {
            isEmailUpdate = false;
        }
        int status = user.getStatus();
        if (status == -1) {
            email_tv.setText(user.getEmail() + "(未验证)");
        } else {
            email_tv.setText(user.getEmail());
        }
        String descripton = user.getDescription();
        if (!TextUtils.isEmpty(descripton)) {
            description_tv.setText(descripton);
        } else {
            description_tv.setText(getResources().getString(R.string.default_description));
        }
        backIntent.putExtra(KeyParams.STATUS, status);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }

    }

    @Override
    public void onBackPressed() {
        setResult(UPDATE_ME_DATA, backIntent);
        super.onBackPressed();
    }

    @Override
    public void onBackClick() {
        setResult(UPDATE_ME_DATA, backIntent);
    }

    public void showSexSelectorDialog() {
        if (sexSelecorDialog == null) {
            sexSelecorDialog = new Dialog(this, R.style.test_dialog);
            View view = View.inflate(this, R.layout.sex_selector_dialog, null);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView manSelTv = (TextView) view.findViewById(R.id.sex_selector_man_tv);
            TextView womanSelTv = (TextView) view.findViewById(R.id.sex_selector_woman_tv);
            manSelTv.setOnClickListener(this);
            womanSelTv.setOnClickListener(this);
            sexSelecorDialog.setContentView(view, params);
            Window dialogWindow = sexSelecorDialog.getWindow();
            dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialogWindow.setGravity(Gravity.BOTTOM);
        }
        sexSelecorDialog.show();
    }

}
