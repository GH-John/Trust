package com.application.trust.CustomComponents.Panels.SideBar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.CustomComponents.Panels.SideBar.Btn.CustomBtnAdd;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.InflateItemList;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.ItemListAdapter;
import com.application.trust.CustomComponents.Panels.SideBar.ItemList.PanelItemList;
import com.application.trust.R;

public class CustomSideBar extends ConstraintLayout implements ISideBar {
    private ImageView panelSideBar,
                      itemUserAccount,
                      backgroundItemList,
                      itemAddAnnouncement;
    private RecyclerView itemListView;
    private PanelItemList panelItemList;
    private CustomBtnAdd customBtnAdd;
    private Context context;

    private ConstraintLayout.LayoutParams layoutParams;
    int width;
    int height;


    public CustomSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateSideBar(context, attrs);
        styleItemSideBar(context);
        stylePanelItemList(getContext(), new PanelItemList(context,R.color.colorWhite,
                R.color.shadowItemList, 6f, 0f, 3f,
                new float[]{80f,80f, 0f,0f, 80f,80f, 0f,0f}));
        startListener(R.id.containerContentDisplay);
    }

    private void inflateSideBar(Context context, AttributeSet attrs){
        inflate(context, R.layout.side_bar, this);
        panelSideBar = findViewById(R.id.panelSideBar);
        backgroundItemList = findViewById(R.id.backgroundItemList);
        itemAddAnnouncement = findViewById(R.id.itemAddAnnouncement);
        itemUserAccount = findViewById(R.id.itemUserAccount);
        itemListView = findViewById(R.id.itemListView);
        customBtnAdd = findViewById(R.id.customBtnAdd);

        layoutParams = new ConstraintLayout.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        width = panelSideBar.getWidth();
        height = panelSideBar.getHeight();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        stylePanelSideBar(getContext(), new PanelSideBar(getContext(), R.color.colorWhite,
                R.color.shadowColor, 10f, 0f, 0f,
                new float[]{0f,0f, 80f,80f, 80f,80f, 0f,0f}));
    }

    @Override
    public void stylePanelSideBar(Context context, DrawPanel drawPanel) {
        panelSideBar.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void stylePanelItemList(Context context, DrawPanel drawPanel){
        panelItemList = (PanelItemList) drawPanel;
    }

    @SuppressLint("NewApi")
    @Override
    public void styleItemSideBar(Context context) {
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0,0, view.getWidth(), view.getHeight(), 100);
            }
        };
        itemUserAccount.setOutlineProvider(viewOutlineProvider);
        itemUserAccount.setClipToOutline(true);
        itemAddAnnouncement.setOutlineProvider(viewOutlineProvider);
        itemAddAnnouncement.setClipToOutline(true);
    }

    @Override
    public void setAdapterItemList(Context context, ItemListAdapter adapter){
        itemListView.setAdapter(adapter);
        itemListView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void startListener(int idContainerContent) {

        setAdapterItemList(context, new ItemListAdapter(R.layout.pattern_item_list,
                InflateItemList.getItemListData(panelItemList)));
        itemAddAnnouncement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"add click", Toast.LENGTH_LONG).show();
            }
        });
//        customBtnAdd.startListener(idContainerContent);
    }
}