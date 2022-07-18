package com.example.kkkkkn.my_2048;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Chronometer;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends Activity {
    private Context mcontext;
    private final String TAG="2048小游戏";
    private GameView game_view;
    private ArrayList<item> list=null;
    private BaseAdapter mbaseadapter=null;
    private int start_x,start_y,end_x,end_y;
    private long runtime;
    private Random run=new Random();
    private int point;
    private int width,height;
    private int auto;
    private Chronometer time;
    private ArrayList<String> blank=new ArrayList<>();
    private ArrayList<String> data=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //绑定控件
        mcontext=getApplicationContext();
        game_view=findViewById(R.id.game_view);
        time=findViewById(R.id.timer);

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
        if(width>height){
            auto=height;
        }else{
            auto=width;
        }
        int size=(auto/4)-16;
        game_view.initView(4,size);
    }

    @Override
    protected void onResume() {
        //设置时间
        if(runtime!=0){
            time.setBase(runtime);
            Log.i(TAG, "onResume: "+runtime);
        }
        //开始计时
        time.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        //记录运行时间
        runtime=time.getBase();
        Log.i(TAG, "onPause: "+runtime);
        //停止计时
        time.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //解除计时
        super.onDestroy();
    }

    /**
     * 初始化网格数字
    * */
    public int[][] Initarray(int[][] array){
        int x1=run.nextInt(4);
        int x2=run.nextInt(4);
        int y1=run.nextInt(4);
        int y2=run.nextInt(4);
        while(x1==x2&&y1==y2){
            x2=run.nextInt(4);
            y2=run.nextInt(4);
        }
        array[x1][y1]=2;
        array[x2][y2]=2;
        return array;
    }

    /**
     *产生随机数并添加到网格       先进行手势操作之后再去添加随机数
     */
    public int[][] changearray(int[][] array){

        //清空空白坐标数组
        blank.clear();
        //遍历数组记录空白坐标
        for(int i=0;i<array.length;i++){
            for (int j=0;j<array[i].length;j++){
                if(array[i][j]==0){
                    blank.add(i+","+j);
                }
            }
        }

        if(blank.size()==0){
            //游戏结束
            return null;
        }else{
            //生成随机位置的2一个
            int flag=run.nextInt(blank.size());
            String str=blank.get(flag);
            String[] array_str=str.split(",");
            int x=Integer.parseInt(array_str[0]);
            int y=Integer.parseInt(array_str[1]);
            //赋值随机数给数组
            array[x][y]=2;
            //生成随机数之后判断相邻是否有相同数据或者空白
            for(int i=0;i<(array.length-1);i++){
                for(int j=0;(j<array[i].length-1);j++){
                    if(array[i][j]==array[i+1][j]||array[i][j]==array[i][j+1]||array[i][j]==0||array[i+1][j]==0||array[i][j+1]==0||array[i+1][j+1]==0){
                        //提前结束循环
                        return array;
                    }
                }
            }
            //游戏结束
            return null;
        }
    }

    /**手势处理  i是行，j是列
     * @param array
     * @param str
     * @return
     */
    public int[][] sign(int[][] array,String str){
        //指针初始化
        point=0;

        switch (str){
            case "up":
                for(int i=0;i<4;i++){
                    point=0;
                    for(int j=0;j<3;j++){
                        if(array[j+1][i]==0){
                            continue;
                        }else{
                            if(array[point][i]==array[j+1][i]){
                                array[point][i]=array[j+1][i]*2;
                                array[j+1][i]=0;
                                point++;
                            }else if(array[point][i]==0){
                                array[point][i]=array[j+1][i];
                                array[j+1][i]=0;
                            }else{
                                point++;
                                array[point][i]=array[j+1][i];
                                if(point!=j+1&&point!=3){
                                    array[j+1][i]=0;
                                }
                            }
                        }
                    }
                }

                break;
            case "down":
                for(int i=0;i<4;i++){
                    point=3;
                    for(int j=3;j>0;j--){
                        if(array[j-1][i]==0){
                            continue;
                        }else{
                            if(array[point][i]==array[j-1][i]){
                                array[point][i]=array[j-1][i]*2;
                                array[j-1][i]=0;
                                point--;
                            }else if(array[point][i]==0){
                                array[point][i]=array[j-1][i];
                                array[j-1][i]=0;
                            }else{
                                point--;
                                array[point][i]=array[j-1][i];
                                if(point!=j-1&&point!=0){
                                    array[j-1][i]=0;
                                }
                            }
                        }
                    }
                }
                break;
            case "left":
                for(int i=0;i<4;i++){
                    point=0;
                    for(int j=0;j<3;j++){
                        if(array[i][j+1]==0){
                            continue;
                        }else{
                            if(array[i][point]==array[i][j+1]){
                                array[i][point]=array[i][j+1]*2;
                                array[i][j+1]=0;
                                point++;
                            }else if(array[i][point]==0){
                                array[i][point]=array[i][j+1];
                                array[i][j+1]=0;
                            }else{
                                point++;
                                array[i][point]=array[i][j+1];
                                if(point!=j+1&&point!=3){
                                    array[i][j+1]=0;
                                }
                            }
                        }
                    }
                }
                break;
            case "right":
                for(int i=0;i<4;i++){
                    point=3;
                    for(int j=3;j>0;j--){
                        if(array[i][j-1]==0){
                            continue;
                        }else{
                            if(array[i][point]==array[i][j-1]){
                                array[i][point]=array[i][j-1]*2;
                                array[i][j-1]=0;
                                point--;
                            }else if(array[i][point]==0){
                                array[i][point]=array[i][j-1];
                                array[i][j-1]=0;
                            }else{
                                point--;
                                array[i][point]=array[i][j-1];
                                if(point!=j-1&&point!=0){
                                    array[i][j-1]=0;
                                }
                            }
                        }
                    }
                }
                break;
        }
        return array;
    }
}
