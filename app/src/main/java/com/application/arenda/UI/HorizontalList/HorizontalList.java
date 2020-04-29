package com.application.arenda.UI.HorizontalList;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.Entities.Models.IModel;
import com.application.arenda.Entities.RecyclerView.BaseAdapter;
import com.application.arenda.Entities.RecyclerView.BaseViewHolder;
import com.application.arenda.Entities.RecyclerView.RVOnScrollListener;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HorizontalList extends FrameLayout {

    @BindView(R.id.textTitleList)
    TextView textTitleList;
    @BindView(R.id.rvHorizontList)
    RecyclerView rvHorizontList;

    private String title = "";

    public HorizontalList(Context context) {
        super(context);
        init(null, 0);
    }

    public HorizontalList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HorizontalList(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray attr = getContext().obtainStyledAttributes(
                attrs, R.styleable.HorizontalList, defStyle, 0);

        title = attr.getString(R.styleable.HorizontalList_titleSeeAll);

        inflate(getContext(), R.layout.horizontal_list, this);

        ButterKnife.bind(this);

        textTitleList.setText(title);

        attr.recycle();
    }

    public void initRV(LinearLayoutManager rvLayoutManager) {
        try {
            rvHorizontList.setLayoutManager(rvLayoutManager);

            rvHorizontList.setItemAnimator(new DefaultItemAnimator());
            rvHorizontList.setItemViewCacheSize(50);
            rvHorizontList.setDrawingCacheEnabled(true);
            rvHorizontList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            rvHorizontList.setHasFixedSize(true);

//        rvHorizontList.setOnFlingListener(new RVOnFlingListener(rvHorizontList));

        } catch (Throwable throwable) {
            Timber.e(throwable);
        }
    }

    public void initAdapter(BaseAdapter<? extends IModel, ? extends BaseViewHolder> adapter) {
        rvHorizontList.setAdapter(adapter);
    }

    public void setRvOnScrollListener(RVOnScrollListener rvOnScrollListener) {
        if (rvOnScrollListener != null) {
            rvHorizontList.addOnScrollListener(rvOnScrollListener);
        }
    }

    public void seeAllClickListener(View.OnClickListener listener) {
        textTitleList.setOnClickListener(listener);
    }

    public void setTitleSeeAll(String title) {
        textTitleList.setText(title);
    }

    public void clearTitle() {
        textTitleList.setText("");
    }
}