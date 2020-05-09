package com.application.arenda.UI.Components.CalendarView;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.application.arenda.Entities.RecyclerView.ScrollCallBack;
import com.application.arenda.R;
import com.application.arenda.UI.Components.CalendarView.DayVH.DayItemOnClickListener;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MonthAdapter extends RecyclerView.Adapter<MonthVH> {
    private static final int DAYS_COUNT = 42;
    private static int currentPosition = 0;
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
            return oldItem.getDateTime().equals(newItem.getDateTime());
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

    private LocalDateTime currentMonth;

    private PagerSnapHelper snapHelper;

    private RecyclerView recyclerView;

    private MonthCallBack monthCallBack;

    private DayItemOnClickListener dayItemOnClickListener;

    @SuppressLint("CheckResult")
    public MonthAdapter() {
        setHasStableIds(true);

        snapHelper = new PagerSnapHelper();

        currentMonth = LocalDateTime.now();
    }

    public void setDayItemClickListener(DayItemOnClickListener listener) {
        this.dayItemOnClickListener = listener;
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
        Observable.just(createMonth(currentMonth.minusMonths(1)), createMonth(currentMonth), createMonth(currentMonth.plusMonths(1)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    add(item);

                    Timber.tag("Init_month_return").d(item.getKey().getKey());

                    if (getItemCount() == 2) {
                        currentMonth = LocalDateTime.now();
                        recyclerView.scrollToPosition(1);

                        outputList();
                    }
                }, Timber::e);
    }

    @SuppressLint("CheckResult")
    private void createMonth(boolean plus, long months) {
        if (!isMonthCreateRun) {
            Observable.just(createMonth(plus ? currentMonth.plusMonths(months) : currentMonth.minusMonths(months)))
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(this::add, Timber::e);
        }
    }

    private synchronized ModelMonthItem createMonth(LocalDateTime dateTimeInflater) {
        Timber.tag("StartCreateMonth").d(dateTimeInflater.getMonthValue() + "/" + dateTimeInflater.getYear());

        isMonthCreateRun = true;

        List<ModelDayItem> cells = new ArrayList<>();

        dateTimeInflater = dateTimeInflater.minusDays(dateTimeInflater.getDayOfMonth() - 1);

        int monthBeginningCell = dateTimeInflater.getDayOfWeek().getValue() - 1;

        dateTimeInflater = dateTimeInflater.minusDays(monthBeginningCell);

        LocalDateTime currentMonth = LocalDateTime.now();

        while (cells.size() < DAYS_COUNT) {
            ModelDayItem modelDayItem = new ModelDayItem();
            modelDayItem.setDate(LocalDateTime.of(dateTimeInflater.toLocalDate(), dateTimeInflater.toLocalTime()));
//            modelDayItem.setEvents(events);
            modelDayItem.setEventStateDrawable(R.drawable.ic_dot_selected);

            cells.add(modelDayItem);

            if (cells.size() == DAYS_COUNT / 3) {
                currentMonth = modelDayItem.getDate();
            }

            dateTimeInflater = dateTimeInflater.plusDays(1);
        }

        ModelMonthItem monthItem = new ModelMonthItem(new KeyMonth(currentMonth.toLocalDate()));

        monthItem.setItemList(cells);
        monthItem.setDateTime(currentMonth);

        isMonthCreateRun = false;

        Timber.tag("EndCreateMonth").d("end");

        return monthItem;
    }

    private void add(ModelMonthItem item) {
        if (item != null)
            sortedList.add(item);
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
        holder.onBind(sortedList.get(position), position);

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
            private int currentScrollState = 0;

            @Override
            public void onScrolledToStart() {
                Timber.tag("ScrolledTo_").d("start");
                createMonth(false, 1);
            }

            @Override
            public void onScrolled(int currentVisibleItems, int firstVisibleItem, int totalItems) {
                Timber.tag("onScrolled").d("State - " + currentScrollState + " item" + firstVisibleItem);
                if (currentScrollState != 1 && firstVisibleItem < 2) {
                    createMonth(false, 2);
                }
            }

            @Override
            public void onScrolledToEnd() {
                Timber.tag("ScrolledTo_").d("end");
                createMonth(true, 1);
            }

            @Override
            public void onScrollState(int state, int currentVisibleItems, int firstVisibleItem, int totalItems) {
                currentScrollState = state;

                switch (state) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Timber.tag("ScrollState").d("SCROLL_STATE_IDLE");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Timber.tag("ScrollState").d("SCROLL_STATE_DRAGGING");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Timber.tag("ScrollState").d("SCROLL_STATE_SETTLING");
                        break;
                }
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
        final ModelMonthItem item = getItem(currentPos);
        currentPosition = currentPos;
        currentMonth = item.getDateTime();

        Timber.tag("CurrentPosition").d(String.valueOf(currentPosition));
        Timber.tag("HandleCurrentMonth").d(currentMonth.getMonthValue() + "/" + currentMonth.getYear());

        if (monthCallBack != null)
            monthCallBack.currentMonth(item);
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

        void currentMonth(ModelMonthItem monthItem);

        void onError(Throwable throwable);
    }
}