package com.brentvatne.react;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;

public class CcMediaController extends MediaController {

    ImageButton mCCBtn;
    Context mContext;

    public CcMediaController(Context context) {
        super(context);
        mContext = context;
        mCCBtn = new ImageButton(mContext);
        mCCBtn.setImageResource(R.drawable.minimize);
    }

    @Override
    public void setAnchorView(View view) {
        super.setAnchorView(view);

        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        frameParams.gravity = Gravity.RIGHT|Gravity.TOP;

        addView(mCCBtn, frameParams);

    }

    public void setDoneOnClickListenter(OnClickListener ocl) {
        mCCBtn.setOnClickListener(ocl);
    }
}