package com.example.kkkkkn.my_2048.custom_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

public class ScoreView extends PopupWindow {
    private int mTextColor =  Color.BLACK;
    private int mTextSize = 16;
    private int mFromY = 0;
    private int mToY =  60;
    private float mFromAlpha = 1.0f;
    private float mToAlpha =  0.0f;
    private int mDuration = 400;
    private int mDistance = 60;
    private AnimationSet mAnimationSet;
    private boolean mChanged = false;
    private Context mContext = null;
    private TextView mScore = null;
    private int cacheScore=0;

    public ScoreView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    private void initView() {
        RelativeLayout layout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        mScore = new TextView(mContext);
        mScore.setIncludeFontPadding(false);
        mScore.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
        mScore.setTextColor(mTextColor);
        mScore.setText("");
        mScore.setLayoutParams(params);
        layout.addView(mScore);
        setContentView(layout);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mScore.measure(w, h);
        setWidth(mScore.getMeasuredWidth());
        setHeight(mDistance + mScore.getMeasuredHeight());
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(false);
        setTouchable(false);
        setOutsideTouchable(false);

        mAnimationSet = createAnimation();
    }

    private static int getTextViewHeight(TextView textView, int width) {
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        return textView.getMeasuredHeight();
    }


    /**
     * 设置移动距离
     *
     * @param dis
     */
    public void setDistance(int dis) {
        mDistance = dis;
        mToY = dis;
        mChanged = true;
        setHeight(mDistance + mScore.getMeasuredHeight());
    }

    /**
     * 设置Y轴移动属性
     *
     * @param fromY
     * @param toY
     */
    public void setTranslateY(int fromY, int toY) {
        mFromY = fromY;
        mToY = toY;
        mChanged = true;
    }

    /**
     * 设置透明度属性
     *
     * @param fromAlpha
     * @param toAlpha
     */
    public void setAlpha(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
        mChanged = true;
    }

    /**
     * 设置动画时长
     *
     * @param duration
     */
    public void setDuration(int duration) {
        mDuration = duration;
        mChanged = true;
    }

    /**
     * 展示
     *
     * @param v
     */
    public void show(int score,View v) {
        if(!isShowing()){
            String str="+"+String.valueOf(cacheScore+score);
            cacheScore=0;
            mScore.setText(str);
            mScore.setBackground(new ColorDrawable(Color.TRANSPARENT));
            int w = (int) mScore.getPaint().measureText(str);
            setWidth(w);
            setHeight(mDistance + getTextViewHeight(mScore, w));

            int offsetY = -v.getHeight() - getHeight();
            showAsDropDown(v, v.getWidth() / 2 - getWidth() / 2, offsetY);
            if (mAnimationSet == null || mChanged) {
                mAnimationSet = createAnimation();
                mChanged = false;
            }
            mScore.startAnimation(mAnimationSet);
        }else {
            cacheScore+=score;
        }

    }

    /**
     * 动画
     *
     * @return
     */
    private AnimationSet createAnimation() {
        mAnimationSet = new AnimationSet(true);
        TranslateAnimation translateAnim = new TranslateAnimation(0, 0, mFromY, -mToY);
        AlphaAnimation alphaAnim = new AlphaAnimation(mFromAlpha, mToAlpha);
        mAnimationSet.addAnimation(translateAnim);
        mAnimationSet.addAnimation(alphaAnim);
        mAnimationSet.setDuration(mDuration);
        mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isShowing()) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                        }
                    });
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        return mAnimationSet;
    }
}
