package com.application.trust.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.trust.R;
import com.application.trust.StructureProject.Panels.ActionBar.ActionBarView;
import com.application.trust.StructureProject.Panels.BottomNavigation.BottomNavigationView;
import com.application.trust.StructureProject.Panels.SideBar.ItemList.ItemListAdapter;
import com.application.trust.StructureProject.Panels.SideBar.ItemList.InflateItemList;
import com.application.trust.StructureProject.Panels.SideBar.ItemList.PanelItemList;

public class ActivityMain extends AppCompatActivity {

    private BottomNavigationView customBottomNavigation;
    private ActionBarView customActionBar;
    private Fragment containerContentDisplay;
    private RecyclerView itemListView;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customBottomNavigation = findViewById(R.id.customBottomNavigation);
        customActionBar = findViewById(R.id.customActionBar);
        itemListView = findViewById(R.id.itemListView);

        customBottomNavigation.startListenerBottomNavigation(this, R.id.containerContentDisplay);
        customActionBar.startListenerActionBar(this, R.id.containerContentDisplay);

        PanelItemList panelItemList = new PanelItemList(this,R.color.colorWhite, new float[8],
                R.color.shadowColor, 5f, 0f, 2f);

        itemListView.setAdapter(new ItemListAdapter(R.layout.pattern_item_list, InflateItemList.getitemListData(panelItemList)));
        itemListView.setLayoutManager(new LinearLayoutManager(this));
    }
}