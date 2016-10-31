package com.brentvatne.react;

import com.brentvatne.react.ReactVideoView.Events;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ThemedReactContext;
import com.yqritc.scalablevideoview.ScalableType;
import com.brentvatne.react.FullScreenVideoActivity;
import android.app.Activity;
import javax.annotation.Nullable;
import android.content.Intent;
import java.util.Map;
import android.os.Bundle;

import android.util.Log;

public class ReactVideoViewManager extends SimpleViewManager<ReactVideoView> implements ActivityEventListener {

    public static final String REACT_CLASS = "RCTVideo";

    public static final String PROP_SRC = "src";
    public static final String PROP_SRC_URI = "uri";
    public static final String PROP_SRC_TYPE = "type";
    public static final String PROP_SRC_IS_NETWORK = "isNetwork";
    public static final String PROP_SRC_IS_ASSET = "isAsset";
    public static final String PROP_RESIZE_MODE = "resizeMode";
    public static final String PROP_REPEAT = "repeat";
    public static final String PROP_PAUSED = "paused";
    public static final String PROP_MUTED = "muted";
    public static final String PROP_VOLUME = "volume";
    public static final String PROP_SEEK = "seek";
    public static final String PROP_RATE = "rate";
    public static final String PROP_FULLSCREEN = "fullscreen";
    public static final int START_ACTIVITY_REQUEST_CODE = 125;

    private ThemedReactContext mThemedReactContext;
    private ReactApplicationContext mReactContext;
    private ReactVideoView mVideoView;

    public ReactVideoViewManager(ReactApplicationContext reactContext) {
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ReactVideoView createViewInstance(ThemedReactContext themedReactContext) {
        mThemedReactContext = themedReactContext;
        mReactContext.addActivityEventListener(this);
        mVideoView = new ReactVideoView(themedReactContext);
        return mVideoView;
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder builder = MapBuilder.builder();
        for (Events event : Events.values()) {
            builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
        }
        return builder.build();
    }

    @Override
    @Nullable
    public Map getExportedViewConstants() {
        return MapBuilder.of(
                "ScaleNone", Integer.toString(ScalableType.LEFT_TOP.ordinal()),
                "ScaleToFill", Integer.toString(ScalableType.FIT_XY.ordinal()),
                "ScaleAspectFit", Integer.toString(ScalableType.FIT_CENTER.ordinal()),
                "ScaleAspectFill", Integer.toString(ScalableType.CENTER_CROP.ordinal())
        );
    }

    @ReactProp(name = PROP_SRC)
    public void setSrc(final ReactVideoView videoView, @Nullable ReadableMap src) {
        videoView.setSrc(
                src.getString(PROP_SRC_URI),
                src.getString(PROP_SRC_TYPE),
                src.getBoolean(PROP_SRC_IS_NETWORK),
                src.getBoolean(PROP_SRC_IS_ASSET)
        );
    }

    @ReactProp(name = PROP_RESIZE_MODE)
    public void setResizeMode(final ReactVideoView videoView, final String resizeModeOrdinalString) {
        videoView.setResizeModeModifier(ScalableType.values()[Integer.parseInt(resizeModeOrdinalString)]);
    }

    @ReactProp(name = PROP_REPEAT, defaultBoolean = false)
    public void setRepeat(final ReactVideoView videoView, final boolean repeat) {
        videoView.setRepeatModifier(repeat);
    }

    @ReactProp(name = PROP_PAUSED, defaultBoolean = false)
    public void setPaused(final ReactVideoView videoView, final boolean paused) {
        videoView.setPausedModifier(paused);
    }

    @ReactProp(name = PROP_MUTED, defaultBoolean = false)
    public void setMuted(final ReactVideoView videoView, final boolean muted) {
        videoView.setMutedModifier(muted);
    }

    @ReactProp(name = PROP_VOLUME, defaultFloat = 1.0f)
    public void setVolume(final ReactVideoView videoView, final float volume) {
        videoView.setVolumeModifier(volume);
    }

    @ReactProp(name = PROP_SEEK)
    public void setSeek(final ReactVideoView videoView, final float seek) {
        videoView.seekTo(Math.round(seek * 1000.0f));
    }

    @ReactProp(name = PROP_RATE)
    public void setRate(final ReactVideoView videoView, final float rate) {
        videoView.setRateModifier(rate);
    }

    @ReactProp(name = PROP_FULLSCREEN)
    public void setFullScreen(final ReactVideoView videoView, final boolean fullscreen) {
        if (fullscreen) {
            videoView.setFullScreen(true);
            int currentPosition = mVideoView.getCurrentPosition();
            Intent intent = new Intent(mThemedReactContext, FullScreenVideoActivity.class);
            mThemedReactContext.startActivityForResult(intent, START_ACTIVITY_REQUEST_CODE, new Bundle());
        }
    }

    public void onActivityResult(final Activity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == START_ACTIVITY_REQUEST_CODE) {
            mVideoView.onVideoFullScreenWillDismiss();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == START_ACTIVITY_REQUEST_CODE) {
            mVideoView.onVideoFullScreenWillDismiss();
        }
    }
}
