package com.application.trust.CustomComponents.Panels.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.application.trust.CustomComponents.ContainerFragments.FragmentLink;
import com.application.trust.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.trust.CustomComponents.Panels.DrawPanel;
import com.application.trust.Patterns.Observable;
import com.application.trust.Patterns.Observer;
import com.application.trust.Patterns.ObserverManager;
import com.application.trust.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomBottomNavigation extends ConstraintLayout implements IBottomNavigation, Observable, FragmentLink {
    private ImageView panelBottomNavigation,
            itemUserAnnouncements,
            itemAllAnnouncements,
            itemAddAnnouncement,
            itemUserStatistics,
            itemUserProposals;

    private Context context;
    private Fragment containerContentDisplay;

    private List<View> listItem;
    private Map<Fragment, View> mapFragmentLinks;
    private ObserverManager<Observable, Observer> observerManager;
    private ComponentLinkManager<Fragment, View, FragmentLink> componentLinkManager;

    public CustomBottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflateBottomNavigation(context, attrs);
        createListItem();
        stylePanel(new PanelBottomNavigation(context,
                R.color.colorWhite, new float[8], R.color.shadowColor,
                10f, 0f, 0f));
        styleItems();
    }

    private void inflateBottomNavigation(Context context, AttributeSet attrs) {
        inflate(context, R.layout.bottom_navigation, this);
        itemUserAnnouncements = findViewById(R.id.itemUserAnnouncement);
        panelBottomNavigation = findViewById(R.id.panelBottomNavigation);
        itemAllAnnouncements = findViewById(R.id.itemAllAnnouncement);
        itemAddAnnouncement = findViewById(R.id.itemAddAnnouncement);
        itemUserStatistics = findViewById(R.id.itemUserStatistics);
        itemUserProposals = findViewById(R.id.itemUserProposals);

        listItem = new LinkedList<>();
        mapFragmentLinks = new HashMap<>();
    }

    private void createListItem() {
        listItem.add(itemUserAnnouncements);
        listItem.add(itemAllAnnouncements);
        listItem.add(itemAddAnnouncement);
        listItem.add(itemUserStatistics);
        listItem.add(itemUserProposals);
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public void stylePanel(DrawPanel drawPanel) {
        this.panelBottomNavigation.setImageDrawable((Drawable) drawPanel);
    }

    @Override
    public void hidePanel() {

    }

    @Override
    public void expandPanel() {

    }

    @Override
    public void itemListener(ComponentLinkManager componentLinkManager) {
        if (mapFragmentLinks.size() == 0) {
            createLinkWithFragment(componentLinkManager);
        } else {
            for (final Fragment fragment : mapFragmentLinks.keySet()) {
                mapFragmentLinks.get(fragment).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        notifyObservers(fragment);
                    }
                });
            }
        }
    }

    @SuppressLint({"NewApi", "ResourceAsColor"})
    @Override
    public void styleItems() {
        ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 90);
            }
        };

        itemAddAnnouncement.setOutlineProvider(viewOutlineProvider);
        itemAddAnnouncement.setClipToOutline(true);
    }

    @Override
    public void createLinkWithFragment(ComponentLinkManager componentLinkManager) {
        int i = 0;

        for (Fragment fragment : (Set<Fragment>) componentLinkManager.getCollectionFragmets(this)) {
            mapFragmentLinks.put(fragment, listItem.get(i));
            i++;
        }
        componentLinkManager.addItemLinks(this, mapFragmentLinks);

        itemListener(componentLinkManager);
    }

    @Override
    public void setManagers(ObserverManager observerManager, ComponentLinkManager componentLinkManager) {
        this.observerManager = observerManager;
        this.componentLinkManager = componentLinkManager;

        createLinkWithFragment(componentLinkManager);
    }

    @Override
    public void notifyObservers(Fragment fragment) {
        observerManager.notifyObservers(this, fragment);
    }
}