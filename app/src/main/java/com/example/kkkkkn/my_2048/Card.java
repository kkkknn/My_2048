package com.example.kkkkkn.my_2048;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
    //展示的数字
    private TextView showTextView;
    private int showNum=0;

    public Card(@NonNull Context context,int leftMargin,int topMargin,int rightMargin,int bottomMargin) {
        super(context);
        init(context,leftMargin, topMargin,rightMargin, bottomMargin);
    }

    private void init(Context context,int leftMargin,int topMargin,int rightMargin,int bottomMargin) {
        showTextView=new TextView(context);
        showTextView.setTextSize(26);
        showTextView.setGravity(Gravity.CENTER);
        showTextView.getPaint().setAntiAlias(true);
        showTextView.getPaint().setFakeBoldText(true);
        showTextView.setTextColor(Color.WHITE);
        LayoutParams params=new LayoutParams(-1,-1);
        params.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
        addView(showTextView,params);
        setShowNum(0);
    }

    public TextView getCard(){
        return showTextView;
    }

    public int getShowNum(){
        return showNum;
    }

    public void setShowNum(int i) {
        this.showNum=i;
        showTextView.setBackgroundColor(getColor(i));
        if(i<=0){
            showTextView.setText(" ");
        }else {
            showTextView.setText(String.valueOf(i));
        }

    }

    private int getColor(int num){
        int color;
        switch (num){
            case 2:
                color=Color.parseColor("#EEE4DA");
                break;
            case 4:
                color=Color.parseColor("#EDE0C8");
                break;
            case 8:
                color=Color.parseColor("#F2B179");
                break;
            case 16:
                color=Color.parseColor("#F49563");
                break;
            case 32:
                color=Color.parseColor("#F5794D");
                break;
            case 64:
                color=Color.parseColor("#F55D37");
                break;
            case 128:
                color=Color.parseColor("#EEE863");
                break;
            case 256:
                color=Color.parseColor("#EDB04D");
                break;
            case 512:
                color=Color.parseColor("#ECB04D");
                break;
            case 1024:
                color=Color.parseColor("#EB9437");
                break;
            case 2048:
                color=Color.parseColor("#EA7821");
                break;
            default:
                color=Color.parseColor("#f5f0ed");
                break;
        }
        return color;
    }


}
