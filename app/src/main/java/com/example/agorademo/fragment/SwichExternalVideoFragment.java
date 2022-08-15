package com.example.agorademo.fragment;
import static com.example.agorademo.util.Constant.ENGINE;
import static com.example.agorademo.util.Constant.TEXTUREVIEW;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Environment;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agorademo.MainActivity;
import com.example.agorademo.R;
import com.example.agorademo.util.ExternalVideoSourceManager;
import java.io.File;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoEncoderConfiguration;


public class SwichExternalVideoFragment extends BaseFragment implements  View.OnClickListener {
    private static final String TAG = SwichExternalVideoFragment.class.getSimpleName();


    private RelativeLayout fl_local;
    private  Bundle mBundle;
    private Button join, mSelectBt;
    private int myUid;
    private boolean joined = false;
    private static  final int MAX_NUM=1000;
    private static  final int MIN_NUM=1;
    private  String path;
    private String mLocalVideoPath;
    private boolean mLocalVideoExists = false;
    private IExternalVideoInputService mService;
    private VideoInputServiceConnection mServiceConnection;

    TextView mTextPreCodeFrame;
    TextView mTextEncodeFrame;
    TextView mTextVideoProfile;
    TextView mTextBitrateSelected;
    TextView mTextDimensionsSelected;
    TextView mTextFrameSelected;
    TextView mTextVideoSentBitrate;
    TextView mTextSentDimensions;



//    Bundle
private int mDimensionWith ;
    private int mDimensionHeight ;
    private int mVideoProfileMode;
    private  int mBitrateNum;
    private String mVideoFrame;
    private String mPutStreamUrl;
    private VideoEncoderConfiguration.FRAME_RATE mFrameRate;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        putBundle();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_swich_external_video, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        join=view.findViewById(R.id.btn_join);
        mSelectBt=view.findViewById(R.id.select_bt);
        fl_local = view.findViewById(R.id.fl_local);

        mTextPreCodeFrame = view.findViewById(R.id.tv_precode_frame);
        mTextEncodeFrame = view.findViewById(R.id.tv_encode_frame);
        mTextVideoProfile = view.findViewById(R.id.tv_video_profile);
        mTextBitrateSelected = view.findViewById(R.id.tv_bitrate_selected);
        mTextDimensionsSelected = view.findViewById(R.id.tv_dimensions_selected);
        mTextFrameSelected = view.findViewById(R.id.tv_frame_selected);
        mTextVideoSentBitrate=view.findViewById(R.id.tv_sent_bitrate);
        mTextSentDimensions=view.findViewById(R.id.tv_sent_dimension);
        join.setOnClickListener(this);
        mSelectBt.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateTextView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Context context=getContext();
        if(context==null)
            return;
        try {
            ENGINE = RtcEngine.create(context.getApplicationContext(), "1a410d207deb42259b763b6edb585cab", iRtcEngineEventHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        path=getPath(getContext(),uri);
        mSelectBt.setText(path+"");
        checkLocalVideo();
        fl_local.removeAllViews();
        try {
            Intent intent = new Intent();
            setVideoConfig(ExternalVideoSourceManager.TYPE_LOCAL_VIDEO, mDimensionWith, mDimensionHeight);
            intent.putExtra(ExternalVideoSourceManager.FLAG_VIDEO_PATH, mLocalVideoPath);
            if (mService.setExternalVideoInput(ExternalVideoSourceManager.TYPE_LOCAL_VIDEO, intent)) {
                fl_local.removeAllViews();
                fl_local.addView(TEXTUREVIEW,
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
            }
        }
        catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_join) {
            if (!joined) {
                TEXTUREVIEW = new TextureView(getContext());
                // call when join button hit
                String channelId = "" + Math.random() * (MAX_NUM - MIN_NUM) + MIN_NUM;
                v.setEnabled(true);
                // Check permission
//                if (AndPermission.hasPermissions(this, Permission.Group.STORAGE, Permission.Group.MICROPHONE, Permission.Group.CAMERA)) {
//                    joinChannel(channelId);
//                    return;
//                }
//                // Request permission
//                AndPermission.with(this).runtime().permission(
//                        Permission.Group.STORAGE,
//                        Permission.Group.MICROPHONE,
//                        Permission.Group.CAMERA
//                ).onGranted(permissions ->
//                {
                    // Permissions Granted
                    joinChannel(channelId);
                //}).start();
            } else {
                joined = false;
                join.setText(getString(R.string.join));
                fl_local.removeAllViews();
                ENGINE.leaveChannel();
                handler.removeCallbacksAndMessages(null);
                TEXTUREVIEW = null;
                unbindVideoService();
            }
        } else if(v.getId()==R.id.select_bt){


            if(android.os.Build.BRAND.equals("Huawei")){
                Intent intentPic = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentPic,2);
            }
            if(android.os.Build.BRAND.equals("Xiaomi")){
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
                startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), 2);
            }else {
                Intent intent = new Intent();
                if(Build.VERSION.SDK_INT < 19){
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                }else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("video/*");
                }
                startActivityForResult(Intent.createChooser(intent, "选择要导入的视频"), 2);
            }

        }




        else {
//                showAlert(getString(R.string.lowversiontip));
            }
        }

    private boolean checkLocalVideo() {
        File videoFile = new File(path);
        mLocalVideoPath = videoFile.getAbsolutePath();
        mLocalVideoExists = videoFile.exists();
        Toast.makeText(getContext(),"路径："+mLocalVideoPath+"   "+mLocalVideoExists,Toast.LENGTH_SHORT).show();

        if (!mLocalVideoExists) {

        }
        return mLocalVideoExists;
    }

    private void setVideoConfig(int sourceType, int width, int height) {
        VideoEncoderConfiguration.ORIENTATION_MODE mode;
        switch (sourceType) {
            case ExternalVideoSourceManager.TYPE_LOCAL_VIDEO:
            case ExternalVideoSourceManager.TYPE_SCREEN_SHARE:
                mode = VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT;
                break;
            default:
                mode = VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_ADAPTIVE;
                break;
        }
        /**Setup video stream encoding configs*/
        ENGINE.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                new VideoEncoderConfiguration.VideoDimensions(width, height),
                mFrameRate,
                mBitrateNum, mode
        ));
    }

    private void joinChannel(String channelId) {
        // Check if the context is valid
        Context context = getContext();
        if (context == null) {
            return;
        }

        ENGINE.setChannelProfile(mVideoProfileMode);

        ENGINE.setParameters("{\"che.hardware_encoding\": 0}");

        /**Sets the role of a user (Live Broadcast only).*/
        ENGINE.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
        /**Enable video module*/
        ENGINE.enableVideo();
        /**Set up to play remote sound with receiver*/
        ENGINE.setDefaultAudioRoutetoSpeakerphone(true);
        ENGINE.setEnableSpeakerphone(false);

        String accessToken="";
        if (TextUtils.equals(accessToken, "") || TextUtils.equals(accessToken, "<#YOUR ACCESS TOKEN#>")) {
            accessToken = null;
        }
        /** Allows a user to join a channel.
         if you do not specify the uid, we will generate the uid for you*/
        int res = ENGINE.joinChannel(accessToken, channelId, "Extra optional Data", 0);
        if (res != 0) {

            return;
        }
        // Prevent repeated entry
        join.setEnabled(false);
    }

    private IRtcEngineEventHandler iRtcEngineEventHandler = new IRtcEngineEventHandler() {
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
            Log.i(TAG, String.format("onJoinChannelSuccess channel %s uid %d", channel, uid));
            if (isDetached()){
                return;
            }
            myUid = uid;
            joined = true;
            handler.post(() -> {
                join.setEnabled(true);
                join.setText("退出频道");
                bindVideoServide();
                ENGINE.startRtmpStreamWithoutTranscoding(mPutStreamUrl);
            });
        }

        @Override
        public void onRemoteVideoStateChanged(int uid, int state, int reason, int errCode) {
            super.onRemoteVideoStateChanged(uid, state, reason, errCode);
            Log.i("ErrorCode", "" + errCode);
            if (errCode == 0) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(getContext(), "推流成功", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            } else if (errCode == 1) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(getContext(), "参数错误", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toast = Toast.makeText(getContext(), "推流失败", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });

            }
        }

    };

    private  void bindVideoServide(){
        Log.d(TAG, "bindVideoServide: "+hashCode());
        Intent intent =new Intent();
        intent.setClass(getContext(), ExternalVideoInputService.class);
        mServiceConnection = new VideoInputServiceConnection();
        getContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindVideoService() {
        if (mServiceConnection != null) {
            getContext().unbindService(mServiceConnection);
            mServiceConnection = null;
        }
    }

    private class VideoInputServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = (IExternalVideoInputService) iBinder;
            Log.i("haos",mService+"");
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        iRtcEngineEventHandler = null;
        handler.removeCallbacksAndMessages(null);
        ENGINE.stopRtmpStream(mPutStreamUrl);
        RtcEngine.destroy();
        ENGINE = null;
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
    }
    private void putBundle(){
        mBundle = getActivity().getIntent().getExtras();
        mDimensionWith = Integer.parseInt(mBundle.getString("dimensions_width"));
        mDimensionHeight = Integer.parseInt(mBundle.getString("dimensions_height"));
        mVideoProfileMode = Integer.parseInt(mBundle.getString("profilemode"));
        mBitrateNum = Integer.parseInt(mBundle.getString("bitrate"));
        mVideoFrame = mBundle.getString("videoframe");
        mPutStreamUrl = mBundle.getString("putstream");
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
                if ("5D68-9217".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }
}