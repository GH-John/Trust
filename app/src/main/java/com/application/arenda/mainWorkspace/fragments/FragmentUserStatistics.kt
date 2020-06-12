package com.application.arenda.mainWorkspace.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserStatisticsBinding
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.CodeHandler.*
import com.application.arenda.entities.serverApi.user.ApiUser
import com.application.arenda.entities.utils.Utils
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.sideBar.ItemSideBar
import com.application.arenda.ui.widgets.sideBar.SideBar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class FragmentUserStatistics : Fragment(), AdapterActionBar, ItemSideBar {
    private var itemBurgerMenu: ImageButton? = null
    private var sideBar: SideBar? = null

    private var consumerUserProfile: Consumer<List<ModelUser>>? = null
    private var consumerLoadProfile: Consumer<CodeHandler>? = null

    private var api: ApiUser? = null
    private var cacheManager: LocalCacheManager? = null

    private var user: ModelUser? = null
    private var userToken: String? = null

    private var disposable = CompositeDisposable()

    private lateinit var bind: FragmentUserStatisticsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserStatisticsBinding.inflate(inflater)

        styles()
        init()
        listeners()

        return bind.root
    }

    private fun styles() {
        bind.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed)
    }

    private fun init() {
        api = ApiUser.getInstance(context)
        cacheManager = LocalCacheManager.getInstance(context)

        consumerUserProfile = Consumer { modelUsers: List<ModelUser> ->
            if (modelUsers.isNotEmpty()) {
                if (user == null) {
                    user = modelUsers[0]
                    userToken = modelUsers[0].token

                    update()
                } else if (user!!.updated.isBefore(modelUsers[0].updated)) {
                    user = modelUsers[0]
                    userToken = modelUsers[0].token
                } else {
                    bind.swipeRefreshLayout.isRefreshing = false
                }

                setStatistic(user!!)
            }
        }

        consumerLoadProfile = Consumer { handler ->
            run {
                when (handler) {
                    SUCCESS -> bind.swipeRefreshLayout.isRefreshing = false

                    UNSUCCESS -> {
                        Utils.messageOutput(context, getString(R.string.error_load_profile))
                        bind.swipeRefreshLayout.isRefreshing = false
                    }

                    USER_NOT_FOUND -> {
                        Utils.messageOutput(context, getString(R.string.error_user_not_found))
                        bind.swipeRefreshLayout.isRefreshing = false
                    }

                    NETWORK_ERROR -> {
                        Utils.messageOutput(context, getString(R.string.error_check_internet_connect))
                        bind.swipeRefreshLayout.isRefreshing = false
                    }

                    else -> {
                        Utils.messageOutput(context, getString(R.string.unknown_error))
                        bind.swipeRefreshLayout.isRefreshing = false
                    }
                }
            }
        }

        bind.swipeRefreshLayout.isRefreshing = true

        disposable.add(cacheManager!!.users()
                .activeUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserProfile))
    }

    private fun listeners() {
        bind.swipeRefreshLayout.setOnRefreshListener { update() }
    }

    @SuppressLint("CheckResult")
    private fun update() {
        disposable.add(api!!.loadOwnProfile(context, userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerLoadProfile!!))
    }

    @SuppressLint("SetTextI18n")
    private fun setStatistic(model: ModelUser?) {
        bind.countViews.text = model?.countAllViewers.toString()
        bind.countAnnouncements.text = model?.countAnnouncementsUser.toString()
        bind.countFollowers.text = model?.countFollowers.toString()
        bind.countFollowing.text = model?.countFollowing.toString()
        bind.userRate.text = model?.rating.toString()
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_statistics
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup) {
        itemBurgerMenu = viewGroup.findViewById(R.id.itemBurgerMenu)
    }

    override fun initListenersActionBar(viewGroup: ViewGroup) {
        itemBurgerMenu!!.setOnClickListener { sideBar!!.openLeftMenu() }
    }

    override fun setSideBar(sideBar: SideBar) {
        this.sideBar = sideBar
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var fragmentUserStatistics: FragmentUserStatistics? = null

        @JvmStatic
        val instance: FragmentUserStatistics?
            get() {
                if (fragmentUserStatistics == null) fragmentUserStatistics = FragmentUserStatistics()
                return fragmentUserStatistics
            }
    }
}