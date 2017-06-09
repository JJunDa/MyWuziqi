package com.example.administrator.mywuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class WuziqiView extends View {
    private int mPanelWidth;
    private float mLineHight;
    private int MAX_LINE = 10;
    public final static int GAME_OVER_NUM = 5;
    private Paint mPaint  = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float pieceRatio = 3 * 1.0f / 4;

    //判断是否为白子（或）白子先行
    private boolean misWhite = true;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean misGameOver;
    private boolean misWhiteWinner;

    private final String INSTANCE = "instance";
    private final String INSTANCE_GAME_OVER = "instance_game_over";
    private final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    private CheckeUtil mCheckeUtil;

    public WuziqiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    //初始化画笔，黑白棋子
    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(),R.drawable.stone_b1);
    }

    //测量方法（根据模式确定自定义View的大小）
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);

        if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }else if(heightMode == MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }
        setMeasuredDimension(width,width);
    }

    //尺寸的修改（与尺寸有关的修改时写入这个方法）
    //行高，棋子大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceSize = (int) (mLineHight * pieceRatio);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece,pieceSize,pieceSize,false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece,pieceSize,pieceSize,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameIsOver();
    }

    //判断游戏是否结束
    private void checkGameIsOver() {
        boolean isWhiteWin = CheckFivePieceOnLine(mWhiteArray);
        boolean isBlackWin = CheckFivePieceOnLine(mBlackArray);

        if(isBlackWin || isWhiteWin){
            misGameOver = true;
            misWhiteWinner = isWhiteWin;

            String overText = misWhiteWinner? "白棋胜利！": "黑棋胜利！";
            Toast.makeText(getContext(),overText,Toast.LENGTH_LONG).show();
        }

    }

    //是否有五个棋子在同一线上
    private boolean CheckFivePieceOnLine(List<Point> points) {
        for(Point p: points){
            int x = p.x;
            int y = p.y;

            mCheckeUtil = new CheckeUtil(x,y,points);
            boolean win = mCheckeUtil.checkeIsFiveHorizontal();
            if(win) return true;
            win = mCheckeUtil.checkeIsFiveVertical();
            if(win) return true;
            win = mCheckeUtil.checkeIsFiveLeftTilt();
            if(win) return true;
            win = mCheckeUtil.checkeIsFiveRightTilt();
            if(win) return true;
        }
        return false;
    }

    //画交互的所有黑白棋子
    private void drawPiece(Canvas canvas) {
        for(int i=0, n=mWhiteArray.size(); i<n; i++){
            Point whitePoint = mWhiteArray.get(i);
            //x = 白点.X +  间隙（1/8的行高）
            float x = (whitePoint.x + (1 - pieceRatio)/2)* mLineHight;
            float y = (whitePoint.y + (1 - pieceRatio)/2) *mLineHight;
            canvas.drawBitmap(mWhitePiece, x, y,null);
        }
        for(int i=0, n=mBlackArray.size(); i<n; i++){
            Point whitePoint = mBlackArray.get(i);
            float x = (whitePoint.x + (1 - pieceRatio)/2)* mLineHight;
            float y = (whitePoint.y + (1 - pieceRatio)/2) *mLineHight;
            canvas.drawBitmap(mBlackPiece, x, y,null);
        }
    }

    //棋子位置
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //判断游戏结束没
        if(misGameOver) return false;

        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getPoint(x,y);
            //判断该点范围内是否有棋子
            if(mWhiteArray.contains(p) || mBlackArray.contains(p)){
               return false;
            }

            if(misWhite){
                mWhiteArray.add(p);
            }else {
                mBlackArray.add(p);
            }
            invalidate();
            misWhite = !misWhite;
        }
        return true;
    }

    private Point getPoint(int x, int y) {
        //点击位置除以行高，表在同一个行高范围内点击为同一个点
        return new Point((int) (x / mLineHight), (int) (y / mLineHight));
    }

    //画棋盘的线
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHight = mLineHight;

        for(int i=0; i < MAX_LINE; i++){
            int starX = (int) (lineHight/2);
            int endX = (int) (w - lineHight/2);
            int Y = (int) ((0.5 + i) * lineHight);

            canvas.drawLine(starX,Y,endX,Y,mPaint);
            canvas.drawLine(Y,starX,Y,endX,mPaint);
        }
    }

    //View的储存
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundler = new Bundle();
        bundler.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundler.putBoolean(INSTANCE_GAME_OVER,misGameOver);
        bundler.putParcelableArrayList(INSTANCE_WHITE_ARRAY,mWhiteArray);
        bundler.putParcelableArrayList(INSTANCE_BLACK_ARRAY,mBlackArray);
        return bundler;
    }

    //View的恢复
    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundler = (Bundle) state;
            misGameOver = bundler.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundler.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundler.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundler.getParcelable(INSTANCE));

            return;
        }
        super.onRestoreInstanceState(state);
    }

    //再来一局
    public void reStart(){
        mBlackArray.clear();
        mWhiteArray.clear();
        misGameOver=false;
        misWhiteWinner = false;
        misWhite = true;
        invalidate();
    }

    public void BackOneStep(){
        if(!misWhite){
            int i = mWhiteArray.size();
            if(i>0){
                mWhiteArray.remove(i-1);
                misWhite = !misWhite;
                invalidate();
            }else {
                Toast.makeText(getContext(),"已经没棋子了",Toast.LENGTH_LONG).show();
            }

        }else {
            int i = mBlackArray.size();
            if(i>0) {
                mBlackArray.remove(i - 1);
                misWhite = !misWhite;
                invalidate();
            }else {
                Toast.makeText(getContext(),"已经没棋子了",Toast.LENGTH_LONG).show();
            }
        }
    }
}
