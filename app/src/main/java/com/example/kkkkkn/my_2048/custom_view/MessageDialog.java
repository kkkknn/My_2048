package com.example.kkkkkn.my_2048.custom_view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.kkkkkn.my_2048.R;

public class MessageDialog extends Dialog {
    private Button btn;
    private TextView textView;
    private Listener listener;

    public MessageDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.msg_dialog_layout);
        Window window=getWindow();
        if(window!=null){
            window.setBackgroundDrawableResource(R.color.transparent);
        }
        initView();
        setCancelable(false);
    }


    protected MessageDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setContentView(R.layout.msg_dialog_layout);
        Window window=getWindow();
        if(window!=null){
            window.setBackgroundDrawableResource(R.color.transparent);
        }
        initView();
        setCancelable(false);
    }

    public void setListener(Listener listener){
        this.listener=listener;
    }

    private void initView() {
        btn=findViewById(R.id.msg_dialog_btn);
        textView=findViewById(R.id.msg_dialog_tv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick();
                }
                cancel();
            }
        });
    }

    public void setMessage(String msg){
        textView.setText(msg);
    }


    public interface Listener{
        void onClick();
    }
}
