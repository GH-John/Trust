package com.application.arenda.mainWorkspace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserMessagesBinding
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.sideBar.ItemSideBar
import com.application.arenda.ui.widgets.sideBar.SideBar
import io.reactivex.functions.Consumer

open class FragmentUserMessages private constructor() : Fragment(), AdapterActionBar, ItemSideBar {
    private var bind: FragmentUserMessagesBinding? = null
    private var burgerMenu: ImageButton? = null
    private var sideBar: SideBar? = null

    private var consumerUserProfile: Consumer<List<ModelUser>>? = null
    private var cacheManager: LocalCacheManager? = null
    private var sharedViewModels: SharedViewModels? = null
    private var userToken: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserMessagesBinding.inflate(inflater)
        return bind!!.root
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_messages
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup) {
        burgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu)
    }

    override fun initListenersActionBar(viewGroup: ViewGroup) {
        burgerMenu!!.setOnClickListener { sideBar?.openLeftMenu() }
    }

    companion object {
        private var fragment: FragmentUserMessages? = null

        val instance: FragmentUserMessages?
            get() {
                if (this.fragment == null) this.fragment = FragmentUserMessages()
                return this.fragment
            }
    }

    override fun setSideBar(sideBar: SideBar?) {
        this.sideBar = sideBar
    }
}