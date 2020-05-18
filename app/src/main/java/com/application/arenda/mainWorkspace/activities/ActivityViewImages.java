package com.application.arenda.mainWorkspace.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.entities.announcements.viewAnnouncement.AdapterPagerBigImageView;
import com.application.arenda.R;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityViewImages extends AppCompatActivity {

    @Nullable
    @BindView(R.id.itemBtnBack)
    ImageButton itemBtnBack;

    @Nullable
    @BindView(R.id.pagerBigImageView)
    ViewPager pagerBigImageView;

    @Nullable
    @BindView(R.id.pagerIndicator)
    TextView pagerIndicator;

    @Nullable
    @BindView(R.id.viewImagePanel)
    RelativeLayout viewImagePanel;

    private Uri selectedUri;
    private List<Uri> uriList = new ArrayList<>();

    private AdapterPagerBigImageView adapterPagerBigImageView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BigImageViewer.initialize(GlideImageLoader.with(this));

        setContentView(R.layout.activity_view_image);

        ButterKnife.bind(this);

        initComponents();

        initListeners();

        initAdapter();
    }

    private void initComponents() {
        Intent intent = getIntent();

        uriList = intent.getParcelableArrayListExtra("CollectionUri");
        selectedUri = intent.getParcelableExtra("SelectedUri");

        adapterPagerBigImageView = new AdapterPagerBigImageView(uriList);
    }

    private void initListeners() {
        adapterPagerBigImageView.setImageClickListener(v -> {
            if (viewImagePanel.getVisibility() == View.INVISIBLE) {
                viewImagePanel.setVisibility(View.VISIBLE);
            } else {
                viewImagePanel.setVisibility(View.INVISIBLE);
            }
        });

        pagerBigImageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorPosition(++position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        itemBtnBack.setOnClickListener(v -> onBackPressed());
    }

    private void initAdapter() {
        pagerBigImageView.setAdapter(adapterPagerBigImageView);

        int positionSelectedUri = uriList.indexOf(selectedUri);

        setIndicatorPosition(positionSelectedUri + 1);

        pagerBigImageView.setCurrentItem(positionSelectedUri);
    }

    @SuppressLint("SetTextI18n")
    private void setIndicatorPosition(int position) {
        pagerIndicator.setText(position + "/" + uriList.size());
    }
}