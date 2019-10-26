package com.application.trust.CustomComponents.Panels.SideBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.AdapterItemList;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.InflateItemList;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.PanelItemList;
import com.application.trust.Patterns.Observer;
import com.application.trust.R;

public class CustomSideBar extends ConstraintLayout implements ISideBar, Observer {
    private ImageView panelSideBar,
            itemUserAccount,
            blackoutSideBar,
            itemAddAnnouncement;
    private ConstraintLayout containerSideBar;
    private ConstraintLayout.LayoutParams containerParams;

    private Context context;
    private int width, height;
    private RecyclerView itemListView;
    private PanelItemList panelItemList;
    private DisplayMetrics displayMetrics;

    private int action;
    private int MIN_MARGIN;
    private int MAX_MARGIN;

    private float dX = 0.0f, dY = 0.0f, mX = 0.0f, mY = 0.0f,
            dragAngle = 0.0f, currentMargin = 0.0f, distances = 0.0f, blackoutAlpha = 0.0f;

    public CustomSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateSideBar(context, attrs);
        styleItemSideBar(context);
        stylePanelItemList(getContext(), new PanelItemList(context,
                R.color.colorWhite, R.color.shadowItemList,
                6f, 0f, 3f,
                new float[]{80f, 80f, 0f, 0f, 80f, 80f, 0f, 0f}));
        initializeListeners();
        startPosition();
    }

    private void inflateSideBar(Context context, AttributeSet attrs) {
        inflate(context, R.layout.side_bar, this);
        panelSideBar = findViewById(R.id.panelSideBar);
        itemListView = findViewById(R.id.itemListView);
        blackoutSideBar = findViewById(R.id.blackoutSideBar);
        itemUserAccount = findViewById(R.id.itemUserAccount);
        containerSideBar = findViewById(R.id.containerSideBar);
        itemAddAnnouncement = findViewById(R.id.itemAddAnnouncement);

        width = panelSideBar.getWidth();
        height = panelSideBar.getHeight();

        displayMetrics = context.getResources().getDisplayMetrics();
        containerParams = (ConstraintLayout.LayoutParams) containerSideBar.getLayoutParams();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        stylePanelSideBar(getContext(), new PanelSideBar(getContext(),
                R.color.colorWhite, R.color.shadowColor,
                10f, 0f, 0f,
                new float[]{0f, 0f, 80f, 80f, 80f, 80f, 0f, 0f}));
    }

    @Override
    public void initializeListeners() {
        setAdapterItemList(context, new AdapterItemList(R.layout.sb_pattern_item_list,
                InflateItemList.getItemListData(panelItemList)));
        itemAddAnnouncement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "add click", Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("NewApi")
    @Override
    public void styleItemSideBar(Context context) {
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 100);
            }
        };
        itemUserAccount.setOutlineProvider(viewOutlineProvider);
        itemUserAccount.setClipToOutline(true);
        itemAddAnnouncement.setOutlineProvider(viewOutlineProvider);
        itemAddAnnouncement.setClipToOutline(true);
    }

    @Override
    public void stylePanelSideBar(Context context, DrawPanel drawPanel) {
        panelSideBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void stylePanelItemList(Context context, DrawPanel drawPanel) {
        panelItemList = (PanelItemList) drawPanel;
    }

    @Override
    public void setAdapterItemList(Context context, AdapterItemList adapter) {
        itemListView.setAdapter(adapter);
        itemListView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void hide() {
        containerParams.rightMargin = MAX_MARGIN;
        containerSideBar.setLayoutParams(containerParams);
//        setAlphaBlackout(0.0f);
    }

    @Override
    public void expand() {
        containerParams.rightMargin = MIN_MARGIN;
        containerSideBar.setLayoutParams(containerParams);
//        setAlphaBlackout(1.0f);
    }

    @Override
    public void startPosition() {
        int widthDisplayPx = convertDpToPixels(getDpWidthDisplay());
        MIN_MARGIN = widthDisplayPx - containerParams.width;
        MAX_MARGIN = widthDisplayPx;
        distances = (MAX_MARGIN - MIN_MARGIN) * 10 / 100;
        hide();
    }

    private float getDpWidthDisplay() {
        return displayMetrics.widthPixels / displayMetrics.density;
    }

    private float getDpHeightDisplay() {
        return displayMetrics.heightPixels / displayMetrics.density;
    }

    public int convertDpToPixels(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
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
    public void update(Fragment fragment) {
        hide();
        if (fragment instanceof AdapterSideBar) {
            ((AdapterSideBar) fragment).setCustomSideBar(this);
        }
    }
}