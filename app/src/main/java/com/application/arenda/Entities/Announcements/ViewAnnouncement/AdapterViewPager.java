package com.application.arenda.Entities.Announcements.ViewAnnouncement;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.application.arenda.Entities.Utils.Glide.GlideUtils;
import com.application.arenda.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterViewPager extends PagerAdapter {
    @BindView(R.id.imgContainer)
    ImageView imgContainer;

    private ModelViewPager data;

    private OnItemClickViewPager itemListener;

    public AdapterViewPager(ModelViewPager data) {
        this.data = data;
    }

    public void setOnClickListener(OnItemClickViewPager listener) {
        this.itemListener = listener;
    }

    @Override
    public int getCount() {
        return data.getCollectionUri().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vp_img_swiper_pattern, container, false);
        ButterKnife.bind(this, view);

        Uri uri = data.getCollectionUri().get(position);

        if (itemListener != null)
            imgContainer.setOnClickListener(v -> itemListener.onClick(data.getCollectionUri(), uri));

        GlideUtils.loadImage(container.getContext(), uri, imgContainer);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}