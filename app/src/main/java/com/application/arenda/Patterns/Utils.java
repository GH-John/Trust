package com.application.arenda.Patterns;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.application.arenda.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static Pattern pattern;
    private static Matcher matcher;

    public static void showKeyboard(@NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void closeKeyboard(@NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static boolean fieldIsEmpty(@NonNull Context context, @NonNull EditText... fields) {
        int countEmpty = 0;
        if (fields.length > 0) {
            for (EditText field : fields) {
                if (field.getText().toString().isEmpty()) {
                    field.setError(context.getResources().getString(R.string.error_empty_field));
                    countEmpty++;
                }
            }
            return countEmpty == fields.length || countEmpty > 0;
        }
        return false;
    }

    public static boolean textIsAlphabet(@NonNull Context context, @NonNull EditText... fields) {
        int countIncorrectFields = 0;

        pattern = Pattern.compile("[\\W_0-9]+");
        if (fields.length > 0) {
            for (EditText field : fields) {
                matcher = pattern.matcher(field.getText().toString());
                if (matcher.find()) {
                    field.setError(context.getResources().getString(R.string.error_incorrect_chars));
                    countIncorrectFields++;
                }
            }
            return countIncorrectFields == 0;
        }
        return false;
    }

    public static boolean isEmail(@NonNull Context context, @NonNull EditText... fields) {
        int countIncorrectFields = 0;

        pattern = Pattern.compile(".+@.+\\..+");
        if (fields.length > 0) {
            for (EditText field : fields) {
                matcher = pattern.matcher(field.getText().toString());
                if (!matcher.find()) {
                    field.setError(context.getResources().getString(R.string.error_incorrect_email));
                    countIncorrectFields++;
                }
            }
            return countIncorrectFields == 0;
        }
        return false;
    }
}