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

public class VideoPlayer extends Activity implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaController.MediaPlayerControl {

    private VideoView mVV;
    private CcMediaController mediaController;
    private Handler videoControlHandler = new Handler();
    private int mStartPos;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);

        String uri = this.getIntent().getStringExtra("URI");
        mStartPos = this.getIntent().getIntExtra("startPos", 0);

        setContentView(R.layout.videoplayer);

//        int fileRes=0;
//        Bundle e = getIntent().getExtras();
//        if (e!=null) {
//            fileRes = e.getInt("fileRes");
//        }

        mVV = (VideoView)findViewById(R.id.myvideoview);
//        mVV.setOnCompletionListener(this);
        mVV.setOnPreparedListener(this);
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
        this.seekTo(mStartPos);
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
