package com.example.kkkkkn.my_2048;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkkkkn.my_2048.custom_view.GameView;
import com.example.kkkkkn.my_2048.custom_view.ScoreView;

public class GameActivity extends Activity {
    private Context mcontext;
    private final String TAG="2048小游戏";
    private GameView game_view;
    private TextView scoreTextView;
    private TextView gameRuleTextView;
    private long runtime;
    private int width,height;
    private int auto;
    private Chronometer time;
    private long score=0;
    private ScoreView scoreView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //绑定控件
        mcontext=getApplicationContext();
        game_view=findViewById(R.id.game_view);
        time=findViewById(R.id.timer);
        scoreTextView=findViewById(R.id.score);
        scoreView=new ScoreView(this);
        gameRuleTextView=findViewById(R.id.game_rule);
        gameRuleTextView.setText(R.string.rule);
        //开始计时
        time.setBase(SystemClock.elapsedRealtime());//计时器清零
        //获取屏幕宽度、高度
        WindowManager windowManager=getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        width = outMetrics.widthPixels;
        height = outMetrics.heightPixels;
        Log.i(TAG, "屏幕宽度是："+width+"屏幕高度是："+height);
        //判断自适应布局长度
        auto = Math.min(width, height);
        int size=(auto/4)-16;
        game_view.initView(4,size);
        game_view.setListener(new GameView.Listener() {
            @Override
            public void merge(int num) {
                if(num==0){
                    return;
                }
                score+=num;
                String str=String.valueOf(score);
                scoreTextView.setText(str);

                scoreView.show(num,scoreTextView);
            }

            @Override
            public void gameOver(boolean isFinish) {
                //弹窗显示完成游戏 还是游戏失败
                String str=isFinish?"游戏成功":"游戏失败";
                Toast.makeText(mcontext,str,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        //设置时间
        if(runtime!=0){
            time.setBase(runtime);
        }
        //开始计时
        time.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //记录运行时间
        runtime=time.getBase();
        //停止计时
        time.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //解除计时
        super.onDestroy();
    }
}
