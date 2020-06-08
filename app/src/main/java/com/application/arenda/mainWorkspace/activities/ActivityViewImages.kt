package com.application.arenda.mainWorkspace.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.application.arenda.databinding.ActivityViewImageBinding
import com.application.arenda.entities.announcements.viewAnnouncement.AdapterPagerBigImageView
import com.github.piasy.biv.BigImageViewer
import com.github.piasy.biv.loader.glide.GlideImageLoader
import java.util.*

class ActivityViewImages : AppCompatActivity() {
    private var bind: ActivityViewImageBinding? = null

    private var selectedUri: Uri? = null
    private var uriList: List<Uri?> = ArrayList()
    private var adapterPagerBigImageView: AdapterPagerBigImageView? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityViewImageBinding.inflate(layoutInflater)
        setContentView(bind!!.root)

        BigImageViewer.initialize(GlideImageLoader.with(this))

        initComponents()
        initListeners()
        initAdapter()
    }

    private fun initComponents() {
        val intent = intent
        uriList = intent.getParcelableArrayListExtra("CollectionUri")
        selectedUri = intent.getParcelableExtra("SelectedUri")
        adapterPagerBigImageView = AdapterPagerBigImageView(uriList)
    }

    private fun initListeners() {
        adapterPagerBigImageView!!.setImageClickListener {
            if (bind!!.viewImagePanel.visibility == View.INVISIBLE) {
                bind!!.viewImagePanel.visibility = View.VISIBLE
            } else {
                bind!!.viewImagePanel.visibility = View.INVISIBLE
            }
        }
        bind!!.pagerBigImageView.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                var pos = position
                setIndicatorPosition(++pos)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        bind!!.itemBtnBack.setOnClickListener { onBackPressed() }
    }

    private fun initAdapter() {
        bind!!.pagerBigImageView.adapter = adapterPagerBigImageView
        val positionSelectedUri = uriList.indexOf(selectedUri)
        setIndicatorPosition(positionSelectedUri + 1)
        bind!!.pagerBigImageView.currentItem = positionSelectedUri
    }

    @SuppressLint("SetTextI18n")
    private fun setIndicatorPosition(position: Int) {
        bind!!.pagerIndicator.text = position.toString() + "/" + uriList.size
    }
}