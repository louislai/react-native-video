package com.brentvatne.react;

import android.app.Activity;
import android.media.MediaPlayer;
import android.widget.VideoView;
import android.view.MotionEvent;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import com.brentvatne.react.ReactMediaController;
import android.content.Intent;
import android.widget.ProgressBar;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import com.brentvatne.react.ReactVideoView;

public class FullScreenVideoActivity extends Activity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaController.MediaPlayerControl,
        MediaPlayer.OnBufferingUpdateListener {

    private SurfaceView mSurfaceView;
    private ReactMediaController mMediaController;
    final private MediaPlayer mMediaPlayer = ReactVideoView.getMediaPlayer(false);
    private ProgressBar mProgressBar;
    private int mBufferProgress = 0;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        setContentView(R.layout.videoplayer);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mSurfaceView = (SurfaceView)findViewById(R.id.surfaceview);
        mMediaPlayer.setOnInfoListener(this);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);

        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mMediaPlayer.setDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int m, int n, int p) {}

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {}
        });
        initializeMediaControllerIfNeeded();
        mMediaController.setMediaPlayer(this);
        mMediaController.setAnchorView(mSurfaceView);
        mMediaController.setDoneOnClickListenter(new View.OnClickListener() {
            public void onClick(View v) {
                done();
            }
        });
    }

    @Override
    public void onBackPressed() {
        done();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mBufferProgress = percent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initializeMediaControllerIfNeeded();
        mMediaController.show();

        return super.onTouchEvent(event);
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
            mProgressBar.setVisibility(View.GONE);
        }
        return false;
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public void start() {
        mMediaPlayer.start();
    }

    @Override
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    public void stopPlaying() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        done();
    }

    @Override
    public int getBufferPercentage() {
        return mBufferProgress;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    private void done() {
        mMediaPlayer.setSurface(ReactVideoView.surface);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    private void initializeMediaControllerIfNeeded() {
        if (mMediaController == null) {
            mMediaController = new ReactMediaController(this, false);
        }
    }
}
