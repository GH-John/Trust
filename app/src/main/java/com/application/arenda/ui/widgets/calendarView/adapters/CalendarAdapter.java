package com.application.arenda.ui.widgets.calendarView.adapters;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.application.arenda.R;
import com.application.arenda.entities.recyclerView.ScrollCallBack;
import com.application.arenda.ui.widgets.calendarView.CalendarLayoutManager;
import com.application.arenda.ui.widgets.calendarView.CalendarScrollListener;
import com.application.arenda.ui.widgets.calendarView.DayController;
import com.application.arenda.ui.widgets.calendarView.models.KeyMonth;
import com.application.arenda.ui.widgets.calendarView.models.ModelDayItem;
import com.application.arenda.ui.widgets.calendarView.models.ModelEvent;
import com.application.arenda.ui.widgets.calendarView.models.ModelVisibleMonths;
import com.application.arenda.ui.widgets.calendarView.vh.DayVH.DayItemOnClickListener;
import com.application.arenda.ui.widgets.calendarView.vh.VisibleMonthVH;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

public class CalendarAdapter extends RecyclerView.Adapter<VisibleMonthVH> {
    private static final int DAYS_COUNT = 42;
    private static int currentPosition = 0;
    private static int currentScrollState = 0;
    private boolean isMonthCreateRun = false;

    private ModelVisibleMonths currentMainItem;

    private CalendarLayoutManager layoutManager;

    private Subject<Map<KeyMonth, List<ModelEvent>>> lastEvents = AsyncSubject.create();
    private Map<KeyMonth, List<ModelEvent>> allEvents = new TreeMap<>();

    private SortedList<ModelVisibleMonths> sortedList = new SortedList<>(ModelVisibleMonths.class, new SortedList.Callback<ModelVisibleMonths>() {
        @Override
        public int compare(ModelVisibleMonths o1, ModelVisibleMonths o2) {
            return o1.compareTo(o2);
        }

        @Override
        public void onChanged(int position, int count) {
            Timber.tag("OnChange").d("Pos - " + position + " Count - " + count);

            notifyItemRangeChanged(position, count);
        }

        @Override
        public boolean areContentsTheSame(ModelVisibleMonths oldItem, ModelVisibleMonths newItem) {
            return oldItem.getDayAdapter().equals(newItem.getDayAdapter());
        }

        @Override
        public boolean areItemsTheSame(ModelVisibleMonths item1, ModelVisibleMonths item2) {
            return item1.equals(item2);
        }

        @Override
        public void onInserted(int position, int count) {
            Timber.tag("OnInserted").d("Pos - " + position + " Count - " + count);

            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            Timber.tag("OnRemoved").d("Pos - " + position + " Count - " + count);

            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            Timber.tag("OnMoved").d("FromPos - " + fromPosition + " ToPos - " + toPosition);

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

        lastEvents.subscribe(getSubscriberReplaceEvents());

        dayItemOnClickListener = (dayItem, month, controller) -> {
            if (isSelectDayStart()) {
                lastSelectedDayStart = dayItem.getDate();
            }

            if (isSelectDayEnd()) {
                lastSelectedDayEnd = dayItem.getDate();
            }

            if (monthCallBack != null)
                monthCallBack.getSelectedDate(dayItem.getDate(), dayItem.getEvents(), controller);

            month.getDayAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
        };
    }

    @SuppressLint("CheckResult")
    public Single<List<ModelEvent>> getEventsInclusiveBetween(LocalDate dateStart, LocalDate dateEnd) {
        return Observable.fromIterable(allEvents.entrySet())
                .subscribeOn(Schedulers.io())
                .map(Map.Entry::getValue)
                .flatMap(Observable::fromIterable)
                .filter(event -> event.getDateStart().equals(dateStart) || event.getDateStart().equals(dateEnd) ||
                        event.getDateEnd().equals(dateStart) || event.getDateEnd().equals(dateEnd) ||
                        event.getDateStart().isAfter(dateStart) || event.getDateStart().isBefore(dateEnd) ||
                        event.getDateEnd().isAfter(dateStart) || event.getDateEnd().isBefore(dateEnd))
                .toList();
    }

    public Consumer<Map<KeyMonth, List<ModelEvent>>> getMonthSubscriberOnLastEvents(ModelVisibleMonths visibleMonths) {
        return visibleMonths::replaceAllEventsVisibleMonths;
    }

    public Consumer<Map<KeyMonth, List<ModelEvent>>> getSubscriberReplaceEvents() {
        return grouped -> {
            allEvents.clear();
            allEvents.putAll(grouped);
        };
    }

    public void goTodayMonth() {
        int pos = getPositionTodayMonth();

        recyclerView.smoothScrollToPosition(pos > -1 ? pos : 0);
    }

    public void resetSelectedDays() {
        lastSelectedDayStart = null;
        lastSelectedDayEnd = null;

        getItem(currentPosition).getDayAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
    }

    public boolean isSelectDayStart() {
        return selectDayStart;
    }

    public void setSelectDayStart(boolean b) {
        if (selectDayEnd)
            selectDayEnd = false;

        selectDayStart = b;
    }

    public boolean isSelectDayEnd() {
        return selectDayEnd;
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
        Observable.just(createMonth(dateMainMonth.minusMonths(2)),
                createMonth(dateMainMonth.minusMonths(1)),
                createMonth(dateMainMonth),
                createMonth(dateMainMonth.plusMonths(1)),
                createMonth(dateMainMonth.plusMonths(2)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    add(item);

                    if (getItemCount() - 1 == 4) {
                        currentMainItem = item;
                        currentPosition = 2;
                        dateMainMonth = currentMainItem.getMainDate();

                        Timber.tag("CurrentMonth_create").d(currentMainItem.toString());

                        recyclerView.scrollToPosition(currentPosition);
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
            modelDayItem.setEventStateDrawable(R.drawable.ic_dot_selected);

            cells.add(modelDayItem);

            dateTimeInflater = dateTimeInflater.plusDays(1);
        }

        ModelVisibleMonths monthItem = new ModelVisibleMonths(new KeyMonth(mainDate));

        monthItem.setItemList(cells);
        monthItem.setKeyFirstVisibleMonth(new KeyMonth(cells.get(0).getDate()));
        monthItem.setMainDate(mainDate);
        monthItem.setKeyLastVisibleMonth(new KeyMonth(cells.get(cells.size() - 1).getDate()));

        lastEvents.subscribe(getMonthSubscriberOnLastEvents(monthItem), Timber::e);

        isMonthCreateRun = false;
        return monthItem;
    }

    private void add(ModelVisibleMonths item) {
        if (item != null)
            sortedList.add(item);
    }

    @SuppressLint("CheckResult")
    public void replaceEvents(List<ModelEvent> modelEvents) {
        Single.just(generateGroupedEvents(modelEvents))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        lastEvents.onNext(new TreeMap<>());
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

        layoutManager = new CalendarLayoutManager(recyclerView.getContext(), RecyclerView.HORIZONTAL, false);

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
                else if (getItemCount() - 2 == lastVisibleItem - 1)
                    createMonth(true, 2);

                if (currentScrollState == 1) {
                    if (dx > 0)
                        getItem(lastVisibleItem).getDayAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
                    else {
                        getItem(firstVisibleItem).getDayAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);
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
                currentMainItem = getItem(currentPos);
                currentPosition = currentPos;
                dateMainMonth = currentMainItem.getMainDate();

                Timber.tag("CurPos").d(currentPos + " date - " + dateMainMonth.toString());

                getItem(currentPos).getDayAdapter().updateAdapter(lastSelectedDayStart, lastSelectedDayEnd);

                if (monthCallBack != null)
                    monthCallBack.currentMonth(currentMainItem);
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.addOnScrollListener(scrollListener);

        snapHelper.attachToRecyclerView(recyclerView);

        startInitMonth();
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
        void getSelectedDate(LocalDate dateTime, List<ModelEvent> events, DayController controller);

        void currentMonth(ModelVisibleMonths monthItem);

        void onError(Throwable throwable);
    }
}