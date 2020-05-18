package com.application.arenda.mainWorkspace.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.arenda.R
import com.application.arenda.databinding.ActivityMainBinding
import com.application.arenda.mainWorkspace.fragments.FragmentAllAnnouncements
import com.application.arenda.ui.widgets.ComponentManager
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import com.application.arenda.ui.widgets.sideBar.ContainerDrawerLayout
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

@ContainerOptions(CacheImplementation.SPARSE_ARRAY)
class ActivityMain : AppCompatActivity() {
    private var containerFragments: ContainerFragments? = null
    private var containerDrawerLayout: ContainerDrawerLayout? = null
    private lateinit var bindingUtil: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        containerFragments = ContainerFragments.getInstance(this)
        containerDrawerLayout = ContainerDrawerLayout.getInstance(this)
        ComponentManager.addLink(containerFragments, bindingUtil.customBottomNavigation, bindingUtil.customActionBar, containerDrawerLayout)
        containerFragments?.open(FragmentAllAnnouncements.getInstance())
    }
}