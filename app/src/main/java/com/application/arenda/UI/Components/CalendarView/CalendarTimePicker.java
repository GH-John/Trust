package com.application.arenda.UI.Components.CalendarView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarTimePicker extends Dialog {
    @BindView(R.id.calendarTimePicker)
    TimePicker timePicker;

    @BindView(R.id.calendarBtnTimeSelect)
    Button btnTimeSelect;

    @BindView(R.id.calendarPickerMessageError)
    TextView pickerError;

    private View.OnClickListener listener;

    public CalendarTimePicker(@NonNull Context context) {
        super(context);
        setContentView(R.layout.calendar_time_picker);

        ButterKnife.bind(this);
        initTimePickerDialog();
    }

    private void initTimePickerDialog() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        toggleModeOnOf(false);

        btnTimeSelect.setOnClickListener(v -> {
            if (listener != null)
                listener.onClick(v);

            dismiss();
        });
    }

    @Override
    public void onStart() {
        timePicker.setIs24HourView(DateFormat.is24HourFormat(getContext()));

        super.onStart();
    }

    public void setMessage(String message, @ColorInt int color) {
        pickerError.setText(message);
        pickerError.setTextColor(color);
    }

    public void clearMessage() {
        pickerError.setText("");
    }

    public void toggleModeOnOf(boolean isOn) {
        timePicker.findViewById(timePicker.getResources().getSystem().getIdentifier("toggle_mode", "id", "android")).setVisibility(isOn ? View.VISIBLE : View.GONE);
    }

    public int getHour() {
        return timePicker.getHour();
    }

    public void setHour(int hour) {
        timePicker.setHour(hour);
    }

    public int getMinute() {
        return timePicker.getMinute();
    }

    public void setMinute(int minute) {
        timePicker.setHour(minute);
    }

    public void setOnTimeChangeListener(TimePicker.OnTimeChangedListener timeChangeListener) {
        if (timeChangeListener != null)
            timePicker.setOnTimeChangedListener(timeChangeListener);
    }

    public void setBtnTimeSelectOnClick(View.OnClickListener listener) {
        this.listener = listener;
    }
}
