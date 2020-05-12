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
import com.application.arenda.UI.Components.CalendarView.DayController;
import com.application.arenda.UI.Components.CalendarView.models.KeyMonth;
import com.application.arenda.UI.Components.CalendarView.models.ModelDayItem;
import com.application.arenda.UI.Components.CalendarView.models.ModelEvent;
import com.application.arenda.UI.Components.CalendarView.models.ModelVisibleMonths;
import com.application.arenda.UI.Components.CalendarView.vh.DayVH.DayItemOnClickListener;
import com.application.arenda.UI.Components.CalendarView.vh.VisibleMonthVH;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

public class CalendarAdapter extends RecyclerView.Adapter<VisibleMonthVH> {
    private static final int DAYS_COUNT = 42;
    private static int currentPosition = 0;
    private static int currentScrollState = 0;
    private boolean isMonthCreateRun = false;

    private ModelVisibleMonths currentVisibleMonthItem;

    private LinearLayoutManager layoutManager;

    private Subject<Map<KeyMonth, List<ModelEvent>>> lastEvents = AsyncSubject.create();

    private SortedList<ModelVisibleMonths> sortedList = new SortedList<>(ModelVisibleMonths.class, new SortedList.Callback<ModelVisibleMonths>() {
        @Override
        public int compare(ModelVisibleMonths o1, ModelVisibleMonths o2) {
            return o1.compareTo(o2);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ModelVisibleMonths oldItem, ModelVisibleMonths newItem) {
            return oldItem.getMainDate().equals(newItem.getMainDate());
        }

        @Override
        public boolean areItemsTheSame(ModelVisibleMonths item1, ModelVisibleMonths item2) {
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
    private LocalDate dateMainMonth;

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
        dateMainMonth = LocalDate.now();

        dayItemOnClickListener = (date, month, controller) -> {
            if (selectDayStart) {

                lastSelectedDayStart = date;

                if (monthCallBack != null)
                    monthCallBack.getSelectedDateTime(lastSelectedDayStart, controller);

                month.getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
            }

            if (selectDayEnd) {

                lastSelectedDayEnd = date;

                if (monthCallBack != null)
                    monthCallBack.getSelectedDateTime(lastSelectedDayEnd, controller);

                month.getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
            }
        };

    }

    public io.reactivex.functions.Consumer<Map<KeyMonth, List<ModelEvent>>> getSubscriberOnLastEvents(ModelVisibleMonths visibleMonths) {
        return visibleMonths::updateAllEventsVisibleMonths;
    }

    public void goTodayMonth() {
        int pos = getPositionTodayMonth();

        recyclerView.smoothScrollToPosition(pos > -1 ? pos : 0);
    }

    public void resetSelectedDays() {
        lastSelectedDayStart = null;
        lastSelectedDayEnd = null;

        currentVisibleMonthItem.getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
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
        Observable.just(createMonth(dateMainMonth.minusMonths(1)), createMonth(dateMainMonth), createMonth(dateMainMonth.plusMonths(1)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    add(item);

                    if (getItemCount() == 3) {
                        currentVisibleMonthItem = item;
                        currentPosition = 1;
                        dateMainMonth = currentVisibleMonthItem.getMainDate();

                        recyclerView.scrollToPosition(1);
                    }
                }, Timber::e);
    }

    @SuppressLint("CheckResult")
    private void createMonth(boolean plus, long months) {
        if (!isMonthCreateRun) {
            Observable.just(createMonth(plus ? dateMainMonth.plusMonths(months) : dateMainMonth.minusMonths(months)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(this::add, Timber::e);
        }
    }

    @SuppressLint("CheckResult")
    private synchronized ModelVisibleMonths createMonth(LocalDate dateTimeInflater) {
        isMonthCreateRun = true;

        List<ModelDayItem> cells = new ArrayList<>();

        //<---------
        // determination of the day of the week and carriage with which the main month should begin
        dateTimeInflater = dateTimeInflater.minusDays(dateTimeInflater.getDayOfMonth() - 1);
        int monthBeginningCell = dateTimeInflater.getDayOfWeek().getValue() - 1;
        //--------->

        //subtract the days from the main month to fill the first carriage with the previous month
        dateTimeInflater = dateTimeInflater.minusDays(monthBeginningCell);


        LocalDate mainDate = dateTimeInflater.plusDays(DAYS_COUNT / 3);

        while (cells.size() < DAYS_COUNT) {

            ModelDayItem modelDayItem = new ModelDayItem();
            modelDayItem.setDate(dateTimeInflater);
//            modelDayItem.setEvents(events);
            modelDayItem.setEventStateDrawable(R.drawable.ic_dot_selected);

            cells.add(modelDayItem);

            dateTimeInflater = dateTimeInflater.plusDays(1);
        }

        ModelVisibleMonths monthItem = new ModelVisibleMonths(new KeyMonth(mainDate));


        monthItem.setItemList(cells);
        monthItem.setKeyFirstVisibleMonth(new KeyMonth(cells.get(0).getDate()));
        monthItem.setMainDate(mainDate);
        monthItem.setKeyLastVisibleMonth(new KeyMonth(cells.get(cells.size() - 1).getDate()));

        lastEvents.subscribe(getSubscriberOnLastEvents(monthItem), Timber::e);

        isMonthCreateRun = false;
        return monthItem;
    }

    private void add(ModelVisibleMonths item) {
        if (item != null)
            sortedList.add(item);
    }

    @SuppressLint("CheckResult")
    public void setEvents(List<ModelEvent> modelEvents) {
        Single.just(generateGroupedEvents(modelEvents))
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.io())
                .subscribe(aBoolean -> Timber.tag("Generated_group_events").d(String.valueOf(aBoolean)),
                        throwable -> {
                            if (monthCallBack != null)
                                monthCallBack.onError(throwable);
                        }
                );
    }

    private boolean generateGroupedEvents(List<ModelEvent> modelEvents) {
        try {
            KeyMonth keyDateStart;
            KeyMonth keyDateEnd;
            Map<KeyMonth, List<ModelEvent>> groupedEvents = new TreeMap<>();

            List<ModelEvent> group;

            for (int i = 0; i < modelEvents.size(); i++) {
                keyDateStart = new KeyMonth(modelEvents.get(i).getDateStart());
                keyDateEnd = new KeyMonth(modelEvents.get(i).getDateEnd());

                if (keyDateStart.equals(keyDateEnd)) {
                    group = groupedEvents.get(keyDateStart);

                    if (group != null) {
                        group.add(modelEvents.get(i));
                    } else {
                        group = new ArrayList<>();
                        group.add(modelEvents.get(i));

                        groupedEvents.put(keyDateStart, group);
                    }
                } else {

                    //<----------------add date start------------------>//

                    group = groupedEvents.get(keyDateStart);

                    if (group != null) {
                        group.add(modelEvents.get(i));
                    } else {
                        group = new ArrayList<>();
                        group.add(modelEvents.get(i));

                        groupedEvents.put(keyDateStart, group);
                    }

                    //<----------------add date end------------------>//

                    group = groupedEvents.get(keyDateEnd);

                    if (group != null) {
                        group.add(modelEvents.get(i));
                    } else {
                        group = new ArrayList<>();
                        group.add(modelEvents.get(i));

                        groupedEvents.put(keyDateEnd, group);
                    }
                }
            }

            lastEvents.onNext(groupedEvents);
            lastEvents.onComplete();

            return true;

        } catch (Throwable throwable) {
            if (monthCallBack != null)
                monthCallBack.onError(throwable);

            Timber.e(throwable);
        }

        return false;
    }

    public void clearEvents() {
        lastEvents.onNext(null);
        currentVisibleMonthItem.setAllEventsMonth(null);
        currentVisibleMonthItem.getMonthAdapter().updateAdapter();
    }

    private int getPositionTodayMonth() {
        int pos = -1;
        for (int i = 0; i < getItemCount(); i++) {

            if (sortedList.get(i).getKeyMainMonth().getKey().equals(KeyMonth.generateKey(today)))
                pos = i;
        }

        return pos;
    }

    private ModelVisibleMonths getItem(int position) {
        if (position >= 0 && position <= getItemCount())
            return sortedList.get(position);

        return null;
    }

    private int getPosition(ModelVisibleMonths item) {
        return sortedList.indexOf(item);
    }

    @NonNull
    @Override
    public VisibleMonthVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return VisibleMonthVH.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull VisibleMonthVH holder, int position) {
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

                if (currentScrollState == 1) {
                    if (dx > 0)
                        getItem(lastVisibleItem).getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
                    else {
                        getItem(firstVisibleItem).getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
                    }
                }
            }

            @Override
            public void onScrolledToEnd() {
                createMonth(true, 1);
            }

            @Override
            public void onScrollState(int state, int currentVisibleItems, int firstVisibleItem, int lastVisibleItem, int totalItems) {
                currentScrollState = state;
            }

            @Override
            public void onScrolledToCurrentPosition(int currentPos) {
                handleCurrentPosition(currentPos);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(100);
        recyclerView.addOnScrollListener(scrollListener);

        snapHelper.attachToRecyclerView(recyclerView);

        startInitMonth();
    }

    @SuppressLint("CheckResult")
    private void handleCurrentPosition(int currentPos) {
        currentVisibleMonthItem = getItem(currentPos);
        currentPosition = currentPos;
        dateMainMonth = currentVisibleMonthItem.getMainDate();

        getItem(currentPos).getMonthAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);

        if (monthCallBack != null)
            monthCallBack.currentMonth(currentVisibleMonthItem);
    }

    private void outputList() {
        Timber.tag("Items_start").d("<----------->");

        for (int i = 0; i < sortedList.size(); i++) {
            ModelVisibleMonths item = sortedList.get(i);
            Timber.tag("Item_" + i).d("Key - %s", item.getKeyMainMonth().getKey());
        }

        Timber.tag("Items_end").d("<----------->");
    }

    public interface MonthCallBack {
        void getSelectedDateTime(LocalDate dateTime, DayController controller);

        void currentMonth(ModelVisibleMonths monthItem);

        void onError(Throwable throwable);
    }
}