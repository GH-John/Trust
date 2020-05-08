package com.application.arenda.UI.Components.CalendarView;

import android.os.Handler;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.DayVH.DayItemOnClickListener;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.HashMap;

import timber.log.Timber;

public class MonthAdapter extends BaseAdapter<ModelMonthItem, MonthVH> {
    private static final int DAYS_COUNT = 42;

    private LocalDateTime currentMonth;

    private PagerSnapHelper snapHelper;

    private HashMap<String, ModelMonthItem> monthStack = new HashMap<>();

    private ModelMonthItem monthFromStack;

    private RecyclerView recyclerView;

    private MonthCallBack monthCallBack;

    private Handler handler = new Handler();

    private Runnable nextMonthRunnable;
    private Runnable previousMonthRunnable;
    private Runnable initFirstMonths;

    private DayItemOnClickListener dayItemOnClickListener;

    public MonthAdapter() {
        snapHelper = new PagerSnapHelper();

        currentMonth = LocalDateTime.now();

        initRunnable();

        initFirstMonths();
    }

    private void initFirstMonths() {
        currentMonth = currentMonth.minusMonths(1);
        createMonth(currentMonth, true);

        currentMonth = currentMonth.plusDays(1);
        createMonth(currentMonth, true);

        currentMonth = currentMonth.plusDays(1);
        createMonth(currentMonth, true);
    }

    private void initRunnable() {

        nextMonthRunnable = () -> {
            try {
                currentMonth = currentMonth.plusMonths(1);
                recyclerView.smoothScrollToPosition(createMonth(currentMonth, true));

            } catch (Throwable throwable) {
                if (monthCallBack != null)
                    monthCallBack.onError(throwable);
            }
        };

        previousMonthRunnable = () -> {
            try {
                currentMonth = currentMonth.minusMonths(1);

                recyclerView.smoothScrollToPosition(createMonth(currentMonth, false));
            } catch (Throwable throwable) {
                if (monthCallBack != null)
                    monthCallBack.onError(throwable);
            }
        };
    }

    private String getKey(LocalDateTime dateTime) {
        String key = dateTime.getMonthValue() + "/" + dateTime.getYear();

        Timber.tag("KEY").d(key);

        return key;
    }

    public void setDayItemClickListener(DayItemOnClickListener listener) {
        this.dayItemOnClickListener = listener;
    }

    public void setMonthCallBack(MonthCallBack callBack) {
        monthCallBack = callBack;
    }

    public void nextMonth() {
        handler.post(nextMonthRunnable);
    }

    public void previousMonth() {
        handler.post(previousMonthRunnable);
    }

    private synchronized int createMonth(LocalDateTime dateTimeInflater, boolean addToEndCollection) {

        monthFromStack = monthStack.get(getKey(currentMonth));

        if (monthFromStack != null) {

            if (monthCallBack != null)
                monthCallBack.currentMonth(monthFromStack);

            int pos = getCollection().indexOf(monthFromStack);

            Timber.tag("FOUND_IN_POS").d(String.valueOf(pos));

            return pos;
        }

        Timber.tag("KEY_is").d("NOT_FOUND");

        ArrayList<ModelDayItem> cells = new ArrayList<>();

        dateTimeInflater = dateTimeInflater.minusDays(dateTimeInflater.getDayOfMonth() - 1);

        int monthBeginningCell = dateTimeInflater.getDayOfWeek().getValue() - 1;

        dateTimeInflater = dateTimeInflater.minusDays(monthBeginningCell);

        while (cells.size() < DAYS_COUNT) {
            ModelDayItem modelDayItem = new ModelDayItem();
            modelDayItem.setDate(LocalDateTime.of(dateTimeInflater.toLocalDate(), dateTimeInflater.toLocalTime()));
//            modelDayItem.setEvents(events);
            modelDayItem.setEventStateDrawable(R.drawable.ic_dot_selected);

            cells.add(modelDayItem);

            dateTimeInflater = dateTimeInflater.plusDays(1);
        }

        ModelMonthItem monthItem = new ModelMonthItem();

        monthItem.setItemList(cells);
        monthItem.setDateTime(currentMonth);

        Timber.tag("KEY_is").d("CREATED");
        monthStack.put(getKey(currentMonth), monthItem);

        if (monthCallBack != null)
            monthCallBack.currentMonth(monthItem);

        if (addToEndCollection) {
            addToCollection(monthItem);

            return getItemCount() - 1;
        } else {
            addToCollection(monthItem, 0);

            return 0;
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false));
        recyclerView.setItemViewCacheSize(30);

        recyclerView.scrollToPosition(1);

        snapHelper.attachToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public MonthVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MonthVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthVH holder, int position) {
        holder.onBind(getItem(position), position);

        holder.setDayItemClick(dayItemOnClickListener);
    }

    public interface MonthCallBack {
        void currentMonth(ModelMonthItem monthItem);

        void onError(Throwable throwable);
    }
}