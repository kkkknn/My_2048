package com.example.kkkkkn.my_2048.custom_view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

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
        addRandomCardNum();
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
            boolean flag=slideProcess(arrI,arrJ,columnCount);
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
            boolean flag=slideProcess(arrI,arrJ,columnCount);
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
            boolean flag=slideProcess(arrI,arrJ,columnCount);
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
            boolean flag=slideProcess(arrI,arrJ,columnCount);
            if(flag){
                isSlide =true;
            }
        }
        return isSlide;
    }

    private boolean slideProcess(int[] arrI, int[] arrJ, int len){
        //2次遍历，第一次合并，第二次移动位置
        boolean isSlide =false;
        int lastNum=-1;
        int lastIndex=-1;
        boolean isChange=false;
        for (int i = 0; i < len; i++) {
            Card card=cards[arrI[i]][arrJ[i]];
            int num=card.getShowNum();
            if(num!=0){
                if(lastNum!=num){
                    if(lastIndex!=-1){
                        cards[arrI[lastIndex]][arrJ[lastIndex]].setShowNum(lastNum);
                    }
                    lastNum=num;
                    lastIndex=i;
                }else {
                    cards[arrI[lastIndex]][arrJ[lastIndex]].setShowNum(lastNum*2);
                    if(listener!=null){
                        listener.merge(lastNum*2);
                        if ((lastNum*2)==2048){
                            listener.gameOver(true);
                        }
                    }
                    lastIndex=-1;
                    lastNum=-1;
                }
                card.setShowNum(0);
                isChange=true;
            }
            if (i==(len-1)&&lastNum!=-1){
                cards[arrI[lastIndex]][arrJ[lastIndex]].setShowNum(lastNum);
                isChange=true;
            }
        }

        //移动位置
        for (int i = 0; i < len; i++) {
            Card card=cards[arrI[i]][arrJ[i]];
            int num=card.getShowNum();
            if(num!=0){
                //反向遍历，直到找到下一个非0，或最后一个0
                for (int j = i-1; j >=0 ; j--) {
                    Card reverseCard=cards[arrI[j]][arrJ[j]];
                    int reverseNum=reverseCard.getShowNum();
                    if(reverseNum!=0){
                        //开始调换
                        int index=j+1;
                        if(index<i){
                            cards[arrI[index]][arrJ[index]].setShowNum(num);
                            card.setShowNum(0);
                            isSlide =true;
                            isChange=true;
                        }
                        break;
                    }
                    if(j==0){
                        cards[arrI[j]][arrJ[j]].setShowNum(num);
                        card.setShowNum(0);
                        isSlide =true;
                        isChange=true;
                    }
                }
            }
        }
        if(!isChange&&listener!=null){
            listener.gameOver(false);
        }
        return isSlide;
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

    public interface Listener{
        void merge(int num);
        void gameOver(boolean isFinish);
    }

}
