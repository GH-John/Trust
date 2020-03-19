package com.application.arenda.UI.Components.SideBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.arenda.R;
import com.application.arenda.UI.Components.ComponentManager;
import com.application.arenda.UI.Components.SideBar.ItemList.AdapterItemList;
import com.application.arenda.UI.Components.SideBar.ItemList.InflateItemList;
import com.application.arenda.UI.DisplayUtils;
import com.application.arenda.UI.DrawPanel;

import butterknife.ButterKnife;

public class CustomSideBar extends ConstraintLayout implements SideBar, ComponentManager.Observer {
    private ImageView panelSideBar,
            itemUserAccount,
            blackoutSideBar;

    private RecyclerView itemRecyclerView;
    private ConstraintLayout containerSideBar;
    private ConstraintLayout.LayoutParams containerParams;

    private PanelSideBar panelItemList;
    private DisplayMetrics displayMetrics;

    private int action;
    private int MIN_MARGIN;
    private int MAX_MARGIN;

    private float dX = 0.0f, dY = 0.0f, mX = 0.0f, mY = 0.0f,
            dragAngle = 0.0f, currentMargin = 0.0f, distances = 0.0f, blackoutAlpha = 0.0f;

    public CustomSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.sb_side_bar, this);

        ButterKnife.bind(this);

        initComponents();

        stylePanelItemList(getContext(), new PanelSideBar(context,
                R.color.colorWhite, R.color.shadowItemList,
                6f, 0f, 3f, 20f, 20f, 20f, 20f));

        initListeners();
    }

    private void initComponents() {
        displayMetrics = getResources().getDisplayMetrics();

        panelSideBar = findViewById(R.id.panelSideBar);
        itemRecyclerView = findViewById(R.id.itemRecyclerView);
        blackoutSideBar = findViewById(R.id.blackoutSideBar);
        itemUserAccount = findViewById(R.id.itemUserAccount);
        containerSideBar = findViewById(R.id.containerSideBar);

        containerParams = (ConstraintLayout.LayoutParams) containerSideBar.getLayoutParams();

        startPosition();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        stylePanelSideBar(getContext(), new PanelSideBar(getContext(),
                R.color.colorWhite, R.color.shadowColor,
                10f, 0f, 0f, 0f, 35f, 35f, 0f));
    }

    @Override
    public void initListeners() {
        setAdapterItemList(getContext(), new AdapterItemList(R.layout.sb_pattern_item_list,
                InflateItemList.getItemListData(panelItemList)));
    }

    @Override
    public void styleItemSideBar(Context context) {
    }

    @Override
    public void stylePanelSideBar(Context context, DrawPanel drawPanel) {
        panelSideBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void stylePanelItemList(Context context, DrawPanel drawPanel) {
        panelItemList = (PanelSideBar) drawPanel;
    }

    @Override
    public void setAdapterItemList(Context context, AdapterItemList adapter) {
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        itemRecyclerView.setAdapter(adapter);
    }

    @Override
    public void hide() {
        if (containerParams != null) {
            containerParams.rightMargin = MAX_MARGIN;
            containerSideBar.setLayoutParams(containerParams);
//        setAlphaBlackout(0.0f);
        }
    }

    @Override
    public void expand() {
        if (containerParams != null) {
            containerParams.rightMargin = MIN_MARGIN;
            containerSideBar.setLayoutParams(containerParams);
//        setAlphaBlackout(1.0f);
        }
    }

    @Override
    public void startPosition() {
        if (containerParams != null) {
            int widthDisplayPx = DisplayUtils.dpToPx((int) getDpWidthDisplay());
            MIN_MARGIN = widthDisplayPx - containerParams.width;
            MAX_MARGIN = widthDisplayPx;
            distances = (MAX_MARGIN - MIN_MARGIN) * 10 / 100;

            hide();
        }
    }

    private float getDpWidthDisplay() {
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    private float getDpHeightDisplay() {
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    @Override
    public void swipeListener(View view, MotionEvent event) {
        action = event.getAction();
        currentMargin = containerParams.getMarginEnd();

        if (action == MotionEvent.ACTION_DOWN) {
            currentMargin = containerParams.getMarginEnd();
            dX = event.getX();
            dY = event.getY();
        }

        if (action == MotionEvent.ACTION_MOVE) {
            mX = event.getX();
            mY = event.getY();
            dragAngle = getAndle(dX, dY, mX, mY);
            if (dragAngle <= 45f) {
                if (isSwipeLeft(dX, mX) && currentMargin < MAX_MARGIN) {
                    containerParams.rightMargin = (int) currentMargin + (int) (distances);
//                    setAlphaBlackout(blackoutAlpha -= 0.05f);
                } else if (currentMargin > MAX_MARGIN) {
                    containerParams.rightMargin = MAX_MARGIN;
//                    setAlphaBlackout(0.0f);
                }

                if (isSwipeRight(dX, mX) && currentMargin > MIN_MARGIN) {
                    containerParams.rightMargin = (int) currentMargin - (int) (distances);
//                    setAlphaBlackout(blackoutAlpha += 0.05f);
                } else if (currentMargin < MIN_MARGIN) {
                    containerParams.rightMargin = MIN_MARGIN;
//                    setAlphaBlackout(1.0f);
                }
                containerSideBar.setLayoutParams(containerParams);
            }
        }

        if (action == MotionEvent.ACTION_UP) {
            if (dragAngle <= 45f) {
                if (isSwipeLeft(dX, mX) && currentMargin < MAX_MARGIN) {
                    currentMargin = containerParams.getMarginEnd();
                    containerParams.rightMargin = MAX_MARGIN;
//                    setAlphaBlackout(0.0f);
                } else if (isSwipeRight(dX, mX) && currentMargin > MIN_MARGIN) {
                    currentMargin = containerParams.getMarginEnd();
                    containerParams.rightMargin = MIN_MARGIN;
//                    setAlphaBlackout(1.0f);
                }
                containerSideBar.setLayoutParams(containerParams);
            }
        }
    }

    private void setAlphaBlackout(float alpha) {
        blackoutSideBar.setAlpha(alpha);
    }

    private float getAndle(float x1, float y1, float x2, float y2) {
        return Math.abs((float) Math.atan((y2 - y1) / (x2 - x1))) * 100f;
    }

    private boolean isSwipeLeft(float dX, float mX) {
        return dX > mX;
    }

    private boolean isSwipeRight(float dX, float mX) {
        return dX < mX;
    }

    @Override
    public void update(@NonNull Object object) {
        hide();
        if (object instanceof AdapterSideBar) {
            ((AdapterSideBar) object).setSideBar(this);
        }
    }
}