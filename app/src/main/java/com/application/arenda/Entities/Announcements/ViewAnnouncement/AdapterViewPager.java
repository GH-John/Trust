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

public class AdapterViewPager extends PagerAdapter {

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
        return data.getCollectionUriBitmap().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.vp_img_swiper_pattern, container, false);

        ImageView imgContainer = view.findViewById(R.id.imgContainer);
        Uri uri = data.getCollectionUriBitmap().get(position);

        if (itemListener != null)
            imgContainer.setOnClickListener(v -> itemListener.onClick(data.getCollectionUriBitmap(), uri));

        GlideUtils.loadImage(container.getContext(), uri, imgContainer);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
