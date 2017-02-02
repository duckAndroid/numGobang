package com.pythoncat.gobang;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.pythoncat.gobang.utils.ResultUtils;

import java.util.ArrayList;

/**
 * @author Administrator
 *         2017/2/2
 *         com.pythoncat.gobang
 */

public class GobangView extends View {

    private static final String INSTANCE_SUPER = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
    private final static int DEF_BORDER_COLOR = 0x88000000;
    private final static int DEF_NUM = 5; // 默认是 5子棋
    private final static int DEF_MAX_LINES = 8; // 默认是 8行8列
    private final static float DEF_SCALE = 3 * 1.0f / 4; // 默认 棋子直径是行高的 3/4
    private static final boolean DEF_WHITE_FIRST = true; // 白子先手!
    private int borderColor;
    private int maxLine;
    private int num;
    private float scale;
    private Paint mPaint;
    private int mWidth;
    private int mHeight;
    private float rowsHeight; // line height !
    private ArrayList<Point> mWhitePieceList = new ArrayList<>();
    private ArrayList<Point> mBlackPieceList = new ArrayList<>();
    private boolean isWhite;
    private Bitmap whitePiece;
    private Bitmap blackPiece;

    private OnGameOverListener mGameOverListener;
    private GameResult currentResult;
    private boolean gameOver;
    private static boolean isSave; // must static !!!

    public GobangView(Context context) {
        this(context, null);
    }

    public GobangView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GobangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray array = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.GobangView, defStyleAttr, 0);
        /*
         <attr name="num"/>
        <attr name="maxLines"/>
        <attr name="scale"/>
        <attr name="borderColor"/>
         */
        borderColor = array.getColor(R.styleable.GobangView_borderColor, DEF_BORDER_COLOR);
        maxLine = array.getInt(R.styleable.GobangView_maxLines, DEF_MAX_LINES);
        num = array.getInt(R.styleable.GobangView_num, DEF_NUM);
        if (maxLine < num || maxLine <= 2 || num < 2) {
            maxLine = DEF_MAX_LINES;
            num = DEF_NUM;
        }
        scale = array.getFloat(R.styleable.GobangView_scale, DEF_SCALE);
        if (scale > 1) scale = DEF_SCALE;
        isWhite = array.getBoolean(R.styleable.GobangView_whiteFirst, DEF_WHITE_FIRST);
        array.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(borderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        whitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureLength(widthMeasureSpec);
        int height = measureLength(heightMeasureSpec);
        Log.e("measure 1", "w = " + width + " , h = " + height);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
        Log.e("measure 2", "w = " + width + " , h = " + height);
    }

    private int measureLength(int measureSpec) {
        final int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            final ViewParent parent = getParent();
            if (parent instanceof View) {
                View pv = (View) parent;
                size = Math.min(pv.getMeasuredWidth(), pv.getMeasuredHeight());
            }
        }
        return size;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        this.rowsHeight = w * 1f / maxLine;
        whitePiece = Bitmap.createScaledBitmap(whitePiece,
                (int) (scale * rowsHeight), (int) (scale * rowsHeight), false);
        blackPiece = Bitmap.createScaledBitmap(blackPiece,
                (int) (scale * rowsHeight), (int) (scale * rowsHeight), false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawLines(canvas);
        drawPieces(canvas);
        gameOver = gameOver();
        Log.e("gameover", "gv = " + gameOver);
        if (gameOver && mGameOverListener != null) {
            mGameOverListener.isOver(currentResult);
        }
    }

    private boolean gameOver() {
        boolean whiteWin = ResultUtils.isCurrentWin(mWhitePieceList, num);
        boolean blackWin = ResultUtils.isCurrentWin(mBlackPieceList, num);
        if (whiteWin) {
            currentResult = GameResult.WhiteWin;
        } else if (blackWin) {
            currentResult = GameResult.BlackWin;
        } else {
            if (mWhitePieceList.size() + mBlackPieceList.size() >= maxLine * maxLine) {
                currentResult = GameResult.Dogfail;
            } else {
                currentResult = GameResult.NotFinished;
            }
        }
        return currentResult != GameResult.NotFinished;
    }

    public void setGameOverListener(OnGameOverListener listener) {
        this.mGameOverListener = listener;
    }

    private void drawPieces(Canvas canvas) {
        Log.w("wh", "mw = " + mWhitePieceList);
        Log.w("bh", "mb = " + mBlackPieceList);
        for (int i = 0, n = mWhitePieceList.size(); i < n; i++) {
            final Point p = mWhitePieceList.get(i);
            // (135,236) ...
            int realX = (int) (rowsHeight * (0.5 - scale * 0.5 + p.x));
            int realY = (int) (rowsHeight * (0.5 - scale * 0.5 + p.y));
            canvas.drawBitmap(whitePiece, realX, realY, null);
        }
        for (int i = 0, n = mBlackPieceList.size(); i < n; i++) {
            final Point p = mBlackPieceList.get(i);
            // (135,236) ...
            int realX = (int) (rowsHeight * (0.5 - scale * 0.5 + p.x));
            int realY = (int) (rowsHeight * (0.5 - scale * 0.5 + p.y));
            canvas.drawBitmap(blackPiece, realX, realY, null);
        }
    }

    private void drawLines(Canvas canvas) {
        for (int i = 0; i < maxLine; i++) {
            int startX = (int) (rowsHeight / 2);
            int endX = (int) (mWidth - rowsHeight / 2);
            int y = (int) (rowsHeight / 2 + rowsHeight * i);
            canvas.drawLine(startX, y, endX, y, mPaint);
        }
        for (int i = 0; i < maxLine; i++) {
            int startY = (int) (rowsHeight / 2);
            int endY = (int) (mHeight - rowsHeight / 2);
            int x = (int) (rowsHeight / 2 + rowsHeight * i);
            canvas.drawLine(x, startY, x, endY, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameOver) return false;
        final int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            final float x = event.getX();
            final float y = event.getY();
            Point p = getValidPoint(x, y);
            if (this.mWhitePieceList.contains(p) || this.mBlackPieceList.contains(p)) {
                return true;
            }
            if (isWhite) {
                this.mWhitePieceList.add(p);
            } else {
                this.mBlackPieceList.add(p);
            }
            invalidate();
            isWhite = !isWhite;
        }
        return true;
    }

    private Point getValidPoint(float x, float y) {
        // (0,0) , (2,5)
        return new Point((int) (x / rowsHeight), (int) (y / rowsHeight));
    }


    public void reStart() {
        mWhitePieceList.clear();
        mBlackPieceList.clear();
        gameOver = false;
        currentResult = GameResult.NotFinished;
        invalidate();
    }

    public enum GameResult {
        WhiteWin, BlackWin, Dogfail, NotFinished
    }

    public interface OnGameOverListener {
        void isOver(GameResult result);
    }


    @Override
    protected Parcelable onSaveInstanceState() {
        isSave = true;
        final Bundle bundle = new Bundle();
        bundle.putBoolean(INSTANCE_GAME_OVER, gameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhitePieceList);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackPieceList);
        bundle.putParcelable(INSTANCE_SUPER, super.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (isSave && state instanceof Bundle) {
            isSave = false;
            Bundle bundle = (Bundle) state;
            gameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhitePieceList = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackPieceList = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            state = bundle.getParcelable(INSTANCE_SUPER);
        }
        super.onRestoreInstanceState(state);
    }

}
