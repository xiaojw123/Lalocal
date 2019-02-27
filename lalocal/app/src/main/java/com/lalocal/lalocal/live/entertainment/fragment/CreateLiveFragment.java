package com.lalocal.lalocal.live.entertainment.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.BaseFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveLocationActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.KeyboardUtil;
import com.lalocal.lalocal.view.dialog.PhotoSelectDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ${WCJ} on 2017/1/1.
 */
public class CreateLiveFragment extends BaseFragment implements PhotoSelectDialog.OnDialogClickListener{
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    public static final int PERMISSION_STGAT_CODE = 1123;
    public static final int LOCATION_REQUEST_CODE=500;
    private byte[] bytesImg;
    @BindView(R.id.create_layout_bg)
    View createLayoutBg;
    @BindView(R.id.live_create_room_close_iv)
    ImageView liveCreateRoomCloseIv;
    @BindView(R.id.input_start_live)
    TextView inputStartLive;
    @BindView(R.id.create_live_head_iv)
    ImageView createLiveHeadIv;
    @BindView(R.id.live_cover_layout)
    RelativeLayout liveCoverLayout;
    @BindView(R.id.create_live_location_tv)
    TextView createLiveLocationTv;
    @BindView(R.id.live_definition_choose)
    TextView liveDefinitionChoose;
    @BindView(R.id.live_definition_choose_up)
    TextView liveDefinitionChooseUp;
    @BindView(R.id.live_definition_choose_down)
    TextView liveDefinitionChooseDown;
    @BindView(R.id.live_definition_choose_layout)
    LinearLayout liveDefinitionChooseLayout;
    @BindView(R.id.live_room_name)
    EditText liveRoomName;
    @BindView(R.id.live_text_title_count)
    TextView liveTextTitleCount;
    @BindView(R.id.create_live_layout)
    RelativeLayout createLiveLayout;
    FragmentManager fm;
    private  int  getImageTokenTag=-1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==LOCATION_REQUEST_CODE&&resultCode== KeyParams.LOCATION_RESULTCODE){//从地理位置页面LiveLocationActivity返回
            if(createLiveLocationTv!=null){
                createLiveLocationTv.setText(CommonUtil.LOCATION_RESULT);
                if (roomNameLength > 0 && CommonUtil.LOCATION_Y == true) {
                    inputStartLive.setTextColor(getResources().getColor(R.color.live_start_tv));
                } else {
                    inputStartLive.setTextColor(getResources().getColor(R.color.live_start_nomal_tv));
                }
            }
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {//从相册返回
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                crop(Uri.fromFile(tempFile));
            } else {
                CommonUtil.showToast(getActivity(),getActivity().getString(R.string.not_find_sd), Toast.LENGTH_SHORT);
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            if (data != null) {
                try {
                    Bitmap bitmap = data.getParcelableExtra("data");
                    if (bitmap != null) {
                        createLiveHeadIv.setImageBitmap(bitmap);
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_live_popu_layout, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        if(CommonUtil.LOCATION_RESULT.length()>0){
            CommonUtil.LOCATION_Y=true;
            createLiveLocationTv.setText(CommonUtil.LOCATION_RESULT);
        }

        createLayoutBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(liveDefinitionChooseLayout!=null&&liveDefinitionChooseLayout.getVisibility()==View.VISIBLE){
                    liveDefinitionChooseLayout.setVisibility(View.GONE);
                    liveRoomName.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

        liveRoomName.addTextChangedListener(watcher);

    }

    @OnClick({R.id.create_live_head_iv,R.id.live_create_room_close_iv,R.id.create_live_location_tv,R.id.input_start_live,R.id.live_definition_choose_up,R.id.live_definition_choose_down,R.id.live_definition_choose})
    public void clickBtn(View view){
        switch (view.getId()){
            case R.id.live_create_room_close_iv:
                if (fm == null) {
                    fm = getFragmentManager();
                }
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(this);
                ft.commit();
                getActivity().finish();
                break;
            case R.id.create_live_head_iv:
                PhotoSelectDialog dialog = new PhotoSelectDialog(getActivity());
                dialog.setButtonClickListener(this);
                dialog.show();
                break;
            case R.id.create_live_location_tv:
                MobHelper.sendEevent(getActivity(), MobEvent.LIVE_LOCACTION_BUTTON);
                Intent intent = new Intent(getActivity(), LiveLocationActivity.class);
                startActivityForResult(intent,LOCATION_REQUEST_CODE);
                break;
            case R.id.input_start_live:
                startLiveing();
                break;
            case R.id.live_definition_choose_up:
                liveDefinitionChoose.setText(getActivity().getString(R.string.video_profile_720p));
                LiveConstant.LIVE_DEFINITION=1;
                liveDefinitionChooseLayout.setVisibility(View.GONE);
                liveRoomName.setVisibility(View.VISIBLE);
                break;
            case R.id.live_definition_choose_down:
                liveDefinitionChoose.setText(getActivity().getString(R.string.video_profile_480p));
                liveDefinitionChooseLayout.setVisibility(View.GONE);
                liveRoomName.setVisibility(View.VISIBLE);
                LiveConstant.LIVE_DEFINITION=2;
                break;
            case R.id.live_definition_choose:
                liveDefinitionChooseLayout.setVisibility(View.VISIBLE);
                liveRoomName.setVisibility(View.GONE);
                break;


        }
    }

    boolean isFirstClick=true;

    /**
     * 开启直播
     */
    private void startLiveing() {

        MobHelper.sendEevent(getActivity(), MobEvent.LIVE_START_START);
        if (isFirstClick) {
            isFirstClick = false;
          String  roomName = liveRoomName.getText().toString().trim();
            if (TextUtils.isEmpty(roomName)) {
                isFirstClick = true;
                Toast.makeText(getActivity(), getString(R.string.live_room_title_no_null), Toast.LENGTH_SHORT).show();
                return;
            }
            if (CommonUtil.LOCATION_Y==false) {
                isFirstClick = true;
                Toast.makeText(getActivity(), getString(R.string.not_get_location_please_input), Toast.LENGTH_SHORT).show();
                return;
            }
            inputStartLive.setText(getString(R.string.prepare_start_live));
            KeyboardUtil.hidenSoftKey(liveRoomName);
            if(startLiveClickListener!=null){
                startLiveClickListener.startLive(getImageTokenTag,bytesImg);
            }
        }
    }


    private int roomNameLength;
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String liveRoomNameCount = liveRoomName.getText().toString().trim();
            roomNameLength = liveRoomNameCount.length();
            if (roomNameLength > 0 && CommonUtil.LOCATION_Y == true) {
                inputStartLive.setTextColor(getResources().getColor(R.color.live_start_tv));
            } else {
                inputStartLive.setTextColor(getResources().getColor(R.color.live_start_nomal_tv));
            }
            liveTextTitleCount.setText(roomNameLength + "/20");
        }
    };
    @Override
    public void onButtonClickListner(Dialog dialog, View view) {
        dialog.dismiss();
        int id = view.getId();
        switch (id) {
            case R.id.photoalbum_btn://相册
                getImageTokenTag=1;
                searchPhotoBum();
                break;
            case R.id.photograph_btn:
                getImageTokenTag=1;
                requestUserPermission(Manifest.permission.CAMERA);
                break;
            case R.id.cancel_selectephoto_btn:
                getImageTokenTag=-1;
                break;
        }

    }

    private void searchPhotoBum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,
                PHOTO_REQUEST_GALLERY);
    }

    private File tempFile;

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
            CommonUtil.showToast(getActivity(), getActivity().getString(R.string.not_find_sd),
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



    public void requestUserPermission(String... permissions) {
        MPermission.with(this)
                .addRequestCode(PERMISSION_STGAT_CODE)
                .permissions(permissions)
                .request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @OnMPermissionGranted(PERMISSION_STGAT_CODE)
    public void onPermissionGranted() {
        photoGraph();
    }


    @OnMPermissionDenied(PERMISSION_STGAT_CODE)
    public void onPermissionDenied() {
        Toast.makeText(getActivity(), getActivity().getString(R.string.permission_reject), Toast.LENGTH_SHORT).show();

    }


    StartLiveClickListener startLiveClickListener;

    public void setOnStartLiveClickListener(StartLiveClickListener startLiveClickListener ) {
        this.startLiveClickListener = startLiveClickListener;
    }


    public interface StartLiveClickListener {
        void startLive(int getImageTokenTag,byte[] bytesImg);
    }

}
