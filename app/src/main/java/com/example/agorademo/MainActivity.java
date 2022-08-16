package com.example.agorademo;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import java.util.Date;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;

import io.agora.rtc.RtcEngine;
import io.agora.rtc.VideoEncodedFrame;
import io.agora.rtc.video.CameraCapturerConfiguration;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    // 填写你的项目在 Agora 控制台中生成的 App ID。
    private final static String mAppId = "1a410d207deb42259b763b6edb585cab";
    // 填写频道名称。
    private String mChannelName;
    private RtcEngine mRtcEngine;
    private int mPrecodeFrameNums = 0;
    private int mEncodeFrameNums = 0;
    long mLastPrecodeTime = 0;
    long mLastEncodeTime = 0;
    // 控件
    TextView mTextPreCodeFrame;
    TextView mTextEncodeFrame;
    TextView mTextVideoProfile;
    TextView mTextBitrateSelected;
    TextView mTextDimensionsSelected;
    TextView mTextFrameSelected;
    TextView mTextCamreaCollection;
    TextView mTextVideoSentBitrate;
    TextView mTextSentDimensions;
    // 获取 bundle 的值
    private int mDimensionWith;
    private int mDimensionHeight;
    private int mCameraCollectionHeight;
    private int mCameraCollectionWidth;
    private int mVideoProfileMode;
    private int mBitrateNum;
    private String mCamreaDirections;
    private String mVideoFrame;
    private String mPutStreamUrl;
    private Bundle mBundle;
    private CameraCapturerConfiguration.CAMERA_DIRECTION mCamreaDirection;
    private VideoEncoderConfiguration.FRAME_RATE mFrameRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int max = 1000, min = 1;
        mChannelName = "" + Math.random() * (max - min) + min;
        initView();
        putBundle();
        switch (mCamreaDirections) {
            case "0":
                mCamreaDirection = CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_FRONT;
                break;
            case "1":
                mCamreaDirection = CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_REAR;
                break;
        }
        switch (mVideoFrame) {
            case "0":
            case "1":
                mFrameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_1;
                break;
            case "7":
                mFrameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_7;
                break;
            case "10":
                mFrameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_10;
                break;

            case "15":
                mFrameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15;
                break;

            case "24":
                mFrameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24;
                break;

            case "30":
                mFrameRate = VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30;
                break;
        }
        updateTextView();
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID)) {
            // 如果已经授予所有权限，初始化 RtcEngine 对象并加入一个频道。
            VideoEncoderConfiguration.VideoDimensions dimensions = new VideoEncoderConfiguration.VideoDimensions(mDimensionWith, mDimensionHeight);
            VideoEncoderConfiguration.ORIENTATION_MODE orientationMode = VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT;
            CameraCapturerConfiguration cameraCapturerConfiguration = new CameraCapturerConfiguration(mCameraCollectionWidth, mCameraCollectionHeight, mCamreaDirection);
            VideoEncoderConfiguration videoEncoderConfiguration = new VideoEncoderConfiguration(dimensions, mFrameRate, mBitrateNum, orientationMode);
            initializeAndJoinChannel(videoEncoderConfiguration, cameraCapturerConfiguration, mVideoProfileMode);
        }
    }

//    IVideoEncodedFrameObserver mVideoEncodedFrameObserver = new IVideoEncodedFrameObserver() {
//
//
//
//        @Override
//        public boolean onVideoEncodedFrame(VideoEncodedFrame videoEncodedFrame) {
//            mEncodeFrameNums++;
//            long mEnCurrentTime = new Date().getTime();
//            if (mEnCurrentTime > mLastEncodeTime + 1000) {
//                mLastEncodeTime = mEnCurrentTime;
//                updateEncodeFrameNums(mEncodeFrameNums);
//                mEncodeFrameNums = 0;
//            }
//            return true;
//        }
//
//    };

//    IVideoFrameObserver mVideoFrameObserver = new IVideoFrameObserver() {
//        @Override
//        public boolean onPreEncodeVideoFrame(VideoFrame videoFrame) {
//            mPrecodeFrameNums++;
//            long mCurrentTime = System.currentTimeMillis();
//            if (mCurrentTime > mLastPrecodeTime + 1000) {
//                mLastPrecodeTime = mCurrentTime;
//                updatePrecodeFrameNums(mPrecodeFrameNums);
//                mPrecodeFrameNums = 0;
//            }
//            return super.onPreEncodeVideoFrame(videoFrame);
//        }
//
//        @Override
//        public boolean onCaptureVideoFrame(VideoFrame videoFrame) {
//            return true;
//        }
//
//        @Override
//        public boolean onRenderVideoFrame(int uid, VideoFrame videoFrame) {
//            return true;
//        }
//
//        @Override
//        public int getObservedFramePosition() {
//            return POSITION_PRE_ENCODER;
//        }
//    };

    protected void onDestroy() {
        super.onDestroy();
        if(mPutStreamUrl!=null)
        mRtcEngine.stopRtmpStream(mPutStreamUrl);
        mRtcEngine.leaveChannel();
        mRtcEngine.destroy();
    }

    private void updateTextView() {
        switch (mVideoProfileMode) {
            case 0:
                mTextVideoProfile.setText("通信");
                break;
            case 1:
                mTextVideoProfile.setText("直播");
                break;
            case 3:
                mTextVideoProfile.setText("互动");
                break;
        }
        mTextBitrateSelected.setText("" + mBitrateNum + "Kbps");
        mTextDimensionsSelected.setText(mDimensionHeight + "*" + mDimensionWith);
        mTextFrameSelected.setText(mVideoFrame);
        mTextCamreaCollection.setText("" + mCameraCollectionHeight + "*" + mCameraCollectionWidth);
    }

    private void initializeAndJoinChannel(VideoEncoderConfiguration videoEncoderConfiguration, CameraCapturerConfiguration cameraCapturerConfiguration, int profilemode) {
        try {
            // 初始化一个RtcEngine
            mRtcEngine = RtcEngine.create(getBaseContext(), mAppId, mRtcEventHandler);
        } catch (Exception e) {
            throw new RuntimeException("Check the error.");
        }
//        mRtcEngine.registerVideoFrameObserver(mVideoFrameObserver);
//        mRtcEngine.registerVideoEncodedFrameObserver(mVideoEncodedFrameObserver);
        // 设置Configuration参数
        mRtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
        mRtcEngine.setCameraCapturerConfiguration(cameraCapturerConfiguration);
        // 直播场景下，设置频道场景为 BROADCASTING。
        mRtcEngine.setChannelProfile(profilemode);
        // 设置为软编方式
        mRtcEngine.setParameters("{\"che.hardware_encoding\": 0}");
        // 根据场景设置用户角色为 BORADCASTER 或 AUDIENCE。
        mRtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        // 视频默认禁用，你需要调用 enableVideo 开始视频流。
        mRtcEngine.enableVideo();
        FrameLayout container = findViewById(R.id.local_video_view_container);
        // 调用 CreateRendererView 创建一个 SurfaceView 对象，并将其作为 FrameLayout 的子对象。
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        // 将 SurfaceView 对象传入 Agora，以渲染本地视频。
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0));
        // 使用 Token 加入频道。
        mRtcEngine.joinChannel("", mChannelName, "", 0);
    }

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        Toast toast;

        @Override
        public void onLocalVideoStats(LocalVideoStats stats) {
            super.onLocalVideoStats(stats);

            mTextSentDimensions.setText(""+stats.encodedFrameHeight+"*"+stats.encodedFrameWidth);
            mTextVideoSentBitrate.setText(stats.sentBitrate+"kbps");
            mTextPreCodeFrame.setText(stats.captureFrameRate+"fps");
            mTextEncodeFrame.setText(stats.encoderOutputFrameRate+"fps");
        }

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            super.onJoinChannelSuccess(channel, uid, elapsed);
            mRtcEngine.startRtmpStreamWithoutTranscoding(mPutStreamUrl);

        }

        @Override
        public void onRtmpStreamingStateChanged(String url, int state, int errCode) {
            super.onRtmpStreamingStateChanged(url, state, errCode);
            Log.i("ErrorCode", "" + errCode);
            if (errCode == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(MainActivity.this, "推流成功", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else if (errCode == 1) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(getApplicationContext(), "参数错误", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(getApplicationContext(), "推流失败", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }

        }

//        @Override
//        // 监听频道内的远端主播，获取主播的 uid 信息。
//        public void onUserJoined(int uid, int elapsed) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    // 从 onUserJoined 回调获取 uid 后，调用 setupRemoteVideo，设置远端视频视图。
//                    setupRemoteVideo(uid);
//                }
//            });
//        }
    };


    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

//    private void updatePrecodeFrameNums(int frameNums) {
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                String out = "" + frameNums;
//                mTextPreCodeFrame.setText(out);
//            }
//        });
//    }
//
//    private void updateEncodeFrameNums(int encodeframeNums) {
//        MainActivity.this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                String out = "" + encodeframeNums;
//                mTextEncodeFrame.setText(out);
//            }
//        });
//    }

    private void initView() {
        mTextPreCodeFrame = findViewById(R.id.tv_precode_frame);
        mTextEncodeFrame = findViewById(R.id.tv_encode_frame);
        mTextVideoProfile = findViewById(R.id.tv_video_profile);
        mTextBitrateSelected = findViewById(R.id.tv_bitrate_selected);
        mTextDimensionsSelected = findViewById(R.id.tv_dimensions_selected);
        mTextFrameSelected = findViewById(R.id.tv_frame_selected);
        mTextCamreaCollection = findViewById(R.id.tv_camrea_collection);
        mTextVideoSentBitrate=findViewById(R.id.tv_sent_bitrate);
        mTextSentDimensions=findViewById(R.id.tv_sent_dimension);

    }

    private void putBundle(){
        mBundle = this.getIntent().getExtras();
        mDimensionWith = Integer.parseInt(mBundle.getString("dimensions_width"));
        mDimensionHeight = Integer.parseInt(mBundle.getString("dimensions_height"));
        mCameraCollectionHeight = Integer.parseInt(mBundle.getString("Camera_height"));
        mCameraCollectionWidth = Integer.parseInt(mBundle.getString("Camera_width"));
        mCamreaDirections = mBundle.getString("cameraDirection");
        mVideoProfileMode = Integer.parseInt(mBundle.getString("profilemode"));
        mBitrateNum = Integer.parseInt(mBundle.getString("bitrate"));
        mVideoFrame = mBundle.getString("videoframe");
        mPutStreamUrl = mBundle.getString("putstream");
    }
}