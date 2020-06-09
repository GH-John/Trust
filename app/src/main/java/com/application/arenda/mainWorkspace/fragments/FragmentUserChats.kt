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
import com.application.arenda.databinding.FragmentUserChatsBinding
import com.application.arenda.entities.chat.ChatsAdapter
import com.application.arenda.entities.models.IModel
import com.application.arenda.entities.models.ModelChat
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.recyclerView.RVOnScrollListener
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.OnApiListener
import com.application.arenda.entities.serverApi.chat.ApiChat
import com.application.arenda.entities.serverApi.client.CodeHandler
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

open class FragmentUserChats private constructor() : Fragment(), AdapterActionBar, ItemSideBar {
    private var sideBar: SideBar? = null
    private var containerFragments: ContainerFragments? = null

    private lateinit var bind: FragmentUserChatsBinding
    private var itemBurgerMenu: ImageButton? = null
    private var rvLayoutManager: LinearLayoutManager? = null
    private var rvOnScrollListener: RVOnScrollListener? = null
    private var rvAdapter: ChatsAdapter? = null

    private lateinit var api: ApiChat

    private var userToken: String? = null

    private lateinit var cacheManager: LocalCacheManager

    private val disposable = CompositeDisposable()

    private var listenerFavoriteInsert: OnApiListener? = null
    private var listenerLoadChats: OnApiListener? = null

    private var consumerUserToken: Consumer<List<ModelUser>>? = null
    private var singleLoaderWithRewriteChats: SingleObserver<List<ModelChat>>? = null
    private var singleLoaderWithoutRewriteChats: SingleObserver<List<ModelChat>>? = null

    private var sharedViewModels: SharedViewModels? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserChatsBinding.inflate(inflater)

        init()

        return bind.root
    }

    private fun init() {
        api = ApiChat.getInstance(context)
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
                if (code == CodeHandler.USER_NOT_FOUND) {
                    Utils.messageOutput(context, resources.getString(R.string.warning_login_required))
                } else if (code == CodeHandler.INTERNAL_SERVER_ERROR) {
                    Utils.messageOutput(context, resources.getString(R.string.error_on_server))
                } else if (code == CodeHandler.UNKNOW_ERROR) {
                    Utils.messageOutput(context, resources.getString(R.string.unknown_error))
                }
            }

            override fun onFailure(t: Throwable) {
                Timber.e(t)
            }
        }
        listenerLoadChats = object : OnApiListener {
            override fun onComplete(code: CodeHandler) {
                when (code) {
                    CodeHandler.USER_NOT_FOUND -> {
                        Utils.messageOutput(context, resources.getString(R.string.warning_login_required))
                        bind.swipeRefreshLayout.isRefreshing = false
                        rvAdapter?.isLoading = false
                    }
                    CodeHandler.UNKNOW_ERROR, CodeHandler.UNSUCCESS, CodeHandler.NOT_CONNECT_TO_DB, CodeHandler.HTTP_NOT_FOUND, CodeHandler.NETWORK_ERROR -> {
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

        singleLoaderWithRewriteChats = object : SingleObserver<List<ModelChat>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelChat>) {
                rvAdapter?.rewriteCollection(collection)
                bind.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter?.isLoading = false
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }

        singleLoaderWithoutRewriteChats = object : SingleObserver<List<ModelChat>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelChat>) {
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
        bind.rvChats.layoutManager = rvLayoutManager
        bind.rvChats.setItemViewCacheSize(50)
        bind.rvChats.setHasFixedSize(true)

        rvOnScrollListener = RVOnScrollListener(rvLayoutManager)
        bind.rvChats.addOnScrollListener(rvOnScrollListener!!)
        rvAdapter = ChatsAdapter()
        rvOnScrollListener?.setRVAdapter(rvAdapter)
        rvAdapter?.setItemViewClick { _: RecyclerView.ViewHolder?, model: IModel?, _: Int ->
            sharedViewModels!!.selectUser((model as ModelChat).idUser)
            containerFragments!!.open(FragmentUserChat())
        }
        bind.rvChats.adapter = rvAdapter
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
        setLoadMoreForAllChats()
    }

    private fun setLoadMoreForAllChats() {
        rvOnScrollListener!!.setOnLoadMoreData { lastID: Long -> addChatsToCollection(lastID, false) }
    }

    @Synchronized
    private fun addChatsToCollection(lastId: Long, rewrite: Boolean) {
        if (!rvAdapter!!.isLoading) {
            rvAdapter?.isLoading = true
            api.loadChats(userToken, lastId, 10, listenerLoadChats!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(if (rewrite) singleLoaderWithRewriteChats!! else singleLoaderWithoutRewriteChats!!)

        } else {
            bind.swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("CheckResult")
    fun refreshLayout() {
        addChatsToCollection(0, true)
    }

    override fun initListenersActionBar(viewGroup: ViewGroup?) {
        itemBurgerMenu?.setOnClickListener { sideBar?.openLeftMenu() }
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup?) {
        itemBurgerMenu = viewGroup?.findViewById(R.id.itemBurgerMenu)
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_chats
    }

    override fun setSideBar(sideBar: SideBar?) {
        this.sideBar = sideBar
    }

    companion object {
        private var fragment: FragmentUserChats? = null

        val instance: FragmentUserChats?
            get() {
                if (this.fragment == null) this.fragment = FragmentUserChats()
                return this.fragment
            }
    }
}