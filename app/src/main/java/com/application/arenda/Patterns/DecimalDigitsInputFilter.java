package com.application.arenda.Patterns;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    Pattern pattern;

    public DecimalDigitsInputFilter() {
        pattern = Pattern.compile("^\\d*([.,][0-9]{0,1})?");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

        Matcher matcher = pattern.matcher(dest);
        if (!matcher.matches())
            return "";
        return null;
    }
}