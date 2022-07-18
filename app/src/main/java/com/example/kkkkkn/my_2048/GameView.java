package com.example.kkkkkn.my_2048;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.Arrays;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

//网格布局样式   4*4
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    private float start_x,start_y,end_x,end_y;
    private boolean gameOver;
    private int columnCount;
    private Card[][] cards;
    private final ArrayList<Point> emptyList=new ArrayList<>();

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void initView(int count,int size) {
        this.columnCount=count;
        setColumnCount(columnCount);
        //初始化布局
        initCards(size,size);
        //设置监听
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //手势操作的判断
                //Log.i(TAG, "获取到触摸事件了: "+event.getAction());
                switch(event.getAction()){
                    case ACTION_DOWN:
                        start_x=event.getX();
                        start_y=event.getY();
                        break;
                    case ACTION_UP:
                        end_x=event.getX();
                        end_y=event.getY();
                        float ax=end_x-start_x;
                        float ay=end_y-start_y;
                        if(Math.abs(ax)>Math.abs(ay)){
                            //左右手势
                            if(ax>0){
                                //右手势
                                slideRight();
                            }else if(ax<0){
                                //左手势
                                slideLeft();
                            }
                        }else if(Math.abs(ax)<Math.abs(ay)){
                            //上下手势
                            if(ay>0){
                                slideDown();
                            }else if(ay<0){
                                slideUp();
                            }
                        }
                        //判断游戏是否结束
                        boolean flag=isGameOver();
                        if (flag){
                            Log.i(TAG, "onTouch: 游戏结束");
                        }
                        break;
                }
                return true;
            }
        });
        addRandomCardNum();
    }

    private boolean isGameOver() {
        return gameOver;
    }

    private void slideLeft(){
        Log.i(TAG, "slideLeft: 左滑");
        //思路， 从每行最右边开始遍历，取到值，然后遍历左边，直到找到数字或到头，
        for (int i = 0; i < columnCount; i++) {
            slide_process(Arrays.copyOf(cards[i],columnCount));
        }
        //修改完所有之后，调用展示动画

    }
    private void slideRight(){
        Log.i(TAG, "slideRight: 右滑");
        Card[] arr=new Card[columnCount];
        for (int i = 0; i <columnCount ; i++) {
            for (int j = columnCount-1; j >=0; j--) {
                arr[i]=cards[i][j];
            }
            slide_process(arr);

        }

    }
    private void slideUp(){
        Log.i(TAG, "slideUp: 上划");
        Card[] arr=new Card[columnCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j <columnCount; j++) {
                arr[i]=cards[j][i];
            }
            slide_process(arr);
        }

    }
    private void slideDown(){
        Log.i(TAG, "slideDown: 下滑");
        Card[] arr=new Card[columnCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = columnCount-1; j >=0; j--) {
                arr[i]=cards[j][i];
            }
            slide_process(arr);
        }
    }

    private void slide_process(Card[] arr){
        int index=0;
        int num=0;
        for (Card card : arr) {
            if (card.getShowNum() == 0) {
            } else if (num == 0) {
                num = card.getShowNum();
                card.setShowNum(0);
            } else if (num == card.getShowNum()) {
                arr[index].setShowNum(card.getShowNum() * 2);
                card.setShowNum(0);
                index++;
                num = 0;
            }
        }
        if(num!=0){
            arr[index].setShowNum(num);
        }
    }




    //随机位置添加卡片
    private void initCards(int cardWidth,int cardHeight){
        Card card;
        cards=new Card[columnCount][columnCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if((i==columnCount-1)&&(j==columnCount-1)){
                    card = new Card(getContext(), 16, 16, 16,16);
                }else if (i == columnCount - 1) {
                    // 为最底下的格子加上bottomMargin
                    card = new Card(getContext(), 16, 16, 0,16);
                } else if(j==columnCount-1){
                    card = new Card(getContext(), 16, 16, 16,0);
                }else {
                    card = new Card(getContext(), 16, 16, 0,0);
                }
                card.setShowNum(0);
                addView(card,cardWidth,cardHeight);
                cards[i][j]=card;
            }
        }
    }

    private void getEmptyCard(){
        emptyList.clear();
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if(cards[i][j].getShowNum()<=0){
                    emptyList.add(new Point(i,j));
                }
            }
        }
    }

    private void addRandomCardNum(){
        getEmptyCard();
        if(!emptyList.isEmpty()){
            Point point=emptyList.get((int) (Math.random()*emptyList.size()));
            cards[point.x][point.y].setShowNum(Math.random()>0.5?2:4);

            //todo 设置动画
            setAppearAnim(cards[point.x][point.y]);
        }
    }

    //设置动画
    private void setAppearAnim(Card card) {

    }


}
