短视频Demo 准备材料并配置项目工程： 1、去下载七牛云播放器SDK 
2、然后把pldroid-player-2.1.6.jar包拷贝到你项目lib文件里 
3、把整个jniLibs文件夹里面的所有内容都拷贝到你项目src/main/里 
4、然后在你项目build.gradle进行关联

android {

sourceSets { main { jniLibs.srcDirs = ['src/main/jniLibs'] } }

}

dependencies { implementation files('libs/pldroid-player-2.1.6.jar') 
implementation 'com.bugsnag:bugsnag-android-ndk:1.1.2' 
implementation 'com.android.support:appcompat-v7:27.1.0' 
implementation 'com.journeyapps:zxing-android-embedded:3.0.2@aar' 
implementation 'com.google.zxing:core:3.2.0' }

5、然后在你项目xml布局里添加如下代码，PLVideoView控件：

 <com.pili.pldroid.player.widget.PLVideoView
        android:id="@+id/VideoView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="center" />
        
        6、最后去在你Activity里找到这个控件并配置播放器代码：

     mVideoView = findViewById(R.id.VideoView);

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

用支付宝，扫下面二维码领红包

![Alt text](https://github.com/Visen123/MyShortVideo/raw/master/Screensshots/hb01.png)

如果你觉得我分享的这个Dmeo还不错，请扫下面二维码，随意打赏，鼓励一下，谢谢支持！

![Alt text](https://github.com/Visen123/MyShortVideo/raw/master/Screenshots/hb02.png)