package com.yyy.myshortvideodemo;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnAudioFrameListener;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnVideoFrameListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.yyy.myshortvideodemo.utils.Utils;
import com.yyy.myshortvideodemo.widget.MediaController;
import com.yyy.myshortvideodemo.widget.MediaController.OnClickSpeedAdjustListener;

import java.util.Arrays;

/**
 * This is a demo activity of PLVideoView
 */
public class ShortVideoctivity extends VideoPlayerBaseActivity {

    private static final String TAG = ShortVideoctivity.class.getSimpleName();

    private PLVideoView mVideoView;
    private int mDisplayAspectRatio = PLVideoView.ASPECT_RATIO_PAVED_PARENT;
    //private TextView mStatInfoTextView;
    private MediaController mMediaController;

    private boolean mIsLiveStreaming=true;
    private LinearLayout loadingView;
    String videoPath = "https://aweme.snssdk.com/aweme/v1/playwm/?video_id=v0200f1b0000bgg8h1j2ap9fhu4m5ajg&line=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置屏幕常亮
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mVideoView = findViewById(R.id.VideoView);
        loadingView = findViewById(R.id.LoadingView);
        mVideoView.setBufferingIndicator(loadingView);

        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1 );
        options.setInteger(AVOptions.KEY_LOG_LEVEL, 0);
        mVideoView.setAVOptions(options);

        // Set some listeners
        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);
        mVideoView.setOnVideoFrameListener(mOnVideoFrameListener);
        mVideoView.setOnAudioFrameListener(mOnAudioFrameListener);
        mVideoView.setVideoPath(videoPath);
        mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
        mVideoView.setLooping(true);

        // You can also use a custom `MediaController` widget
        mMediaController = new MediaController(this, !mIsLiveStreaming, mIsLiveStreaming);
        mMediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener);
        mVideoView.setMediaController(mMediaController);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mVideoView != null)
            mVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: 2018/12/25
        mMediaController.getWindow().dismiss();
        mVideoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.pause();
        mVideoView.stopPlayback();
        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }


    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    Utils.showToastTips(ShortVideoctivity.this, "第一个视频：" + extra + "ms");
                    Log.i(TAG, "Response: " + mVideoView.getResponseInfo());
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVideoView.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    updateStatInfo();
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    Log.i(TAG, "Rotation changed: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_LOOP_DONE:
                    Log.i(TAG, "Loop done");
                    break;
                case PLOnInfoListener.MEDIA_INFO_CACHE_DOWN:
                    Log.i(TAG, "Cache done");
                    break;
                default:
                    break;
            }
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    Log.e(TAG, "IO Error!");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    Utils.showToastTips(ShortVideoctivity.this, "failed to open player !");
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    Utils.showToastTips(ShortVideoctivity.this, "failed to seek !");
                    return true;
                case PLOnErrorListener.ERROR_CODE_CACHE_FAILED:
                    Utils.showToastTips(ShortVideoctivity.this, "failed to cache url !");
                    break;
                default:
                    Utils.showToastTips(ShortVideoctivity.this, "unknown error !");
                    break;
            }
            finish();
            return true;
        }
    };

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
            Log.i(TAG, "Play Completed !");
            Utils.showToastTips(ShortVideoctivity.this, "已播放完！");
            if (!mIsLiveStreaming) {
                mMediaController.refreshProgress();
            }
            //finish();
        }
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = new PLOnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLOnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private PLOnVideoFrameListener mOnVideoFrameListener = new PLOnVideoFrameListener() {
        @Override
        public void onVideoFrameAvailable(byte[] data, int size, int width, int height, int format, long ts) {
            Log.i(TAG, "onVideoFrameAvailable: " + size + ", " + width + " x " + height + ", " + format + ", " + ts);
            if (format == PLOnVideoFrameListener.VIDEO_FORMAT_SEI && bytesToHex(Arrays.copyOfRange(data, 19, 23)).equals("74733634")) {
                // If the RTMP stream is from Qiniu
                // Add &addtssei=true to the end of URL to enable SEI timestamp.
                // Format of the byte array:
                // 0:       SEI TYPE                    This is part of h.264 standard.
                // 1:       unregistered user data      This is part of h.264 standard.
                // 2:       payload length              This is part of h.264 standard.
                // 3-18:    uuid                        This is part of h.264 standard.
                // 19-22:   ts64                        Magic string to mark this stream is from Qiniu
                // 23-30:   timestamp                   The timestamp
                // 31:      0x80                        Magic hex in ffmpeg
                Log.i(TAG, " timestamp: " + Long.valueOf(bytesToHex(Arrays.copyOfRange(data, 23, 31)), 16));
            }
        }
    };

    private PLOnAudioFrameListener mOnAudioFrameListener = new PLOnAudioFrameListener() {
        @Override
        public void onAudioFrameAvailable(byte[] data, int size, int samplerate, int channels, int datawidth, long ts) {
            Log.i(TAG, "onAudioFrameAvailable: " + size + ", " + samplerate + ", " + channels + ", " + datawidth + ", " + ts);
        }
    };

    private OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };

    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void updateStatInfo() {
        long bitrate = mVideoView.getVideoBitrate() / 1024;
        final String stat = bitrate + "kbps, " + mVideoView.getVideoFps() + "fps";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // mStatInfoTextView.setText(stat);
            }
        });
    }
}