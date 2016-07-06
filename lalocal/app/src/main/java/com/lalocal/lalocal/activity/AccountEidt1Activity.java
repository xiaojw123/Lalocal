package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.FileUploadUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.PhotoSelectDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountEidt1Activity extends BaseActivity implements View.OnClickListener, PhotoSelectDialog.OnDialogClickListener, CustomTitleView.onBackBtnClickListener {
    public static final int UPDATE_ME_DATA = 301;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final int MODIFY_USER_PROFILE = 4;
    private File tempFile;
    CircleImageView personalheader_civ;
    RelativeLayout nickname_rlt;
    TextView nickaname_tv;
    CheckBox boysex_cb, girlsex_cb;
    FrameLayout boysex_fl, girlsex_fl;
    RelativeLayout email_rlt;
    RelativeLayout phone_rlt;
    TextView phone_tv;
    TextView areacode_tv;
    TextView email_tv;
    ContentService contentService;
    LoginUser user;
    int sex = -1;
    boolean isEmailUpdate;
    Intent backIntent = new Intent();
    Bitmap bitmap;
    CustomTitleView customTitleView;

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
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
        contentService.getUserProfile(getUserId(), getToken());

    }


    private void initView() {
        customTitleView = (CustomTitleView) findViewById(R.id.account_eidt1_titleview);
        personalheader_civ = (CircleImageView) findViewById(R.id.account_edit_personalheader);
        nickname_rlt = (RelativeLayout) findViewById(R.id.account_edit_nickname);
        nickaname_tv = (TextView) findViewById(R.id.account_edit_nickname_text);
        boysex_fl = (FrameLayout) findViewById(R.id.accout_edit_boy_sex_fl);
        girlsex_fl = (FrameLayout) findViewById(R.id.accout_edit_girl_sex_fl);
        boysex_cb = (CheckBox) findViewById(R.id.accout_edit_boy_sex_cb);
        girlsex_cb = (CheckBox) findViewById(R.id.accout_edit_girl_sex_cb);
        phone_rlt = (RelativeLayout) findViewById(R.id.account_edit_phone);
        areacode_tv = (TextView) findViewById(R.id.account_edit_areacode_text);
        phone_tv = (TextView) findViewById(R.id.acount_edit_phone_text);
        email_rlt = (RelativeLayout) findViewById(R.id.account_edit_email);
        email_tv = (TextView) findViewById(R.id.acount_edit_email_text);
        personalheader_civ.setOnClickListener(this);
        nickname_rlt.setOnClickListener(this);
        boysex_fl.setOnClickListener(this);
        girlsex_fl.setOnClickListener(this);
        boysex_cb.setOnClickListener(this);
        girlsex_cb.setOnClickListener(this);
        phone_rlt.setOnClickListener(this);
        email_rlt.setOnClickListener(this);
        customTitleView.setOnBackClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.account_edit_personalheader:
                PhotoSelectDialog dialog = new PhotoSelectDialog(this);
                dialog.setButtonClickListener(this);
                dialog.show();
                break;
            case R.id.account_edit_nickname:
                startEditIntent(AccountEidt2Activity.ACTION_NICKNAME_MODIFY);
                break;
            case R.id.accout_edit_boy_sex_fl:
                sex = 1;
                girlsex_cb.setChecked(false);
                boysex_cb.setChecked(true);
                contentService.modifyUserProfile(null, sex, null, null, user.getId(), getToken());
                break;

            case R.id.accout_edit_girl_sex_fl:
                sex = 0;
                girlsex_cb.setChecked(true);
                boysex_cb.setChecked(false);
                contentService.modifyUserProfile(null, sex, null, null, user.getId(), getToken());
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
        intent.putExtra(KeyParams.USERID, user.getId());
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
            Toast.makeText(this, "未找到存储卡，无法存储照片",
                    Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "未找到存储卡，无法存储照片",
                        Toast.LENGTH_SHORT).show();
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
            }

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
                photoGraph();
                break;

        }


    }


    class CallBack extends ICallBack {

        @Override
        public void onGetUserProfile(LoginUser user) {
            updateUserProfileView(user);
        }
    }

    private void updateUserProfileView(LoginUser user) {
        this.user = user;
        if (!isEmailUpdate) {
            DrawableUtils.displayImg(this, personalheader_civ, user.getAvatar());
            nickaname_tv.setText(user.getNickName());
            if (user.isSex()) {
                boysex_cb.setChecked(true);
                girlsex_cb.setChecked(false);
            } else {
                girlsex_cb.setChecked(true);
                boysex_cb.setChecked(false);
            }
            if (!TextUtils.isEmpty(user.getPhone())) {
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
        backIntent.putExtra(KeyParams.STATUS, status);
        backIntent.putExtra(KeyParams.USERID, user.getId());
        backIntent.putExtra(KeyParams.TOKEN, user.getToken());
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

}
