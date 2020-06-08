package com.application.arenda.mainWorkspace.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserProfileBinding
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.CodeHandler.*
import com.application.arenda.entities.serverApi.user.ApiUser
import com.application.arenda.entities.utils.Utils
import com.application.arenda.entities.utils.glide.GlideUtils
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.observers.ResourceCompletableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.user_container_profile.*
import kotlinx.android.synthetic.main.user_header_profile.*
import timber.log.Timber

open class FragmentUserProfile private constructor() : Fragment(), AdapterActionBar {
    private var abBtnBack: ImageButton? = null
    private var abBtnSave: TextView? = null
    private var abUserLogin: TextView? = null

    private var consumerUserProfile: Consumer<List<ModelUser>>? = null
    private var consumerLoadProfile: Consumer<CodeHandler>? = null

    private var api: ApiUser? = null
    private var cacheManager: LocalCacheManager? = null

    private var user: ModelUser? = null
    private var userToken: String? = null

    private var disposable = CompositeDisposable()

    private lateinit var bind: FragmentUserProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        bind = FragmentUserProfileBinding.inflate(inflater)

        init()
        initListeners()
        initStyles()
        return bind.root
    }

    @SuppressLint("CheckResult")
    private fun init() {
        api = ApiUser.getInstance(context)
        cacheManager = LocalCacheManager.getInstance(context)

        consumerUserProfile = Consumer { modelUsers: List<ModelUser> ->
            if (modelUsers.isNotEmpty()) {
                if (user == null) {
                    user = modelUsers[0]
                    userToken = modelUsers[0].token

                    updateProfile()
                } else if (user!!.updated.isBefore(modelUsers[0].updated)) {
                    user = modelUsers[0]
                    userToken = modelUsers[0].token

                    bind.swipeRefreshLayout.isRefreshing = false
                    bind.progressUserProfile.visibility = View.GONE
                }
                setProfile(user!!)
            }
        }

        consumerLoadProfile = Consumer { handler ->
            run {
                when (handler) {
                    SUCCESS -> {
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
                    }

                    UNSUCCESS -> {
                        Utils.messageOutput(context, getString(R.string.error_load_profile))
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
                    }

                    USER_NOT_FOUND -> {
                        Utils.messageOutput(context, getString(R.string.error_user_not_found))
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
                    }

                    NETWORK_ERROR -> {
                        Utils.messageOutput(context, getString(R.string.error_check_internet_connect))
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
                    }

                    else -> {
                        Utils.messageOutput(context, getString(R.string.unknown_error))
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
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

    private fun initStyles() {
        Utils.setPhoneMask(resources.getString(R.string.hint_phone), bind.profileContainer.editFirstPhone)
        Utils.setPhoneMask(resources.getString(R.string.hint_phone), bind.profileContainer.editSecondPhone)
        Utils.setPhoneMask(resources.getString(R.string.hint_phone), bind.profileContainer.editThirdPhone)

        bind.swipeRefreshLayout.setColorSchemeResources(
                R.color.colorBlue,
                R.color.colorAccent,
                R.color.colorRed)
    }

    private fun initListeners() {
        bind.swipeRefreshLayout.setOnRefreshListener { updateProfile() }
        bind.profileHeader.itemBtnSettings.setOnClickListener { Utils.messageOutput(context, "В разработке") }
    }

    @SuppressLint("CheckResult")
    private fun updateProfile() {
        disposable.add(api!!.loadOwnProfile(context, userToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerLoadProfile!!))
    }

    @SuppressLint("SetTextI18n")
    private fun setProfile(model: ModelUser?) {
        GlideUtils.loadAvatar(context, model?.avatar, bind.profileHeader.itemUserAvatar, 200, 200)

        userLogin.text = model?.login
        abUserLogin?.text = model?.login

        userName.text = model?.lastName + " " + model?.name
        countAnnouncementsUser.text = model?.countAnnouncementsUser.toString()
        countUserFollowers.text = model?.countFollowers.toString()
        countUserFollowing.text = model?.countFollowing.toString()

        editFirstAddress.setText(model?.address_1)
        editSecondAddress.setText(model?.address_2)
        editThirdAddress.setText(model?.address_3)

        editFirstPhone.setText(model?.phone_1)
        editSecondPhone.setText(model?.phone_2)
        editThirdPhone.setText(model?.phone_3)
    }

    @SuppressLint("CheckResult")
    private fun updateUserData() {
        bind.progressUserProfile.visibility = View.VISIBLE

        user?.address_1 = editFirstAddress.text.toString()
        user?.address_2 = editSecondAddress.text.toString()
        user?.address_3 = editThirdAddress.text.toString()

        user?.phone_1 = editFirstPhone.text.toString()
        user?.phone_2 = editSecondPhone.text.toString()
        user?.phone_3 = editThirdPhone.text.toString()

        cacheManager!!.users().updateInRoom(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : ResourceCompletableObserver() {
                    override fun onComplete() {
                        Utils.messageOutput(context, "Сохранено")
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        bind.swipeRefreshLayout.isRefreshing = false
                        bind.progressUserProfile.visibility = View.GONE
                    }
                })

        api!!.updateProfile(userToken, user?.address_1,
                user?.address_2,
                user?.address_3,
                user?.phone_1,
                user?.phone_2,
                user?.phone_3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerLoadProfile)
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    companion object {
        private var fragment: FragmentUserProfile? = null

        val instance: FragmentUserProfile?
            get() {
                if (this.fragment == null) this.fragment = FragmentUserProfile()
                return this.fragment
            }
    }

    override fun initListenersActionBar(viewGroup: ViewGroup?) {
        abBtnBack?.setOnClickListener { ContainerFragments.getInstance(context).popBackStack() }
        abBtnSave?.setOnClickListener { updateUserData() }
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup?) {
        abBtnBack = viewGroup?.findViewById(R.id.itemBtnBack)
        abBtnSave = viewGroup?.findViewById(R.id.itemBtnSave)
        abUserLogin = viewGroup?.findViewById(R.id.itemUserLogin)
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_profile
    }
}