package com.example.agorademo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

public class SettingActivity extends AppCompatActivity {
    //  控件
    private Spinner mSpinnerDimensions;
    private Spinner mSpinnerVideoProfile;
    private Spinner mSpinnerBitrate;
    private Spinner mSpinnerVideoFrame;
    private Spinner mSpinnerCamreaDirection;
    private Spinner mSpinnerCamreaCollection;
    private Button mRtcBtn;
    private Button mRtcUrlBtn;
    private Button mSelectLocalVideoBtn;
    private EditText mEdDimensionsWidth;
    private EditText mEdDimensionsHeight;
    private EditText mEdBitrate;
    private EditText mEdCameraCollectionHeight;
    private EditText mEdCameraCollectionWidth;
    private EditText mEdPutStreamUrl;
    private Bundle mBundle;

    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ZXingLibrary.initDisplayOpinion(this);
        mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() != RESULT_OK || result.getData() == null) {
                Log.e("TAG", "onCreate: 哈哈厉害了");
            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q && !Environment.isExternalStorageManager()) {
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                mActivityResultLauncher.launch(intent);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 666);
        }
        mBundle = new Bundle();
        initView();
        mSpinnerDimensions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        mEdDimensionsHeight.setText("请选择或者输入你的分辨率");
                        mEdDimensionsWidth.setText("请选择或者输入你的分辨率");
                    case 1:
                        mEdDimensionsHeight.setText("640");
                        mEdDimensionsWidth.setText("360");
                        break;
                    case 2:
                        mEdDimensionsHeight.setText("480");
                        mEdDimensionsWidth.setText("360");
                        break;
                    case 3:
                        mEdDimensionsHeight.setText("320");
                        mEdDimensionsWidth.setText("240");
                        break;
                    case 4:
                        mEdDimensionsHeight.setText("640");
                        mEdDimensionsWidth.setText("480");
                        break;
                    case 5:
                        mEdDimensionsHeight.setText("960");
                        mEdDimensionsWidth.setText("540");
                        break;
                    case 6:
                        mEdDimensionsHeight.setText("1280");
                        mEdDimensionsWidth.setText("720");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinnerVideoProfile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        mBundle.putString("profilemode", "0");
                        break;
                    case 1:
                        mBundle.putString("profilemode", "1");
                        break;
                    case 2:
                        mBundle.putString("profilemode", "3");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mSpinnerBitrate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                    case 1:
                        mEdBitrate.setText("800");
                        break;
                    case 2:
                        mEdBitrate.setText("100");
                        break;
                    case 3:
                        mEdBitrate.setText("280");
                        break;
                    case 4:
                        mEdBitrate.setText("200");
                        break;
                    case 5:
                        mEdBitrate.setText("240");
                        break;
                    case 6:
                        mEdBitrate.setText("400");
                        break;
                    case 7:
                        mEdBitrate.setText("280");
                        break;
                    case 8:
                        mEdBitrate.setText("440");
                        break;
                    case 9:
                        mEdBitrate.setText("130");
                        break;
                    case 10:
                        mEdBitrate.setText("520");
                        break;
                    case 11:
                        mEdBitrate.setText("1200");
                        break;
                    case 12:
                        mEdBitrate.setText("800");
                        break;
                    case 13:
                        mEdBitrate.setText("640");
                        break;
                    case 14:
                        mEdBitrate.setText("980");
                        break;
                    case 15:
                        mEdBitrate.setText("1000");
                        break;
                    case 16:
                        mEdBitrate.setText("800");
                        break;
                    case 17:
                        mEdBitrate.setText("1500");
                        break;
                    case 18:
                        mEdBitrate.setText("1200");
                        break;
                    case 19:
                        mEdBitrate.setText("1220");
                        break;
                    case 20:
                        mEdBitrate.setText("1860");
                        break;
                    case 21:
                        mEdBitrate.setText("800");
                        break;
                    case 22:
                        mEdBitrate.setText("2260");
                        break;
                    case 23:
                        mEdBitrate.setText("3420");
                        break;
                    case 24:
                        mEdBitrate.setText("1820");
                        break;
                    case 25:
                        mEdBitrate.setText("2760");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mSpinnerVideoFrame.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        mBundle.putString("videoframe", "0");
                    case 1:
                        mBundle.putString("videoframe", "15");
                        break;
                    case 2:
                        mBundle.putString("videoframe", "7");
                        break;
                    case 3:
                        mBundle.putString("videoframe", "10");
                        break;
                    case 4:
                        mBundle.putString("videoframe", "1");
                        break;
                    case 5:
                        mBundle.putString("videoframe", "24");
                        break;
                    case 6:
                        mBundle.putString("videoframe", "30");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        mSpinnerCamreaDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 0:
                        mBundle.putString("cameraDirection", "0");
                        break;
                    case 1:
                        mBundle.putString("cameraDirection", "1");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mSpinnerCamreaCollection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                switch (i) {
                    case 0:
                        mEdCameraCollectionHeight.setText("1920");
                        mEdCameraCollectionWidth.setText("1080");
                        break;
                    case 1:
                        mEdCameraCollectionHeight.setText("1280");
                        mEdCameraCollectionWidth.setText("720");
                        break;
                    case 2:
                        mEdCameraCollectionHeight.setText("640");
                        mEdCameraCollectionWidth.setText("480");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mRtcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                mBundle.putString("dimensions_width", mEdDimensionsWidth.getText().toString());
                mBundle.putString("dimensions_height", mEdDimensionsHeight.getText().toString());
                mBundle.putString("Camera_height", mEdCameraCollectionHeight.getText().toString());
                mBundle.putString("Camera_width", mEdCameraCollectionWidth.getText().toString());
                mBundle.putString("bitrate", mEdBitrate.getText().toString());
                mBundle.putString("putstream", mEdPutStreamUrl.getText().toString());
                intent.putExtras(mBundle);
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        mRtcUrlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        mSelectLocalVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                mBundle.putString("dimensions_width", mEdDimensionsWidth.getText().toString());
                mBundle.putString("dimensions_height", mEdDimensionsHeight.getText().toString());
                mBundle.putString("Camera_height", mEdCameraCollectionHeight.getText().toString());
                mBundle.putString("Camera_width", mEdCameraCollectionWidth.getText().toString());
                mBundle.putString("bitrate", mEdBitrate.getText().toString());
                mBundle.putString("putstream", mEdPutStreamUrl.getText().toString());
                intent.putExtras(mBundle);
                intent.setClass(getApplicationContext(), LocalVideoMainActivity.class);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理二维码扫描结果
        if (requestCode == 1) {
            // 处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    mEdPutStreamUrl.setText(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(SettingActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void initView() {
        mSpinnerDimensions = findViewById(R.id.spinner_dimensions);
        mSpinnerVideoProfile = findViewById(R.id.spinner_video_profile);
        mSpinnerBitrate = findViewById(R.id.spinner_bitrate);
        mSpinnerVideoFrame = findViewById(R.id.spinner_video_frame);
        mSpinnerCamreaDirection = findViewById(R.id.spinner_camrea_direction);
        mSpinnerCamreaCollection = findViewById(R.id.spinner_camrea_collection);
        mRtcBtn = findViewById(R.id.rtc_bt);
        mRtcUrlBtn = findViewById(R.id.rtc_url_bt);
        mSelectLocalVideoBtn = findViewById(R.id.select_local_video_bt);
        mEdDimensionsWidth = findViewById(R.id.et_dimensions_width);
        mEdDimensionsHeight = findViewById(R.id.et_dimensions_height);
        mEdBitrate = findViewById(R.id.et_bitrate);
        mEdPutStreamUrl = findViewById(R.id.et_putstream_url);
        mEdCameraCollectionHeight = findViewById(R.id.et_carm_collection_height);
        mEdCameraCollectionWidth = findViewById(R.id.et_carm_collection_width);

    }
}