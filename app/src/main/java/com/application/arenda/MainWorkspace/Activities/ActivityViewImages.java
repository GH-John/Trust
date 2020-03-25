package com.application.arenda.MainWorkspace.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.application.arenda.Entities.Announcements.ViewAnnouncement.AdapterPagerBigImageView;
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

    private Uri selectedUri;
    private List<Uri> uriList = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BigImageViewer.initialize(GlideImageLoader.with(this));

        setContentView(R.layout.activity_view_image);

        ButterKnife.bind(this);

        Intent intent = getIntent();

        uriList = intent.getParcelableArrayListExtra("CollectionUri");
        selectedUri = intent.getParcelableExtra("SelectedUri");

        pagerBigImageView.setAdapter(new AdapterPagerBigImageView(uriList));

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

        int positionSelectedUri = uriList.indexOf(selectedUri);

        setIndicatorPosition(positionSelectedUri);

        pagerBigImageView.setCurrentItem(positionSelectedUri);

        itemBtnBack.setOnClickListener(v -> onBackPressed());
    }

    @SuppressLint("SetTextI18n")
    private void setIndicatorPosition(int position) {
        pagerIndicator.setText(position + "/" + uriList.size());
    }
}