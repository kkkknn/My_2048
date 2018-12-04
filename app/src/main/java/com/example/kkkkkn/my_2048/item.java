package com.example.kkkkkn.my_2048;

//单元格实体类
public class item {
    private int value;

    public item(int value) {
        this.value = value;
    }


    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
