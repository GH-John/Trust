package com.application.arenda.mainWorkspace.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.arenda.R
import com.application.arenda.databinding.FragmentViewerUserProfileBinding
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.ModelUserProfileToView
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.ServerResponse
import com.application.arenda.entities.serverApi.user.ApiUser
import com.application.arenda.entities.utils.Utils
import com.application.arenda.entities.utils.glide.GlideUtils
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.viewer_user_header_profile.*

open class FragmentViewerUserProfile private constructor() : Fragment(), AdapterActionBar {
    private var abBtnBack: ImageButton? = null
    private var abItemMore: ImageButton? = null
    private var abUserLogin: TextView? = null

    private var consumerUserProfile: Consumer<List<ModelUser>>? = null
    private var consumerLoadProfile: Consumer<ServerResponse<ModelUserProfileToView>>? = null

    private var api: ApiUser? = null
    private var cacheManager: LocalCacheManager? = null
    private var sharedViewModels: SharedViewModels? = null

    private var userToken: String? = null

    private var disposable = CompositeDisposable()

    private lateinit var bind: FragmentViewerUserProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentViewerUserProfileBinding.inflate(inflater)
        init()
        initListeners()
        initStyles()
        return bind.root
    }

    @SuppressLint("CheckResult")
    private fun init() {
        api = ApiUser.getInstance(context)
        cacheManager = LocalCacheManager.getInstance(context)
        sharedViewModels = ViewModelProvider(requireActivity()).get(SharedViewModels::class.java)

        consumerUserProfile = Consumer { modelUsers: List<ModelUser> ->
            if (modelUsers.isNotEmpty()) {
                userToken = modelUsers[0].token
                updateProfile()
            }
        }

        consumerLoadProfile = Consumer { response ->
            when (response.handler) {
                CodeHandler.SUCCESS -> {
                    bind.swipeRefreshLayout.isRefreshing = false

                    setProfile(response.response)
                }
                CodeHandler.USER_NOT_FOUND -> Utils.messageOutput(context, getString(R.string.error_user_not_found))
                CodeHandler.UNKNOW_ERROR -> Utils.messageOutput(context, getString(R.string.unknown_error))
            }
        }

        disposable.add(cacheManager!!.users()
                .activeUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserProfile))
    }

    private fun initStyles() {
        bind.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed)
    }

    private fun initListeners() {
        bind.swipeRefreshLayout.setOnRefreshListener { updateProfile() }
        bind.profileHeader.btnFollowingUnfollowing.setOnClickListener { }
    }

    @SuppressLint("CheckResult")
    private fun updateProfile() {
        sharedViewModels!!.selectedUser.observe(viewLifecycleOwner, Observer { idUser ->
            run {
                disposable.add(api!!.loadProfileToView(userToken, idUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(consumerLoadProfile))
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setProfile(model: ModelUserProfileToView) {
        GlideUtils.loadAvatar(context, model.avatar, bind.profileHeader.itemUserAvatar, 200, 200)

        userLogin.text = model.login
        abUserLogin?.text = model.login

        userName.text = model.lastName + " " + model.name
        countAnnouncementsUser.text = model.countAnnouncementsUser.toString()
        countUserFollowers.text = model.countFollowers.toString()
        countUserFollowing.text = model.countFollowing.toString()
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    companion object {
        private var fragment: FragmentViewerUserProfile? = null

        val instance: FragmentViewerUserProfile?
            get() {
                if (this.fragment == null) this.fragment = FragmentViewerUserProfile()
                return this.fragment
            }
    }

    override fun initListenersActionBar(viewGroup: ViewGroup?) {
        abBtnBack?.setOnClickListener { ContainerFragments.getInstance(context).popBackStack() }
        abItemMore?.setOnClickListener { Utils.messageOutput(context, "В разработке") }
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup?) {
        abBtnBack = viewGroup?.findViewById(R.id.itemBtnBack)
        abUserLogin = viewGroup?.findViewById(R.id.itemUserLogin)
        abItemMore = viewGroup?.findViewById(R.id.itemBtnMore)
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_profile_toview
    }
}