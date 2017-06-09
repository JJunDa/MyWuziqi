package com.example.administrator.mywuziqi;

import android.graphics.Point;

import java.util.List;

import static com.example.administrator.mywuziqi.WuziqiView.GAME_OVER_NUM;

/**
 * Created by Administrator on 2017/6/8.
 */

public class CheckeUtil {
    private int x;
    private int y;
    private List<Point> points;

    public CheckeUtil(int x, int y, List<Point> points) {
        this.x = x;
        this.y = y;
        this.points = points;
    }

    //判断水平
     public boolean checkeIsFiveHorizontal() {
        int num = 1;
        for(int i = 1; i< GAME_OVER_NUM; i++){
            if(points.contains(new Point(x-i,y))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x+i,y))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        return false;
    }

    //判断垂直
    public boolean checkeIsFiveVertical() {
        int num = 1;
        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x,y-i))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x,y+i))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        return false;
    }

    //判断右斜
    public boolean checkeIsFiveRightTilt() {
        int num = 1;
        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x-i,y+i))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x+i,y-i))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        return false;
    }

    //判断左斜
    public boolean checkeIsFiveLeftTilt() {
        int num = 1;
        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x-i,y-i))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        for(int i=1; i<GAME_OVER_NUM; i++){
            if(points.contains(new Point(x+i,y+i))){
                num++;
            }else {
                break;
            }
        }
        if(num == GAME_OVER_NUM) return true;

        return false;
    }


}
