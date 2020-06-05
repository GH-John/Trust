package com.application.arenda.mainWorkspace.fragments.proposals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.arenda.R
import com.application.arenda.databinding.FragmentOutgoingProposalsBinding
import com.application.arenda.entities.announcements.proposalsAnnouncement.outgoing.OutProposalAdapter
import com.application.arenda.entities.models.ModelProposal
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.recyclerView.RVOnScrollListener
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.OnApiListener
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.CodeHandler.*
import com.application.arenda.entities.serverApi.proposal.ApiProposal
import com.application.arenda.entities.utils.DisplayUtils
import com.application.arenda.entities.utils.Utils
import com.application.arenda.mainWorkspace.fragments.FragmentUserChat
import com.application.arenda.mainWorkspace.fragments.FragmentViewerUserProfile
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class FragmentOutgoingProposals private constructor() : Fragment() {
    private var api: ApiProposal? = null
    private var cacheManager: LocalCacheManager? = null

    private var rvLayoutManager: LinearLayoutManager? = null
    private var rvOnScrollListener: RVOnScrollListener? = null

    private var userToken: String? = null

    private var singleLoaderWithRewriteProposals: SingleObserver<List<ModelProposal>>? = null
    private var singleLoaderWithoutRewriteProposals: SingleObserver<List<ModelProposal>>? = null

    private var consumerUserToken: Consumer<List<ModelUser>>? = null
    private var listenerFavoriteInsert: OnApiListener? = null
    private var listenerLoadAnnouncement: OnApiListener? = null

    private var sharedViewModels: SharedViewModels? = null

    private var rvAdapter: OutProposalAdapter = OutProposalAdapter()

    private var disposable = CompositeDisposable()
    private var containerFragments: ContainerFragments? = null

    private lateinit var bind: FragmentOutgoingProposalsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentOutgoingProposalsBinding.inflate(inflater)

        init()

        return bind.root
    }

    private fun init() {
        api = ApiProposal.getInstance(context)

        cacheManager = LocalCacheManager.getInstance(context)

        rvLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        sharedViewModels = ViewModelProvider(requireActivity()).get(SharedViewModels::class.java)

        containerFragments = ContainerFragments.getInstance(context)

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
                    UNKNOW_ERROR, UNSUCCESS, NOT_CONNECT_TO_DB, HTTP_NOT_FOUND, NETWORK_ERROR -> {
                        Utils.messageOutput(context, resources.getString(R.string.error_check_internet_connect))
                    }
                    NONE_REZULT -> {
                        rvAdapter.isLoading = false
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Timber.e(t)
            }
        }

        consumerUserToken = Consumer { modelUsers: List<ModelUser> ->
            userToken = if (modelUsers.isNotEmpty())
                modelUsers[0].token
            else null

            bind.swipeRefreshLayout.isRefreshing = true
            refreshLayout()
        }

        cacheManager!!.users()
                .activeUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserToken)
        singleLoaderWithRewriteProposals = object : SingleObserver<List<ModelProposal>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelProposal>) {
                rvAdapter.rewriteCollection(collection)
                bind.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter.isLoading = false
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }
        singleLoaderWithoutRewriteProposals = object : SingleObserver<List<ModelProposal>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelProposal>) {
                rvAdapter.addToCollection(collection)
                bind.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter.isLoading = false
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun initAdapters() {
        bind.rvOutgoingProposals.layoutManager = rvLayoutManager
        bind.rvOutgoingProposals.setItemViewCacheSize(50)
        bind.rvOutgoingProposals.setHasFixedSize(true)

        rvOnScrollListener = RVOnScrollListener(rvLayoutManager)

        bind.rvOutgoingProposals.addOnScrollListener(rvOnScrollListener!!)

        rvAdapter.setItemUserAvatarListener { _, model, _ ->
            run {
                sharedViewModels!!.selectUser((model as ModelProposal).idUser)
                containerFragments!!.open(FragmentViewerUserProfile.instance!!)
            }
        }

        rvAdapter.setBtnSendMessageListener { _, model, _ ->
            run {
                sharedViewModels!!.selectUser((model as ModelProposal).idUser)
                containerFragments!!.open(FragmentUserChat())
            }
        }

        rvAdapter.setBtnRejectListener { vh, model, position ->
            run {
                vh.itemView.visibility = GONE

                val snackbar = Snackbar
                        .make(bind.root, getString(R.string.warning_proposal_reject), Snackbar.LENGTH_LONG)

                var snackbarCallBack = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                        super.onDismissed(transientBottomBar, event)
                        api!!.rejectOutgoingProposal(userToken, model.id)
                                .subscribeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe { response ->
                                    run {
                                        when (response.handler) {
                                            SUCCESS -> rvAdapter.removeFromCollection(position)
                                            UNSUCCESS -> {
                                                Utils.messageOutput(context, getString(R.string.error_unsuccess_reject_proposal))
                                                vh.itemView.visibility = VISIBLE
                                            }
                                            NETWORK_ERROR -> {
                                                Utils.messageOutput(context, getString(R.string.error_check_internet_connect))
                                                vh.itemView.visibility = VISIBLE
                                            }
                                            PROPOSAL_NOT_FOUND -> {
                                                Utils.messageOutput(context, getString(R.string.error_proposal_not_found))
                                                vh.itemView.visibility = VISIBLE
                                            }
                                            else -> {
                                                Timber.e(response.error)
                                                Utils.messageOutput(context, getString(R.string.unknown_error))
                                                vh.itemView.visibility = VISIBLE
                                            }
                                        }
                                    }
                                }
                    }
                }

                snackbar.addCallback(snackbarCallBack)

                snackbar.setAction(getString(R.string.text_cancle)) {
                    vh.itemView.visibility = VISIBLE
                    snackbar.removeCallback(snackbarCallBack)
                }.setActionTextColor(requireContext().getColor(R.color.colorWhite))

                snackbar.show()
            }
        }

        rvOnScrollListener!!.setRVAdapter(rvAdapter)


        bind.rvOutgoingProposals.adapter = rvAdapter
    }

    private fun initStyles() {
        bind.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed)
    }

    private fun initListeners() {
        bind.swipeRefreshLayout.setProgressViewEndTarget(false, DisplayUtils.dpToPx(120))
        bind.swipeRefreshLayout.setOnRefreshListener { this.refreshLayout() }
        setLoadMoreForAllAnnouncement()
    }

    private fun setLoadMoreForAllAnnouncement() {
        rvOnScrollListener!!.setOnLoadMoreData { lastID: Long -> addProposalsToCollection(lastID, false) }
    }

    @Synchronized
    private fun addProposalsToCollection(lastId: Long, rewrite: Boolean) {
        if (!rvAdapter.isLoading) {
            rvAdapter.isLoading = true
            api!!.loadOutgoingProposal(userToken, lastId, 10, listenerLoadAnnouncement)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((if (rewrite) singleLoaderWithRewriteProposals else singleLoaderWithoutRewriteProposals)!!)
        } else {
            bind.swipeRefreshLayout.isRefreshing = false
        }
    }

    @SuppressLint("CheckResult")
    fun refreshLayout() {
        addProposalsToCollection(0, true)
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    companion object {
        private var fragment: FragmentOutgoingProposals? = null

        val instance: FragmentOutgoingProposals?
            get() {
                if (this.fragment == null) this.fragment = FragmentOutgoingProposals()
                return this.fragment
            }
    }
}