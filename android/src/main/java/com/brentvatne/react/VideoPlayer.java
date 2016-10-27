package com.brentvatne.react;

import android.app.Activity;
import android.media.MediaPlayer.*;
import android.media.MediaPlayer;
import android.widget.VideoView;
import android.view.MotionEvent;
import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.MediaController;
import com.brentvatne.react.CcMediaController;
import android.os.Handler;
import android.content.Intent;
import android.widget.ProgressBar;

public class VideoPlayer extends Activity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, MediaController.MediaPlayerControl {

    private VideoView mVV;
    private CcMediaController mediaController;
    private Handler videoControlHandler = new Handler();
    private int mStartPos;
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        String uri = this.getIntent().getStringExtra("URI");
        mStartPos = this.getIntent().getIntExtra("startPos", 0);

        setContentView(R.layout.videoplayer);

        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mProgressBar.setVisibility(View.VISIBLE);

        mVV = (VideoView)findViewById(R.id.myvideoview);
//        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);
        mVV.setOnInfoListener(this);
//        mVV.setOnTouchListener(this);
//
//        if (!playFileRes(fileRes)) return;
        mVV.setVideoURI(Uri.parse(uri));
        mediaController = new CcMediaController(this);
        mediaController.setMediaPlayer(mVV);
        mediaController.setAnchorView(mVV);
        mediaController.setDoneOnClickListenter(new View.OnClickListener() {
            public void onClick(View v) {
                done();
            }
        });
        mVV.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initializeMediaControllerIfNeeded();
        mediaController.show();

        return super.onTouchEvent(event);
    }

    private void initializeMediaControllerIfNeeded() {
        if (mediaController == null) {
            mediaController = new CcMediaController(this);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.setScreenOnWhilePlaying(true);
        this.seekTo(mStartPos);
        mProgressBar.setVisibility(View.GONE);
        initializeMediaControllerIfNeeded();
        mediaController.setMediaPlayer(mVV);
        mediaController.setAnchorView(mVV);

        videoControlHandler.post(new Runnable() {
            @Override
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
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
        return mVV.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return mVV.getDuration();
    }

    @Override
    public void pause() {
        mVV.pause();
    }

    @Override
    public void start() {
        mVV.start();
    }

    @Override
    public boolean isPlaying() {
        return mVV.isPlaying();
    }

    @Override
    public void seekTo(int msec) {
        mVV.seekTo(msec);
    }

    private boolean playFileRes(int fileRes) {
        if (fileRes==0) {
            stopPlaying();
            return false;
        } else {
            mVV.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + fileRes));
            return true;
        }
    }

    public void stopPlaying() {
        mVV.stopPlayback();
        this.finish();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
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

    public void done() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("startPos", getCurrentPosition());
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
