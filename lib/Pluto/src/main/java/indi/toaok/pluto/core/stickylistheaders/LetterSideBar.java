package indi.toaok.pluto.core.stickylistheaders;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import indi.toaok.pluto.R;


public class LetterSideBar extends View {
    private static final int TEXT_SIZE_DEF = 12;

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private List<String> mLetterData;

    public final String[] mLetters = {"中国", "热门", "A", "B", "C", "D", "E", "F", "G", "H",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private int mChooseIndex = -1;
    private TextPaint mPaint = new TextPaint();

    private int mSelectLetterColor;
    private int mNormalLetterColor;
    private int mLetterSize;
    private TextView mTextDialog;

    public LetterSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterSideBar);
        this.mNormalLetterColor = typedArray.getColor(R.styleable.LetterSideBar_normalLetterColor, Color.BLACK);
        this.mSelectLetterColor = typedArray.getColor(R.styleable.LetterSideBar_selectLetterColor, Color.RED);
        this.mLetterSize = (int) typedArray.getDimension(R.styleable.LetterSideBar_letterSize, TEXT_SIZE_DEF);
        typedArray.recycle();
        init();
    }

    public LetterSideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context) {
        this(context, null);
    }


    private void init() {
        if (mLetterData == null) {
            mLetterData = new ArrayList<>();
        }
        mLetterData.addAll(Arrays.asList(mLetters));
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public void setSlidData(List<String> data) {
        if (mLetterData == null) {
            mLetterData = new ArrayList<>();
        }
        mLetterData.clear();
        mLetterData.addAll(data);
        invalidate();
    }

    // 起始Y
    int startY = 0;
    // 结束Y
    int endY = 0;
    // 每项高度
    int itemHeight = 0;
    // 每项高度 = 字体高度 * 倍率
    final float paddingMagnification = 2f;

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = getHeight();
        int width = getWidth();
        int size = mLetterData.size();


        itemHeight = (int) (getFontHeight() * paddingMagnification);
        int realityHeight = size * itemHeight;

        // 每项高度
        if (itemHeight > height / size) {
            itemHeight = height / size;
        }


        startY = (height - realityHeight) / 2;
        endY = startY + realityHeight;

        for (int i = 0; i < size; i++) {
            mPaint.setColor(mNormalLetterColor);
            mPaint.setTypeface(Typeface.DEFAULT);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(mLetterSize);
            // 选中的状态
            if (i == mChooseIndex) {
                mPaint.setColor(mSelectLetterColor);
                mPaint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - mPaint.measureText(mLetterData.get(i)) / 2;
            float yPos = itemHeight * i + startY;
            canvas.drawText(mLetterData.get(i), xPos, yPos, mPaint);
            mPaint.reset();
        }
    }

    /**
     * 获取字体高度
     *
     * @return
     */
    public int getFontHeight() {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.top) + 2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChooseIndex;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;

        int min = startY - itemHeight;
        int max = endY - itemHeight;
        if (y < min || y > max) {
            setBackgroundDrawable(new ColorDrawable(0x00000000));
            mChooseIndex = -1;//
            invalidate();
            if (mTextDialog != null) {
                mTextDialog.setVisibility(View.INVISIBLE);
            }
            return true;
        }


        final int c = (int) ((y + itemHeight - startY) / itemHeight);
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                mChooseIndex = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < mLetterData.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(mLetterData.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(mLetterData.get(c));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        mChooseIndex = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }


}