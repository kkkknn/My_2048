package com.example.kkkkkn.my_2048.custom_view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;

import com.example.kkkkkn.my_2048.R;

import java.util.ArrayList;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

//网格布局样式   4*4
public class GameView extends GridLayout {
    private static final String TAG = "GameView";
    private float start_x,start_y,end_x,end_y;
    private int columnCount;
    private Card[][] cards;
    private Listener listener;
    private final ArrayList<Point> emptyList=new ArrayList<>();
    private Animation addAnimation ;
    private Animation newAnimation ;

    public GameView(Context context) {
        super(context);
        addAnimation = AnimationUtils.loadAnimation(context, R.anim.card_scale_add);
        newAnimation = AnimationUtils.loadAnimation(context, R.anim.card_scale_new);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addAnimation = AnimationUtils.loadAnimation(context, R.anim.card_scale_add);
        newAnimation = AnimationUtils.loadAnimation(context, R.anim.card_scale_new);
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
                        boolean isSlide =false;
                        if(Math.abs(ax)>Math.abs(ay)){
                            //左右手势
                            if(ax>0){
                                //右手势
                                isSlide =slideRight();
                            }else if(ax<0){
                                //左手势
                                isSlide =slideLeft();
                            }
                        }else if(Math.abs(ax)<Math.abs(ay)){
                            //上下手势
                            if(ay>0){
                                isSlide =slideDown();
                            }else if(ay<0){
                                isSlide =slideUp();
                            }
                        }
                        //判断是否结束游戏
                        boolean flag=isGameOver();
                        if(flag){
                            listener.gameOver(false);
                        }
                        //已滑动状态，进行添加数字
                        if(isSlide){
                            addRandomCardNum();
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        getEmptyCard();
        addRandomCardNum();
    }

    //游戏是否结束
    private boolean isGameOver() {
        getEmptyCard();
        return emptyList.isEmpty();
    }

    public void setListener(Listener listener){
        this.listener=listener;
    }

    private boolean slideLeft(){
        Log.i(TAG, "slideLeft: 左滑");
        boolean isMerge=false;
        //思路， 从每行最右边开始遍历，取到值，然后遍历左边，直到找到数字或到头，
        for (int i = 0; i <columnCount ; i++) {
            int[] arrI =new int[columnCount];
            int[] arrJ =new int[columnCount];
            for (int j = 0; j <columnCount; j++) {
                arrI[j]=i;
                arrJ[j]=j;
            }
            boolean flag=slideProcess(arrI,arrJ);
            if(flag){
                isMerge=true;
            }
        }
        //修改完所有之后，调用展示动画
        return isMerge;
    }

    private boolean slideRight(){
        Log.i(TAG, "slideRight: 右滑");
        boolean isSlide =false;
        for (int i = 0; i <columnCount ; i++) {
            int[] arrI =new int[columnCount];
            int[] arrJ =new int[columnCount];
            int count=0;
            for (int j = columnCount-1; j >=0; j--) {
                arrI[count]=i;
                arrJ[count]=j;
                count++;
            }
            boolean flag=slideProcess(arrI,arrJ);
            if(flag){
                isSlide =true;
            }

        }
        return isSlide;
    }

    private boolean slideUp(){
        Log.i(TAG, "slideUp: 上划");
        boolean isSlide=false;
        for (int i = 0; i < columnCount; i++) {
            int[] arrI =new int[columnCount];
            int[] arrJ =new int[columnCount];
            for (int j = 0; j <columnCount; j++) {
                arrI[j]=j;
                arrJ[j]=i;
            }
            boolean flag=slideProcess(arrI,arrJ);
            if(flag){
                isSlide=true;
            }
        }
        return isSlide;
    }

    private boolean slideDown(){
        Log.i(TAG, "slideDown: 下滑");
        boolean isSlide =false;
        for (int i = 0; i < columnCount; i++) {
            int[] arrI =new int[columnCount];
            int[] arrJ =new int[columnCount];
            int count=0;
            for (int j = columnCount-1; j >=0; j--) {
                arrI[count]=j;
                arrJ[count]=i;
                count++;
            }
            boolean flag=slideProcess(arrI,arrJ);
            if(flag){
                isSlide =true;
            }
        }
        return isSlide;
    }
    private boolean slideProcess(int[] arrI, int[] arrJ){
        boolean isChange=false;
        int gameOver=0;
        int score=0;
        ArrayList<Card> arrayList=new ArrayList();
        for (int i = 0; i < columnCount; i++) {
            Card card=cards[arrI[i]][arrJ[i]];
            for (int j = i+1; j < columnCount; j++) {
                Card card_next=cards[arrI[j]][arrJ[j]];
                if(card_next.getShowNum()==0){
                    continue;
                }else if(card.getShowNum()==card_next.getShowNum()){
                    int num=card_next.getShowNum()*2;
                    if (num==2048){
                        gameOver=1;
                    }
                    score+=num;
                    card.setShowNum(num);
                    card_next.setShowNum(0);
                    arrayList.add(card);
                    isChange=true;
                }else if(card.getShowNum()==0){
                    card.setShowNum(card_next.getShowNum());
                    card_next.setShowNum(0);
                    isChange=true;
                }
            }
        }
        if(listener!=null){
            listener.merge(score);
            if(gameOver==1){
                listener.gameOver(true);
            }
        }
        if(!arrayList.isEmpty()){
            for (Card card:arrayList) {
                card.startAnimation(addAnimation);
            }
        }
        return isChange;
    }





    //随机位置添加卡片
    private void initCards(int cardWidth,int cardHeight){
        Card card;
        cards=new Card[columnCount][columnCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                if((i==columnCount-1)&&(j==columnCount-1)){
                    card = new Card(getContext(), 25, 25, 25,25);
                }else if (i == columnCount - 1) {
                    // 为最底下的格子加上bottomMargin
                    card = new Card(getContext(), 25, 25, 0,25);
                } else if(j==columnCount-1){
                    card = new Card(getContext(), 25, 25, 25,0);
                }else {
                    card = new Card(getContext(), 25, 25, 0,0);
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
        if(!emptyList.isEmpty()){
            Point point=emptyList.get((int) (Math.random()*emptyList.size()));
            cards[point.x][point.y].setShowNum(Math.random()>0.5?2:4);

            setAppearAnim(cards[point.x][point.y]);
        }

    }

    //设置动画
    private void setAppearAnim(Card card) {
        if(newAnimation!=null){
            card.startAnimation(newAnimation);
        }
    }

    public interface Listener{
        void merge(int num);
        void gameOver(boolean isFinish);
    }

}
