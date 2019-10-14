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
    private final int DISTANCES = 100;
    private int MIN_MARGIN;
    private int MAX_MARGIN;
    private float dX = 0f, mX = 0f, position = 0f, distances = 0f;

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
    public void stylePanelSideBar(Context context, DrawPanel drawPanel) {
        panelSideBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void stylePanelItemList(Context context, DrawPanel drawPanel) {
        panelItemList = (PanelItemList) drawPanel;
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
    public void setAdapterItemList(Context context, AdapterItemList adapter) {
        itemListView.setAdapter(adapter);
        itemListView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void startPosition() {
        int widthDisplayPx = convertDpToPixels(getDpWidthDisplay());
        MIN_MARGIN = widthDisplayPx - containerParams.width;
        MAX_MARGIN = widthDisplayPx;
        containerParams.rightMargin = MAX_MARGIN;
        blackoutSideBar.setAlpha(0f);
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

    @Override
    public void update(Fragment fragment) {
        if (fragment instanceof AdapterSideBar) {
            ((AdapterSideBar) fragment).touchListener().setOnTouchListener(new OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    action = event.getAction() & MotionEvent.ACTION_MASK;
                    position = containerParams.getMarginEnd();

                    if (action == MotionEvent.ACTION_DOWN) {
                        dX = event.getX();
                    }

                    if (action == MotionEvent.ACTION_MOVE) {
                        mX = event.getX();
                        distances = Math.round(mX - dX);

                        if (isSwipeLeft(dX, mX) && position != MAX_MARGIN && position < MAX_MARGIN) {
                            containerParams.rightMargin = (int) position + 10;
                            containerSideBar.setLayoutParams(containerParams);
//                            Toast.makeText(getContext(), "Left - " + position + " : " + MAX_MARGIN, Toast.LENGTH_LONG).show();
                        } else if (isSwipeRight(dX, mX) && position != MIN_MARGIN && position > MIN_MARGIN) {
                            containerParams.rightMargin = (int) position - 10;
                            containerSideBar.setLayoutParams(containerParams);
//                            Toast.makeText(getContext(), "Right - " + position + " : " + MIN_MARGIN, Toast.LENGTH_LONG).show();
                        }
                    }

                    if (action == MotionEvent.ACTION_UP) {
                        if (isSwipeLeft(dX, mX) && position < MAX_MARGIN) {
                            Toast.makeText(getContext(), "Left - " + position + " : " + MAX_MARGIN, Toast.LENGTH_LONG).show();
                            for (int i = 1; i != MAX_MARGIN; i++) {
                                containerParams.rightMargin = (int)position + i;
                                containerSideBar.setLayoutParams(containerParams);
                            }
                        } else if (isSwipeRight(dX, mX) && position > MIN_MARGIN) {
                            Toast.makeText(getContext(), "Right - " + position + " : " + MIN_MARGIN, Toast.LENGTH_LONG).show();
                            for (int i = 1; i != MIN_MARGIN; i++) {
                                containerParams.rightMargin = (int)position - i;
                                containerSideBar.setLayoutParams(containerParams);
                            }

                        }
                    }
                    return true;
                }
            });
        }
    }

    private boolean isSwipeLeft(float dX, float mX) {
        return dX > mX;
    }

    private boolean isSwipeRight(float dX, float mX) {
        return dX < mX;
    }
}