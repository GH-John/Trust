package com.application.arenda.UI.Components.CalendarView.adapters;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.application.arenda.Entities.RecyclerView.ScrollCallBack;
import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.CalendarScrollListener;
import com.application.arenda.UI.Components.CalendarView.models.KeyMonth;
import com.application.arenda.UI.Components.CalendarView.vh.DayVH.DayItemOnClickListener;
import com.application.arenda.UI.Components.CalendarView.models.ModelDayItem;
import com.application.arenda.UI.Components.CalendarView.models.ModelMonthItem;
import com.application.arenda.UI.Components.CalendarView.vh.MonthVH;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class CalendarAdapter extends RecyclerView.Adapter<MonthVH> {
    private static final int DAYS_COUNT = 42;
    private static int currentPosition = 0;
    private static int currentScrollState = 0;
    private ModelMonthItem visibleMonthItem;

    private boolean isMonthCreateRun = false;

    private LinearLayoutManager layoutManager;

    private SortedList<ModelMonthItem> sortedList = new SortedList<>(ModelMonthItem.class, new SortedList.Callback<ModelMonthItem>() {
        @Override
        public int compare(ModelMonthItem o1, ModelMonthItem o2) {
            return o1.compareTo(o2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ModelMonthItem oldItem, ModelMonthItem newItem) {
            return oldItem.getDate().equals(newItem.getDate());
        }

        @Override
        public boolean areItemsTheSame(ModelMonthItem item1, ModelMonthItem item2) {
            return item1.equals(item2);
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }
    });

    private CalendarScrollListener scrollListener;

    private LocalDate today;
    private LocalDate visibleMonth;

    private PagerSnapHelper snapHelper;

    private RecyclerView recyclerView;

    private MonthCallBack monthCallBack;

    private LocalDate lastSelectedDayStart, lastSelectedDayEnd;

    private DayItemOnClickListener dayItemOnClickListener;

    private boolean selectDayStart = true;
    private boolean selectDayEnd = false;

    @SuppressLint("CheckResult")
    public CalendarAdapter() {
        setHasStableIds(true);

        snapHelper = new PagerSnapHelper();

        today = LocalDate.now();
        visibleMonth = LocalDate.now();

        dayItemOnClickListener = (date, month) -> {
            if (selectDayStart) {

                lastSelectedDayStart = date;

                if (monthCallBack != null)
                    monthCallBack.getSelectedDateTime(lastSelectedDayStart);

                month.getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
            }

            if (selectDayEnd) {

                lastSelectedDayEnd = date;

                if (monthCallBack != null)
                    monthCallBack.getSelectedDateTime(lastSelectedDayEnd);

                month.getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
            }
        };
    }

    public void goTodayMonth() {
        int pos = getPositionTodayMonth();

        recyclerView.smoothScrollToPosition(pos > -1 ? pos : 0);
    }

    public void resetSelectedDays() {
        lastSelectedDayStart = null;
        lastSelectedDayEnd = null;

        visibleMonthItem.getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
    }

    public void setSelectDayStart(boolean b) {
        if (selectDayEnd)
            selectDayEnd = false;

        selectDayStart = b;
    }

    public void setSelectDayEnd(boolean b) {
        if (selectDayStart)
            selectDayStart = b;

        selectDayEnd = b;
    }

    public void setMonthCallBack(MonthCallBack callBack) {
        monthCallBack = callBack;
    }

    public void nextMonth() {
        if (currentPosition < getItemCount() && !isMonthCreateRun) {
            recyclerView.smoothScrollToPosition(++currentPosition);
        }
    }

    public void previousMonth() {
        if (currentPosition > 0 && !isMonthCreateRun)
            recyclerView.smoothScrollToPosition(--currentPosition);
    }

    @SuppressLint("CheckResult")
    private void startInitMonth() {
        Observable.just(createMonth(visibleMonth.minusMonths(1)), createMonth(visibleMonth), createMonth(visibleMonth.plusMonths(1)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    add(item);

                    if (getItemCount() == 2) {
                        visibleMonth = LocalDate.now();
                        visibleMonthItem = item;
                        currentPosition = 1;

                        recyclerView.scrollToPosition(1);
                    }
                }, Timber::e);
    }

    @SuppressLint("CheckResult")
    private void createMonth(boolean plus, long months) {
        if (!isMonthCreateRun) {
            Observable.just(createMonth(plus ? visibleMonth.plusMonths(months) : visibleMonth.minusMonths(months)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(this::add, Timber::e);
        }
    }

    private synchronized ModelMonthItem createMonth(LocalDate dateTimeInflater) {
        isMonthCreateRun = true;

        List<ModelDayItem> cells = new ArrayList<>();

        dateTimeInflater = dateTimeInflater.minusDays(dateTimeInflater.getDayOfMonth() - 1);

        int monthBeginningCell = dateTimeInflater.getDayOfWeek().getValue() - 1;

        dateTimeInflater = dateTimeInflater.minusDays(monthBeginningCell);

        LocalDate currentMonth = LocalDate.now();

        while (cells.size() < DAYS_COUNT) {
            ModelDayItem modelDayItem = new ModelDayItem();
            modelDayItem.setDate(dateTimeInflater);
//            modelDayItem.setEvents(events);
            modelDayItem.setEventStateDrawable(R.drawable.ic_dot_selected);

            cells.add(modelDayItem);

            if (cells.size() == DAYS_COUNT / 3) {
                currentMonth = modelDayItem.getDate();
            }

            dateTimeInflater = dateTimeInflater.plusDays(1);
        }

        ModelMonthItem monthItem = new ModelMonthItem(new KeyMonth(currentMonth));

        monthItem.setItemList(cells);
        monthItem.setDate(currentMonth);

        isMonthCreateRun = false;
        return monthItem;
    }

    private void add(ModelMonthItem item) {
        if (item != null)
            sortedList.add(item);
    }

    private int getPositionTodayMonth() {
        int pos = -1;
        for (int i = 0; i < getItemCount(); i++) {

            if (sortedList.get(i).getKey().getKey().equals(KeyMonth.generateKey(today)))
                pos = i;
        }

        return pos;
    }

    private ModelMonthItem getItem(int position) {
        if (position >= 0 && position <= getItemCount())
            return sortedList.get(position);

        return null;
    }

    private int getPosition(ModelMonthItem item) {
        return sortedList.indexOf(item);
    }

    @NonNull
    @Override
    public MonthVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MonthVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthVH holder, int position) {
        holder.onBind(sortedList.get(position), lastSelectedDayStart, lastSelectedDayEnd);

        holder.setDayItemClick(dayItemOnClickListener);
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    @Override
    public long getItemId(int position) {
        return sortedList.get(position).hashCode();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;
        this.recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false);

        scrollListener = new CalendarScrollListener(layoutManager);
        scrollListener.setScrollCallBack(new ScrollCallBack() {

            @Override
            public void onScrolledToStart() {
                createMonth(false, 1);
            }

            @Override
            public void onScrolled(int currentVisibleItems, int firstVisibleItem, int lastVisibleItem, int totalItems, int dx, int dy) {
                if (currentScrollState != 1 && firstVisibleItem < 2)
                    createMonth(false, 2);

//                if (currentScrollState == 1) {
//                    if (dx > 0)
//                        getItem(lastVisibleItem).getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
//                    else {
//                        getItem(firstVisibleItem).getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
//                    }
//                }
            }

            @Override
            public void onScrolledToEnd() {
                createMonth(true, 1);
            }

            @Override
            public void onScrollState(int state, int currentVisibleItems, int firstVisibleItem, int totalItems) {
                currentScrollState = state;
            }

            @Override
            public void currentPosition(int currentPos) {
                handleCurrentPosition(currentPos);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(30);
        recyclerView.addOnScrollListener(scrollListener);

        snapHelper.attachToRecyclerView(recyclerView);

        startInitMonth();
    }

    private void handleCurrentPosition(int currentPos) {
        visibleMonthItem = getItem(currentPos);
        currentPosition = currentPos;
        visibleMonth = visibleMonthItem.getDate();

        getItem(currentPos).getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);

        if (monthCallBack != null)
            monthCallBack.currentMonth(visibleMonthItem);
    }

    private void outputList() {
        Timber.tag("Items_start").d("<----------->");

        for (int i = 0; i < sortedList.size(); i++) {
            ModelMonthItem item = sortedList.get(i);
            Timber.tag("Item_" + i).d("Key - %s", item.getKey().getKey());
        }

        Timber.tag("Items_end").d("<----------->");
    }

    public interface MonthCallBack {
        void getSelectedDateTime(LocalDate dateTime);

        void currentMonth(ModelMonthItem monthItem);

        void onError(Throwable throwable);
    }
}