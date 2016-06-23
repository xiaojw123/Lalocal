package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.FileUploadUtil;
import com.lalocal.lalocal.view.dialog.PhotoSelectDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountEidt1Activity extends AppCompatActivity implements View.OnClickListener, PhotoSelectDialog.OnDialogClickListener {
    public static final int RESULT_CODE_MODFY_PROFILE = 123;
    public static final int UPDATE_ME_DATA = 301;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final int MODIFY_USER_PROFILE = 4;
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    CircleImageView personalheader_civ;
    RelativeLayout nickname_rlt;
    TextView nickaname_tv;
    CheckBox boysex_cb, girlsex_cb;
    RelativeLayout email_rlt;
    RelativeLayout phone_rlt;
    TextView phone_tv;
    TextView areacode_tv;
    TextView email_tv;
    int outputX, outputY;
    ContentService contentService;
    LoginUser user;
    ImageView backImg;
    int sex = -1;
    boolean isEmailUpdate;
    Intent backIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_eidt1_layout);
        initService();
        intParams();
        initView();
        contentService.getUserProfile(getUserId(), getToken());
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

    }

    private void intParams() {
        outputX = (int) getResources().getDimension(R.dimen.home_me_headportrait_img_width);
        outputY = outputX;


    }

    private void initView() {
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        personalheader_civ = (CircleImageView) findViewById(R.id.account_edit_personalheader);
        nickname_rlt = (RelativeLayout) findViewById(R.id.account_edit_nickname);
        nickaname_tv = (TextView) findViewById(R.id.account_edit_nickname_text);
        boysex_cb = (CheckBox) findViewById(R.id.accout_edit_boy_sex_cb);
        girlsex_cb = (CheckBox) findViewById(R.id.accout_edit_girl_sex_cb);
        phone_rlt = (RelativeLayout) findViewById(R.id.account_edit_phone);
        areacode_tv = (TextView) findViewById(R.id.account_edit_areacode_text);
        phone_tv = (TextView) findViewById(R.id.acount_edit_phone_text);
        email_rlt = (RelativeLayout) findViewById(R.id.account_edit_email);
        email_tv = (TextView) findViewById(R.id.acount_edit_email_text);
        backImg.setOnClickListener(this);
        personalheader_civ.setOnClickListener(this);
        nickname_rlt.setOnClickListener(this);
        boysex_cb.setOnClickListener(this);
        girlsex_cb.setOnClickListener(this);
        phone_rlt.setOnClickListener(this);
        email_rlt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.common_back_btn:
                setResult(UPDATE_ME_DATA, backIntent);
                finish();
                break;
            case R.id.account_edit_personalheader:
                PhotoSelectDialog dialog = new PhotoSelectDialog(this);
                dialog.setButtonClickListener(this);
                dialog.show();
                break;
            case R.id.account_edit_nickname:
                startEditIntent(AccountEidt2Activity.ACTION_NICKNAME_MODIFY);
                break;
            case R.id.accout_edit_boy_sex_cb:
                sex = 1;
                if (girlsex_cb.isChecked()) {
                    girlsex_cb.setChecked(false);
                }
                boysex_cb.setChecked(true);
                contentService.modifyUserProfile(null, sex, null, null, user.getId(), getToken());

                break;
            case R.id.accout_edit_girl_sex_cb:
                sex = 0;
                if (boysex_cb.isChecked()) {
                    boysex_cb.setChecked(false);
                }
                girlsex_cb.setChecked(true);
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
        Intent intent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            tempFile = new File(Environment
                    .getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
            Uri uri = Uri.fromFile(tempFile);
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
                final Bitmap bitmap = data.getParcelableExtra("data");
                personalheader_civ.setImageBitmap(bitmap);
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    bitmap.recycle();
                    bos.flush();
                    bos.close();
                    byte[] bytes = bos.toByteArray();
                    if (user != null) {
                        FileUploadUtil.uploadFile(this, backIntent, bytes, user.getId(), getToken());
                    }
                    bitmap.recycle();
                    bos.flush();
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (tempFile != null && tempFile.exists())
                    tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
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
                AppLog.print("modify nickanme__"+nickname);
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
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
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
        public void onGetUserProfile(int code, LoginUser user) {
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
        backIntent.putExtra(KeyParams.STATUS, status);
        if (status == -1) {
            email_tv.setText(user.getEmail() + "(未验证)");
        } else {
            email_tv.setText(user.getEmail());
        }

    }

    @Override
    public void onBackPressed() {
        setResult(UPDATE_ME_DATA, backIntent);
        super.onBackPressed();
    }

}
