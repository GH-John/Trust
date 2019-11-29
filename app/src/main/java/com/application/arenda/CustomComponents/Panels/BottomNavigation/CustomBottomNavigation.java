package com.application.arenda.CustomComponents.Panels.BottomNavigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.application.arenda.CustomComponents.ContainerFragments.AdapterLink;
import com.application.arenda.CustomComponents.ContainerFragments.ComponentLinkManager;
import com.application.arenda.CustomComponents.DrawPanel;
import com.application.arenda.Patterns.AdapterManager;
import com.application.arenda.Patterns.Observable;
import com.application.arenda.Patterns.Observer;
import com.application.arenda.Patterns.ObserverManager;
import com.application.arenda.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomBottomNavigation extends ConstraintLayout implements BottomNavigation, Observable, AdapterLink, AdapterManager {
    private ImageView
            itemUserAnnouncements,
            itemAllAnnouncements,
            itemAddAnnouncement,
            itemUserStatistics,
            itemUserProposals,
            leftPanelBN,
            rightPanelBN,
            itemExpand;

    private Group groupHide;

    private Context context;
    private Fragment containerContentDisplay;

    private List<View> listItem;
    private Map<Fragment, View> mapFragmentLinks;
    private ObserverManager<Observable, Observer> observerManager;
    private ComponentLinkManager<Fragment, View, AdapterLink> componentLinkManager;

    public CustomBottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializationComponents(context, attrs);
        createListItem();
        stylePanel(new PanelBottomNavigation(context,
                R.color.colorWhite, new float[8], R.color.shadowColor,
                10f, 0f, 0f), leftPanelBN);

        styleItems();
    }

    private void initializationComponents(Context context, AttributeSet attrs) {
        inflate(context, R.layout.bn_bottom_navigation, this);
        itemUserAnnouncements = findViewById(R.id.itemUserAnnouncement);
        leftPanelBN = findViewById(R.id.leftPanelBN);
        rightPanelBN = findViewById(R.id.rightPanelBN);
        itemAllAnnouncements = findViewById(R.id.itemAllAnnouncement);
        itemAddAnnouncement = findViewById(R.id.itemAddAnnouncement);
        itemUserStatistics = findViewById(R.id.itemUserStatistics);
        itemUserProposals = findViewById(R.id.itemUserProposals);
        itemExpand = findViewById(R.id.itemExpand);

        groupHide = findViewById(R.id.groupHideBottomNavigation);

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

    @SuppressLint({"ResourceAsColor"})
    @Override
    public void stylePanel(DrawPanel drawPanel, View view) {
        ((ImageView)view).setImageDrawable((Drawable) drawPanel);
        rightPanelBN.setImageDrawable(((ImageView)view).getDrawable());
        rightPanelBN.setRotation(180f);
    }

    @Override
    public void hidePanel() {
        groupVisible(false);
        itemExpand.setVisibility(VISIBLE);
    }

    @Override
    public void expandPanel() {
        groupVisible(true);
        itemExpand.setVisibility(INVISIBLE);
    }

    private void groupVisible(final boolean b) {
        groupHide.post(new Runnable() {
            @Override
            public void run() {
                for (int value : groupHide.getReferencedIds()) {
                    if (!b)
                        findViewById(value).setVisibility(View.INVISIBLE);
                    else
                        findViewById(value).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void itemListener(ComponentLinkManager componentLinkManager) {
        if (mapFragmentLinks.size() == 0) {
            createLinkWithFragment(componentLinkManager);
        } else {
            itemExpand.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandPanel();
                }
            });

            for (final Fragment fragment : mapFragmentLinks.keySet()) {
                View item = mapFragmentLinks.get(fragment);
                if (item.equals(itemAddAnnouncement)) {
                    item.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            hidePanel();
                            notifyObservers(fragment);
                        }
                    });
                } else {
                    item.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyObservers(fragment);
                        }
                    });
                }
            }
        }
    }

    @SuppressLint({"ResourceAsColor"})
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

        itemExpand.setOutlineProvider(viewOutlineProvider);
        itemExpand.setClipToOutline(true);
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
    public void notifyObservers(Object object) {
        observerManager.notifyObservers(this, (Fragment) object);
    }
}