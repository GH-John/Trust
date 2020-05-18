package com.application.arenda.ui.widgets.calendarView.models;

import androidx.annotation.Nullable;

import org.threeten.bp.LocalDate;

import java.util.Objects;

public class KeyMonth implements Comparable<KeyMonth> {
    private String key;
    private int monthValue;
    private int year;

    public KeyMonth() {

    }

    public KeyMonth(LocalDate date) {
        createKey(date);
    }

    public static String generateKey(LocalDate date) {
        if (date == null)
            return null;

        return date.getMonthValue() + "/" + date.getYear();
    }

    public String createKey(LocalDate date) {
        if (date != null) {
            monthValue = date.getMonthValue();
            year = date.getYear();

            key = monthValue + "/" + year;
        }
        return key;
    }

    public int getMonthValue() {
        return monthValue;
    }

    public int getYear() {
        return year;
    }

    public String getKey() {
        return key;
    }

    @Override
    public int compareTo(KeyMonth k) {
        if (key.equals(k.getKey()))
            return 0;
        else if (monthValue > k.getMonthValue() && year == k.getYear())
            return 1;
        else if (monthValue < k.getMonthValue() && year == k.getYear())
            return -1;

        else if (monthValue > k.getMonthValue() && year > k.getYear())
            return 1;
        else if (monthValue < k.getMonthValue() && year > k.getYear())
            return 1;
        else if (monthValue == k.getMonthValue() && year > k.getYear())
            return 1;

        else if (monthValue > k.getMonthValue() && year < k.getYear())
            return -1;
        else if (monthValue < k.getMonthValue() && year < k.getYear())
            return -1;
        else if (monthValue == k.getMonthValue() && year < k.getYear())
            return -1;

        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this)
            return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        KeyMonth item = (KeyMonth) obj;

        return key.equals(item.getKey());
    }
}