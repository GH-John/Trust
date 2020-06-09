package com.application.arenda.mainWorkspace.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserFavoriteAnnouncementsBinding
import com.application.arenda.entities.announcements.loadingAnnouncements.favorites.FavoriteAnnouncementVH
import com.application.arenda.entities.announcements.loadingAnnouncements.favorites.FavoriteAnnouncementsAdapter
import com.application.arenda.entities.models.IModel
import com.application.arenda.entities.models.ModelFavoriteAnnouncement
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.recyclerView.RVOnScrollListener
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.OnApiListener
import com.application.arenda.entities.serverApi.announcement.ApiAnnouncement
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.CodeHandler.*
import com.application.arenda.entities.utils.DisplayUtils
import com.application.arenda.entities.utils.Utils
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import com.application.arenda.ui.widgets.sideBar.ItemSideBar
import com.application.arenda.ui.widgets.sideBar.SideBar
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

open class FragmentUserFavoriteAnnouncements private constructor() : Fragment(), AdapterActionBar, ItemSideBar {
    private var sideBar: SideBar? = null
    private var containerFragments: ContainerFragments? = null

    private lateinit var bind: FragmentUserFavoriteAnnouncementsBinding
    private var itemBurgerMenu: ImageButton? = null
    private var rvLayoutManager: LinearLayoutManager? = null
    private var rvOnScrollListener: RVOnScrollListener? = null
    private var rvAdapter: FavoriteAnnouncementsAdapter? = null

    private lateinit var api: ApiAnnouncement

    private var userToken: String? = null

    private lateinit var cacheManager: LocalCacheManager

    private val disposable = CompositeDisposable()

    private var listenerFavoriteInsert: OnApiListener? = null
    private var listenerLoadAnnouncement: OnApiListener? = null

    private var consumerUserToken: Consumer<List<ModelUser>>? = null
    private var singleLoaderWithRewriteAnnouncements: SingleObserver<List<ModelFavoriteAnnouncement>>? = null
    private var singleLoaderWithoutRewriteAnnouncements: SingleObserver<List<ModelFavoriteAnnouncement>>? = null

    private lateinit var sharedViewModels: SharedViewModels

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserFavoriteAnnouncementsBinding.inflate(inflater)

        init()

        return bind.root
    }

    private fun init() {
        api = ApiAnnouncement.getInstance(context)
        cacheManager = LocalCacheManager.getInstance(context)
        rvLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        containerFragments = ContainerFragments.getInstance(context)
        sharedViewModels = ViewModelProvider(requireActivity()).get(SharedViewModels::class.java)

        initInterfaces()
        initAdapters()
        initStyles()
        initListeners()
    }

    @SuppressLint("CheckResult")
    private fun initInterfaces() {
        listenerFavoriteInsert = object : OnApiListener {
            override fun onComplete(code: CodeHandler) {
                when (code) {
                    USER_NOT_FOUND -> {
                        Utils.messageOutput(context, resources.getString(R.string.warning_login_required))
                    }
                    INTERNAL_SERVER_ERROR -> {
                        Utils.messageOutput(context, resources.getString(R.string.error_on_server))
                    }
                    UNKNOW_ERROR -> {
                        Utils.messageOutput(context, resources.getString(R.string.unknown_error))
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Timber.e(t)
            }
        }
        listenerLoadAnnouncement = object : OnApiListener {
            override fun onComplete(code: CodeHandler) {
                when (code) {
                    USER_NOT_FOUND -> {
                        Utils.messageOutput(context, resources.getString(R.string.warning_login_required))
                        bind.swipeRefreshLayout.isRefreshing = false
                        rvAdapter?.isLoading = false
                    }
                    UNKNOW_ERROR, UNSUCCESS, NOT_CONNECT_TO_DB, HTTP_NOT_FOUND, NETWORK_ERROR -> {
                        Utils.messageOutput(context, resources.getString(R.string.error_check_internet_connect))
                        bind.swipeRefreshLayout.isRefreshing = false
                        rvAdapter?.isLoading = false
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Timber.e(t)
            }
        }
        consumerUserToken = Consumer { modelUsers: List<ModelUser> ->
            userToken = if (modelUsers.isNotEmpty()) modelUsers[0].token else null

            bind.swipeRefreshLayout.isRefreshing = true
            refreshLayout()
        }

        cacheManager
                .users()
                .activeUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserToken)

        singleLoaderWithRewriteAnnouncements = object : SingleObserver<List<ModelFavoriteAnnouncement>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelFavoriteAnnouncement>) {
                rvAdapter?.rewriteCollection(collection)
                bind.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter?.isLoading = false
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }

        singleLoaderWithoutRewriteAnnouncements = object : SingleObserver<List<ModelFavoriteAnnouncement>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelFavoriteAnnouncement>) {
                rvAdapter?.addToCollection(collection)
                bind.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter?.isLoading = false
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initAdapters() {
        bind.rvFavorites.layoutManager = rvLayoutManager
        bind.rvFavorites.setItemViewCacheSize(50)
        bind.rvFavorites.setHasFixedSize(true)

        rvOnScrollListener = RVOnScrollListener(rvLayoutManager)
        bind.rvFavorites.addOnScrollListener(rvOnScrollListener!!)
        rvAdapter = FavoriteAnnouncementsAdapter()
        rvOnScrollListener?.setRVAdapter(rvAdapter)
        rvAdapter?.setItemViewClick { _: RecyclerView.ViewHolder?, model: IModel?, position: Int ->
//            sharedViewModels!!.selectAnnouncement(model as ModelFavoriteAnnouncement)
//            containerFragments!!.open(FragmentViewAnnouncement())
        }
        rvAdapter?.setItemHeartClick { viewHolder: RecyclerView.ViewHolder, model: IModel, position: Int ->
            (model as ModelFavoriteAnnouncement).idAnnouncement.let {
                api.insertToFavorite(userToken, it, listenerFavoriteInsert)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<Boolean> {
                            override fun onSubscribe(d: Disposable) {
                                disposable.add(d)
                            }

                            override fun onSuccess(t: Boolean) {
                                (viewHolder as FavoriteAnnouncementVH).setActiveHeart(t)
                            }

                            override fun onError(e: Throwable) {
                                Timber.e(e)
                            }
                        })
            }
        }
        rvAdapter?.setItemUserAvatarClick { _: RecyclerView.ViewHolder?, model: IModel, position: Int ->
            sharedViewModels.selectUser((model as ModelFavoriteAnnouncement).idUser)
            containerFragments!!.open(instance!!)
        }
        bind.rvFavorites.adapter = rvAdapter
    }

    private fun initStyles() {
        bind.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed)
        bind.swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtils.dpToPx(120))
    }

    private fun initListeners() {
        bind.swipeRefreshLayout.setOnRefreshListener { refreshLayout() }
        setLoadMoreForAllAnnouncement()
    }

    private fun setLoadMoreForAllAnnouncement() {
        rvOnScrollListener!!.setOnLoadMoreData { lastID: Long -> addAnnouncementsToCollection(lastID, false) }
    }

    @Synchronized
    private fun addAnnouncementsToCollection(lastId: Long, rewrite: Boolean) {
        if (!rvAdapter!!.isLoading) {
            rvAdapter?.isLoading = true
            api.loadFavoriteAnnouncements(userToken, lastId, 10, listenerLoadAnnouncement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(if (rewrite) singleLoaderWithRewriteAnnouncements!! else singleLoaderWithoutRewriteAnnouncements!!)

        } else {
            bind.swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("CheckResult")
    fun refreshLayout() {
        addAnnouncementsToCollection(0, true)
    }

    override fun initListenersActionBar(viewGroup: ViewGroup?) {
        itemBurgerMenu?.setOnClickListener { sideBar?.openLeftMenu() }
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup?) {
        itemBurgerMenu = viewGroup?.findViewById(R.id.itemBurgerMenu)
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_favorite_announcements
    }

    override fun setSideBar(sideBar: SideBar?) {
        this.sideBar = sideBar
    }

    companion object {
        private var fragment: FragmentUserFavoriteAnnouncements? = null

        val instance: FragmentUserFavoriteAnnouncements?
            get() {
                if (this.fragment == null) this.fragment = FragmentUserFavoriteAnnouncements()
                return this.fragment
            }
    }
}