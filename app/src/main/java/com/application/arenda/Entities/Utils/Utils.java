package com.application.arenda.Entities.Utils;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.application.arenda.R;
import com.google.android.material.snackbar.Snackbar;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class Utils {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Pattern pattern;
    private static Matcher matcher;

    public static Handler getHandler() {
        return handler;
    }

    public static void setPhoneMask(String pattern, TextView textView) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(pattern);
        FormatWatcher formatWatcher = new MaskFormatWatcher(
                MaskImpl.createTerminated(slots)
        );
        formatWatcher.installOn(textView);
    }

    public static void changeStatusBarColor(Activity activity) {
        Window window = activity.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorAccent));
    }

    public static String convertTimeTo_24H_or_12h(int hour, int minute, boolean convertTo24H) {
        if ((hour >= 0 && hour <= 23) && (minute >= 0 && minute <= 59)) {
            String format = LocalTime.of(hour, minute)
                    .format(DateTimeFormatter.ofPattern(
                            convertTo24H ?
                                    DatePattern.HH_mm.getPattern() :
                                    DatePattern.hh_mm.getPattern())
                    );

            if (!convertTo24H) {
                if (hour >= 12 && hour <= 23)
                    format = format + " PM";
                else if (hour >= 0 && hour < 12)
                    format = format + " AM";

                return format;
            }
        }
        return "";
    }

    public static String getFormatingDate(Context context, String date, DatePattern pattern) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd_HH_mm_ss.getPattern()));

        return dateTime.format(DateTimeFormatter.ofPattern(pattern.getPattern()));
    }

    public static String getFormatingDate(Context context, String date) {
        LocalDate today = LocalDate.now();

        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DatePattern.yyyy_MM_dd_HH_mm_ss.getPattern()));

        if (today.getDayOfYear() - dateTime.getDayOfYear() == 0)
            return context.getString(R.string.text_today) + ", " + dateTime.format(DateTimeFormatter.ofPattern(DatePattern.HH_mm.getPattern()));

        if (today.getDayOfYear() - dateTime.getDayOfYear() == 1)
            return context.getString(R.string.text_yesterday) + ", " + dateTime.format(DateTimeFormatter.ofPattern(DatePattern.HH_mm.getPattern()));


        return dateTime.getDayOfMonth() + " " + getMonthOfYear(context, dateTime.getMonth(), false) + ", " + dateTime.format(DateTimeFormatter.ofPattern(DatePattern.HH_mm.getPattern()));
    }

    public static String getDayOfWeek(Context context, LocalDateTime date, boolean isFull) {
        String dayOfWeek = "";

        switch (date.getDayOfWeek().getValue()) {
            case 1: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_monday);
                else
                    dayOfWeek = context.getString(R.string.short_monday);
                break;
            }

            case 2: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_tuesday);
                else
                    dayOfWeek = context.getString(R.string.short_tuesday);
                break;
            }

            case 3: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_wednesday);
                else
                    dayOfWeek = context.getString(R.string.short_wednesday);
                break;
            }

            case 4: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_thursday);
                else
                    dayOfWeek = context.getString(R.string.short_thursday);
                break;
            }

            case 5: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_friday);
                else
                    dayOfWeek = context.getString(R.string.short_friday);
                break;
            }

            case 6: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_saturday);
                else
                    dayOfWeek = context.getString(R.string.short_saturday);
                break;
            }

            case 7: {
                if (isFull)
                    dayOfWeek = context.getString(R.string.full_sunday);
                else
                    dayOfWeek = context.getString(R.string.short_sunday);
                break;
            }
        }

        return dayOfWeek;
    }

    public static String getMonthOfYear(Context context, Month month, boolean isFull) {
        String s = "";

        switch (month.getValue()) {
            case 1: {
                if (isFull)
                    s = context.getString(R.string.full_january);
                else
                    s = context.getString(R.string.short_january);
                break;
            }

            case 2: {
                if (isFull)
                    s = context.getString(R.string.full_february);
                else
                    s = context.getString(R.string.short_february);
                break;
            }

            case 3: {
                if (isFull)
                    s = context.getString(R.string.full_march);
                else
                    s = context.getString(R.string.short_march);
                break;
            }

            case 4: {
                if (isFull)
                    s = context.getString(R.string.full_april);
                else
                    s = context.getString(R.string.short_april);
                break;
            }

            case 5: {
                if (isFull)
                    s = context.getString(R.string.full_may);
                else
                    s = context.getString(R.string.short_may);
                break;
            }

            case 6: {
                if (isFull)
                    s = context.getString(R.string.full_june);
                else
                    s = context.getString(R.string.short_june);
                break;
            }

            case 7: {
                if (isFull)
                    s = context.getString(R.string.full_july);
                else
                    s = context.getString(R.string.short_july);
                break;
            }

            case 8: {
                if (isFull)
                    s = context.getString(R.string.full_august);
                else
                    s = context.getString(R.string.short_august);
                break;
            }

            case 9: {
                if (isFull)
                    s = context.getString(R.string.full_september);
                else
                    s = context.getString(R.string.short_september);
                break;
            }

            case 10: {
                if (isFull)
                    s = context.getString(R.string.full_october);
                else
                    s = context.getString(R.string.short_october);
                break;
            }

            case 11: {
                if (isFull)
                    s = context.getString(R.string.full_november);
                else
                    s = context.getString(R.string.short_november);
                break;
            }

            case 12: {
                if (isFull)
                    s = context.getString(R.string.full_december);
                else
                    s = context.getString(R.string.short_december);
                break;
            }
        }

        return s;
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
        handler.post(() -> Toast.makeText(context, str, Toast.LENGTH_SHORT).show());
    }

    public static boolean isWeakPassword(@NonNull Context context, @NonNull EditText editText) {
        if (editText != null)
            if (editText.getText().length() < 6) {
                editText.setError(context.getResources().getString(R.string.error_weak_password));
                return true;
            }

        return false;
    }

    public static boolean isEmail(@NonNull Context context, @NonNull EditText... fields) {
        int countIncorrectFields = 0;

        pattern = Pattern.compile(".+@.+\\..+");
        if (fields.length > 0) {
            for (EditText field : fields) {
                if (field != null) {
                    matcher = pattern.matcher(field.getText().toString());
                    if (!matcher.find()) {
                        field.setError(context.getResources().getString(R.string.error_incorrect_format_email));
                        countIncorrectFields++;
                    }
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
                if (field != null && field.getText().toString().isEmpty()) {
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
                if (field != null) {
                    matcher = pattern.matcher(field.getText().toString());
                    if (matcher.find()) {
                        field.setError(context.getResources().getString(R.string.error_incorrect_chars));
                        countIncorrectFields++;
                    }
                }
            }
            return countIncorrectFields == 0;
        }
        return false;
    }

    public static boolean isConfirmPassword(Context context, EditText fieldPassReg, EditText
            fieldConfirmPassReg) {
        if (fieldPassReg.getText().toString().equals(fieldConfirmPassReg.getText().toString())) {
            return true;
        } else {
            fieldConfirmPassReg.setError(context.getResources().getString(R.string.error_confirm_password));
        }

        return false;
    }

    public enum DatePattern {
        yyyy_MM_dd("yyyy-MM-dd"),
        dd_MM_yyyy("dd.MM.yyyy"),
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
        yyyy_MM_dd_hh_mm_ss("yyyy-MM-dd hh:mm:ss aa"),
        HH_mm("HH:mm"),
        hh_mm("hh:mm"),
        hh_mm_aa("hh:mm aa");

        private String pattern = "";

        DatePattern(String pattern) {
            this.pattern = pattern;
        }

        public String getPattern() {
            return pattern;
        }
    }
}