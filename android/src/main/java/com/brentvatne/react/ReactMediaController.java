package com.brentvatne.react;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;

public class ReactMediaController extends MediaController {

    ImageButton mBtn;
    Context mContext;

    public ReactMediaController(Context context, boolean useFastForward) {
        super(context, useFastForward);
        mContext = context;
        mBtn = new ImageButton(mContext);
        mBtn.setImageResource(R.drawable.minimize);
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.RIGHT|Gravity.TOP;

        addView(mBtn, frameParams);

    }

    public void setDoneOnClickListenter(OnClickListener ocl) {
        mBtn.setOnClickListener(ocl);
    }
}