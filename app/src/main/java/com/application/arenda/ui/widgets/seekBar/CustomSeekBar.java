package com.application.arenda.ui.widgets.seekBar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.application.arenda.R;
import com.application.arenda.entities.utils.DisplayUtils;
import com.application.arenda.ui.widgets.seekBar.interfaces.OnRangeSeekbarChangeListener;
import com.application.arenda.ui.widgets.seekBar.interfaces.OnSeekbarChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomSeekBar extends FrameLayout {

    @BindView(R.id.textTitleBar)
    TextView textTitleBar;

    @BindView(R.id.textMinValue)
    TextView textMinValue;

    @BindView(R.id.textMaxValue)
    TextView textMaxValue;

    @BindView(R.id.textProgressSeekBar)
    TextView textProgressSeekBar;

    @BindView(R.id.seekBarRelativeLayout)
    RelativeLayout layout;

    private SeekBarSingle seekbarSingle;
    private SeekBarRange seekbarRange;

    private String titleBar, titleMin, titleMax, titleProgress;
    private float maxValue, minValue, cornerRadius, step;
    private int mode, dataType, gap;

    private RelativeLayout.LayoutParams params;

    private OnSeekbarChangeListener onSeekbarChangeListener;
    private OnRangeSeekbarChangeListener onRangeSeekbarChangeListener;

    public CustomSeekBar(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CustomSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.seek_bar_layout, this);

        ButterKnife.bind(this);

        initAttrs(attrs, defStyle);

        initUI();
    }

    private void initAttrs(AttributeSet attrs, int defStyle) {
        final TypedArray attr = getContext().obtainStyledAttributes(
                attrs, R.styleable.CustomSeekBar, defStyle, 0);

        titleBar = attr.getString(R.styleable.CustomSeekBar_seekBarTitle);
        titleMin = attr.getString(R.styleable.CustomSeekBar_minValueTitle);
        titleProgress = attr.getString(R.styleable.CustomSeekBar_seekBarProgress);
        titleMax = attr.getString(R.styleable.CustomSeekBar_maxValueTitle);

        cornerRadius = attr.getInt(R.styleable.CustomSeekBar_seekCornerRadius, 0);
        dataType = attr.getInt(R.styleable.CustomSeekBar_seekDataType, 2);
        mode = attr.getInt(R.styleable.CustomSeekBar_seekMode, 0);
        step = attr.getFloat(R.styleable.CustomSeekBar_seekSteps, -1f);
        gap = attr.getInt(R.styleable.CustomSeekBar_seekGap, 0);


        minValue = attr.getFloat(R.styleable.CustomSeekBar_seekMinValue, 0f);
        maxValue = attr.getFloat(R.styleable.CustomSeekBar_seekMaxValue, 10f);

        attr.recycle();
    }

    private void initUI() {
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, DisplayUtils.dpToPx(6), 0, 0);
        params.addRule(RelativeLayout.BELOW, textProgressSeekBar.getId());

        textTitleBar.setText(titleBar);
        textMaxValue.setText(titleMax);
        textMinValue.setText(titleMin);

        if (mode == 0) {
            seekbarSingle = new SeekBarSingle(getContext());
            seekbarSingle.setMinValue(minValue);
            seekbarSingle.setMaxValue(maxValue);
            seekbarSingle.setBarColor(getContext().getResources().getColor(R.color.colorGray_50));
            seekbarSingle.setBarHighlightColor(getContext().getResources().getColor(R.color.colorBlue));
            seekbarSingle.setCornerRadius(cornerRadius);
            seekbarSingle.setDataType(dataType);
            seekbarSingle.setLeftThumbColor(getContext().getResources().getColor(R.color.colorBlue));
            seekbarSingle.setLeftThumbHighlightColor(getContext().getResources().getColor(R.color.colorBlueDark));
            seekbarSingle.setSteps(step);

            layout.addView(seekbarSingle, params);
        } else {
            seekbarRange = new SeekBarRange(getContext());
            seekbarRange.setMinValue(minValue);
            seekbarRange.setMaxValue(maxValue);
            seekbarRange.setBarColor(getContext().getResources().getColor(R.color.colorGray_50));
            seekbarRange.setBarHighlightColor(getContext().getResources().getColor(R.color.colorBlue));
            seekbarRange.setCornerRadius(cornerRadius);
            seekbarRange.setDataType(dataType);
            seekbarRange.setLeftThumbColor(getContext().getResources().getColor(R.color.colorBlue));
            seekbarRange.setLeftThumbHighlightColor(getContext().getResources().getColor(R.color.colorBlueDark));
            seekbarRange.setRightThumbColor(getContext().getResources().getColor(R.color.colorBlue));
            seekbarRange.setRightThumbHighlightColor(getContext().getResources().getColor(R.color.colorBlueDark));
            seekbarRange.setSteps(step);
            seekbarRange.setGap(gap);

            layout.addView(seekbarRange, params);
        }
    }

    public void setTextProgressSeekBar(String text) {
        textProgressSeekBar.setText(text);
    }

    public void setOnRangeSeekbarChangeListener(OnRangeSeekbarChangeListener onRangeSeekbarChangeListener) {
        if (seekbarRange == null)
            return;

        seekbarRange.setOnRangeSeekbarChangeListener(onRangeSeekbarChangeListener);
    }

    public void setOnSeekbarChangeListener(OnSeekbarChangeListener onSeekbarChangeListener) {
        if (seekbarSingle == null)
            return;

        seekbarSingle.setOnSeekbarChangeListener(onSeekbarChangeListener);
    }
}