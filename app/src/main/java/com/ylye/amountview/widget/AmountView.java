package com.ylye.amountview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ylye.amountview.R;
import com.ylye.amountview.util.DensityUtil;

/**
 * Created by Administrator on 2017/3/2.
 * 购物车加减按钮
 */
public class AmountView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private Context mContext;

    // 最大的数量
    public static final int MAX_VALUE = 100;
    // 最小的数量
    public static final int MIN_VALUE = 1;

    private int countValue = 1;//数量
    private int maxValue = MAX_VALUE;

    private ImageView mIvAddButton, mIvSubButton;
    private EditText mEtCountInput;

    public AmountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView(context, attrs);
    }

    /**
     * 功能描述：设置最大数量
     */
    public void setMaxValue(int max) {
        this.maxValue = max;
    }

    /**
     * 功能描述：设置当前数量
     */
    public void setCurrentValue(int currentValue) {
        this.countValue = currentValue;
        mEtCountInput.setText("" + currentValue);
    }

    /**
     * 功能描述：获取当前数量
     */
    public int getCurrentValue() {
        return countValue;
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(mContext).inflate(R.layout.layout_shop_amount, this);

        mIvSubButton = (ImageView) findViewById(R.id.iv_shop_sub);
        mIvSubButton.setOnClickListener(this);

        mIvAddButton = (ImageView) findViewById(R.id.iv_shop_add);
        mIvAddButton.setOnClickListener(this);

        mEtCountInput = (EditText) findViewById(R.id.et_shop_count);
        mEtCountInput.addTextChangedListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AmountView);

        int maxCount = a.getInt(R.styleable.AmountView_amount_maxCount, 100);
        int currentCount = a.getInt(R.styleable.AmountView_amount_currentCount, 1);
        int btnWidth = a.getDimensionPixelSize(R.styleable.AmountView_amount_btnWidth, -1);
        int btnHeight = a.getDimensionPixelSize(R.styleable.AmountView_amount_btnHeight, -1);
        int inputMinWidth = a.getDimensionPixelSize(R.styleable.AmountView_amount_inputMinWidth, -1);
        int inputMinHeight = a.getDimensionPixelSize(R.styleable.AmountView_amount_inputMinHeight, -1);
        int inputTextSize = a.getDimensionPixelSize(R.styleable.AmountView_amount_inputTextSize, -1);
        // 减号
        ViewGroup.LayoutParams lpSub = mIvSubButton.getLayoutParams();
        lpSub.width = btnWidth;
        lpSub.height = btnHeight;
        mIvSubButton.setLayoutParams(lpSub);
        // 加号
        ViewGroup.LayoutParams lpAdd = mIvAddButton.getLayoutParams();
        lpAdd.width = btnWidth;
        lpAdd.height = btnHeight;
        mIvAddButton.setLayoutParams(lpAdd);
        // 输入框
        mEtCountInput.setMinWidth(inputMinWidth);
        mEtCountInput.setMinHeight(inputMinHeight);
        mEtCountInput.setTextSize(DensityUtil.px2sp(context, inputTextSize));

        setMaxValue(maxCount);
        setCurrentValue(currentCount);
        a.recycle();
        btnChangeWord();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_shop_add:
                addAction();
                break;
            case R.id.iv_shop_sub:
                subAction();
                break;


        }
    }

    /**
     * 加操作
     */
    private void addAction() {
        countValue++;
        btnChangeWord();
    }


    /**
     * 减操作
     */
    private void subAction() {
        countValue--;
        btnChangeWord();
    }

    /**
     * 功能描述：
     * 参数：boolean 是否需要重新赋值
     */
    private void changeWord(boolean needUpdate) {
        if (needUpdate) {
            mEtCountInput.removeTextChangedListener(this);
            if (!TextUtils.isEmpty(mEtCountInput.getText().toString().trim())) {  //不为空的时候才需要赋值
                mEtCountInput.setText(String.valueOf(countValue));
            }
            mEtCountInput.addTextChangedListener(this);
        }
        mEtCountInput.setSelection(mEtCountInput.getText().toString().trim().length());
        if (listener != null) {
            //
            listener.change(countValue);
        }
    }

    private void btnChangeWord() {
        mIvSubButton.setEnabled(countValue > MIN_VALUE);
        mIvAddButton.setEnabled(countValue < maxValue);
        mEtCountInput.setText(String.valueOf(countValue));
        mEtCountInput.setSelection(mEtCountInput.getText().toString().trim().length());
        if (listener != null) {
            //
            listener.change(countValue);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        boolean needUpdate = false;
        if (!TextUtils.isEmpty(s)) {
            countValue = Integer.valueOf(s.toString());
            if (countValue <= MIN_VALUE) {
                countValue = MIN_VALUE;
                mIvSubButton.setEnabled(false);
                mIvAddButton.setEnabled(true);
                needUpdate = true;
            } else if (countValue >= maxValue) {
                countValue = maxValue;
                mIvSubButton.setEnabled(true);
                mIvAddButton.setEnabled(false);
                needUpdate = true;

            } else {
                mIvSubButton.setEnabled(true);
                mIvAddButton.setEnabled(true);
            }
        } else {  //如果编辑框被清空了，直接填1
            countValue = 1;
            mIvSubButton.setEnabled(false);
            mIvAddButton.setEnabled(true);
            needUpdate = true;
        }
        changeWord(needUpdate);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    // 定义监听接口
    interface OnChangeCountListener {
        void change(int count);
    }

    // 声明接口变量
    private OnChangeCountListener listener;

    // 提供给外部使用的方法
    public void setOnChangeCountListener(OnChangeCountListener l) {
        listener = l;
    }

}
