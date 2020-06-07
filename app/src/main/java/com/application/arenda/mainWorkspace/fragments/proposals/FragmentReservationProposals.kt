package com.application.arenda.mainWorkspace.fragments.proposals

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.application.arenda.R
import com.application.arenda.databinding.FragmentReservationProposalsBinding
import com.application.arenda.entities.announcements.proposalsAnnouncement.reservation.ReservationProposalAdapter
import com.application.arenda.entities.models.IModel
import com.application.arenda.entities.models.ModelProposal
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.recyclerView.RVOnScrollListener
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.OnApiListener
import com.application.arenda.entities.serverApi.client.ApiHandler
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

class FragmentReservationProposals private constructor() : Fragment() {
    private var api: ApiProposal? = null
    private var cacheManager: LocalCacheManager? = null

    private var rvLayoutManager: LinearLayoutManager? = null
    private var rvOnScrollListener: RVOnScrollListener? = null

    private var userToken: String? = null

    private var singleLoaderWithRewriteProposals: SingleObserver<List<ModelProposal>>? = null
    private var singleLoaderWithoutRewriteProposals: SingleObserver<List<ModelProposal>>? = null

    private var consumerUserToken: Consumer<List<ModelUser>>? = null
    private var listenerLoadProposal: OnApiListener? = null

    private var sharedViewModels: SharedViewModels? = null

    private var rvAdapter: ReservationProposalAdapter? = null

    private var disposable = CompositeDisposable()

    private var containerFragments: ContainerFragments? = null

    private lateinit var bind: FragmentReservationProposalsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentReservationProposalsBinding.inflate(inflater)

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
        listenerLoadProposal = object : OnApiListener {
            override fun onComplete(code: CodeHandler) {
                when (code) {
                    UNKNOW_ERROR, UNSUCCESS, NOT_CONNECT_TO_DB, HTTP_NOT_FOUND, NETWORK_ERROR -> {
                        Utils.messageOutput(context, resources.getString(R.string.error_check_internet_connect))
                    }
                    NONE_REZULT -> {
                        rvAdapter?.isLoading = false
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
                rvAdapter?.rewriteCollection(collection)
                bind.swipeRefreshLayout.isRefreshing = false
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter?.isLoading = false
                bind.swipeRefreshLayout.isRefreshing = false
            }
        }
        singleLoaderWithoutRewriteProposals = object : SingleObserver<List<ModelProposal>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelProposal>) {
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

    private fun initAdapters() {
        bind.rvReservationProposals.layoutManager = rvLayoutManager
        bind.rvReservationProposals.setItemViewCacheSize(50)
        bind.rvReservationProposals.setHasFixedSize(true)

        rvOnScrollListener = RVOnScrollListener(rvLayoutManager)

        bind.rvReservationProposals.addOnScrollListener(rvOnScrollListener!!)
        rvAdapter = ReservationProposalAdapter()
        rvOnScrollListener!!.setRVAdapter(rvAdapter)

        rvAdapter?.setItemUserAvatarListener { _, model, _ ->
            run {
                sharedViewModels!!.selectUser((model as ModelProposal).idUser)
                containerFragments!!.open(FragmentViewerUserProfile.instance!!)
            }
        }

        rvAdapter?.setBtnSendMessageListener { _, model, _ ->
            run {
                sharedViewModels!!.selectUser((model as ModelProposal).idUser)
                containerFragments!!.open(FragmentUserChat())
            }
        }

        rvAdapter?.setBtnStartListener { vh, model, position ->
            run {
                snackBarStartProposal(vh, model, position)
            }
        }

        rvAdapter?.setItemRescheduleReservation { vh, model, position ->
            run {
                snackBarRescheduleReservationProposal(vh, model, position)
            }
        }

        rvAdapter?.setItemCancleReservation { vh, model, position ->
            run {
                snackBarCancleReservationProposal(vh, model, position)
            }
        }

        bind.rvReservationProposals.adapter = rvAdapter
    }

    private fun snackBarCancleReservationProposal(vh: ViewHolder?, model: IModel?, position: Int?) {
        position?.let { rvAdapter?.removeFromCollection(it) }

        val snackbar = Snackbar
                .make(bind.root, getString(R.string.warning_cancle_reservation_start), Snackbar.LENGTH_LONG)

        val snackbarCallBack = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                model?.id?.let {
                    api!!.cancleReservationProposal(userToken, it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getConsumerCancleReservationProposal(vh, model, position))
                }
            }
        }

        snackbar.addCallback(snackbarCallBack)

        snackbar.setAction(getString(R.string.text_cancle)) {
            rvAdapter?.addToCollection(model as ModelProposal)
            snackbar.removeCallback(snackbarCallBack)
        }.setActionTextColor(requireContext().getColor(R.color.colorWhite))

        snackbar.show()
    }

    private fun getConsumerCancleReservationProposal(vh: ViewHolder?, model: IModel?, position: Int?): Consumer<ApiHandler> {
        return Consumer { response ->
            run {
                when (response.handler) {
                    SUCCESS -> {
                    }
                    UNSUCCESS -> {
                        Utils.messageOutput(context, getString(R.string.error_unsuccess_cancle_reservation_proposal))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                    NETWORK_ERROR -> {
                        Utils.messageOutput(context, getString(R.string.error_check_internet_connect))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                    PROPOSAL_NOT_FOUND -> {
                        Utils.messageOutput(context, getString(R.string.error_proposal_not_found))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                    else -> {
                        Timber.e(response.error)
                        Utils.messageOutput(context, getString(R.string.unknown_error))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                }
            }
        }
    }


    private fun snackBarRescheduleReservationProposal(vh: ViewHolder?, model: IModel?, position: Int?) {
        val snackbar = Snackbar
                .make(bind.root, getString(R.string.warning_reschedule_reservation_start), Snackbar.LENGTH_LONG)

        val snackbarCallBack = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                model?.id?.let {
                    api!!.rescheduleReservationProposal(userToken, it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getConsumerRescheduleReservationProposal(vh, model, position))
                }
            }
        }

        snackbar.addCallback(snackbarCallBack)

        snackbar.setAction(getString(R.string.text_cancle)) {
            snackbar.removeCallback(snackbarCallBack)
        }.setActionTextColor(requireContext().getColor(R.color.colorWhite))

        snackbar.show()
    }

    private fun getConsumerRescheduleReservationProposal(vh: ViewHolder?, model: IModel?, position: Int?): Consumer<ApiHandler> {
        return Consumer { response ->
            run {
                when (response.handler) {
                    SUCCESS -> {
                    }
                    UNSUCCESS -> {
                        Utils.messageOutput(context, getString(R.string.error_unsuccess_reschedule_reservation_proposal))
                    }
                    NETWORK_ERROR -> {
                        Utils.messageOutput(context, getString(R.string.error_check_internet_connect))
                    }
                    PROPOSAL_NOT_FOUND -> {
                        Utils.messageOutput(context, getString(R.string.error_proposal_not_found))
                    }
                    else -> {
                        Timber.e(response.error)
                        Utils.messageOutput(context, getString(R.string.unknown_error))
                    }
                }
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun snackBarStartProposal(vh: ViewHolder?, model: IModel?, position: Int?) {
        position?.let { rvAdapter?.removeFromCollection(it) }

        val snackbar = Snackbar
                .make(bind.root, getString(R.string.warning_proposal_start), Snackbar.LENGTH_LONG)

        val snackbarCallBack = object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                model?.id?.let {
                    api!!.startProposal(userToken, it)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(getConsumerStartProposal(vh, model, position))
                }
            }
        }

        snackbar.addCallback(snackbarCallBack)

        snackbar.setAction(getString(R.string.text_cancle)) {
            rvAdapter?.addToCollection(model as ModelProposal)
            snackbar.removeCallback(snackbarCallBack)
        }.setActionTextColor(requireContext().getColor(R.color.colorWhite))

        snackbar.show()
    }

    private fun getConsumerStartProposal(vh: ViewHolder?, model: IModel?, position: Int?): Consumer<ApiHandler> {
        return Consumer { response ->
            run {
                when (response.handler) {
                    SUCCESS -> {
                    }
                    UNSUCCESS -> {
                        Utils.messageOutput(context, getString(R.string.error_unsuccess_start_proposal))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                    NETWORK_ERROR -> {
                        Utils.messageOutput(context, getString(R.string.error_check_internet_connect))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                    PROPOSAL_NOT_FOUND -> {
                        Utils.messageOutput(context, getString(R.string.error_proposal_not_found))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                    else -> {
                        Timber.e(response.error)
                        Utils.messageOutput(context, getString(R.string.unknown_error))
                        rvAdapter?.addToCollection(model as ModelProposal)
                    }
                }
            }
        }
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
        if (!rvAdapter?.isLoading!!) {
            rvAdapter?.isLoading = true
            api!!.loadReservationProposal(userToken, lastId, 10, listenerLoadProposal)
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
        private var fragment: FragmentReservationProposals? = null

        val instance: FragmentReservationProposals?
            get() {
                if (this.fragment == null) this.fragment = FragmentReservationProposals()
                return this.fragment
            }
    }
}