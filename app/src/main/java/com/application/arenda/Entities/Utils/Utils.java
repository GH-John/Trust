package com.application.arenda.Entities.Utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.application.arenda.R;
import com.jakewharton.threetenabp.AndroidThreeTen;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Action;

public class Utils {
    public static final Action<View> V_GONE = (view, index) -> {
        view.setVisibility(View.GONE);
    };
    public static final Action<View> V_VISIBLE = (view, index) -> {
        view.setVisibility(View.VISIBLE);
    };

    private static Pattern pattern;
    private static Matcher matcher;

    public static String getFormatingDate(Context context, String date) {
        AndroidThreeTen.init(context);
        LocalDate today = LocalDate.now();

        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (today.getDayOfYear() - dateTime.getDayOfYear() == 0)
            return context.getString(R.string.text_today) + ", " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        if (today.getDayOfYear() - dateTime.getDayOfYear() == 1)
            return context.getString(R.string.text_yesterday) + ", " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));


        return dateTime.getDayOfMonth() + " " + getMonthOfYear(context, dateTime) + ", " + dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public static String getDayOfWeek(Context context, LocalDateTime dateTime) {
        String dayOfWeek = "";

        switch (dateTime.getDayOfWeek().getValue()) {
            case 1: {
                dayOfWeek = context.getString(R.string.monday);
                break;
            }

            case 2: {
                dayOfWeek = context.getString(R.string.tuesday);
                break;
            }

            case 3: {
                dayOfWeek = context.getString(R.string.wednesday);
                break;
            }

            case 4: {
                dayOfWeek = context.getString(R.string.thursday);
                break;
            }

            case 5: {
                dayOfWeek = context.getString(R.string.friday);
                break;
            }

            case 6: {
                dayOfWeek = context.getString(R.string.saturday);
                break;
            }

            case 7: {
                dayOfWeek = context.getString(R.string.sunday);
                break;
            }
        }

        return dayOfWeek;
    }

    public static String getMonthOfYear(Context context, LocalDateTime dateTime) {
        String month = "";

        switch (dateTime.getMonth().getValue()) {
            case 1: {
                month = context.getString(R.string.january);
                break;
            }

            case 2: {
                month = context.getString(R.string.february);
                break;
            }

            case 3: {
                month = context.getString(R.string.march);
                break;
            }

            case 4: {
                month = context.getString(R.string.april);
                break;
            }

            case 5: {
                month = context.getString(R.string.may);
                break;
            }

            case 6: {
                month = context.getString(R.string.june);
                break;
            }

            case 7: {
                month = context.getString(R.string.july);
                break;
            }

            case 8: {
                month = context.getString(R.string.august);
                break;
            }

            case 9: {
                month = context.getString(R.string.september);
                break;
            }

            case 10: {
                month = context.getString(R.string.october);
                break;
            }

            case 11: {
                month = context.getString(R.string.november);
                break;
            }

            case 12: {
                month = context.getString(R.string.december);
                break;
            }
        }

        return month;
    }

    public static void showKeyboard(@NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void closeKeyboard(@NonNull Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void messageOutput(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
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
}