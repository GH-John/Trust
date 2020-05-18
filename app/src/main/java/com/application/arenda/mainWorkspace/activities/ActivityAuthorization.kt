package com.application.arenda.mainWorkspace.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.application.arenda.R
import com.application.arenda.databinding.ActivityAuthorizationBinding
import com.application.arenda.entities.authentication.ApiAuthentication
import com.application.arenda.entities.utils.Utils
import com.application.arenda.entities.utils.retrofit.CodeHandler
import com.application.arenda.entities.utils.retrofit.CodeHandler.*
import com.application.arenda.ui.ComponentBackground
import com.application.arenda.ui.SetDrawableImageViews
import com.application.arenda.ui.style.SetBtnStyle
import com.application.arenda.ui.style.SetFieldStyle
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException

class ActivityAuthorization : AppCompatActivity() {
    private var api: ApiAuthentication? = null
    private val disposable = CompositeDisposable()

    private lateinit var bindingUtil: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_authorization)

        api = ApiAuthentication.getInstance()

        initStyles()
        initListeners()
    }

    private fun initStyles() {
        SetDrawableImageViews.setStyle(ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 3f, 3f, 0f, 20f, 20f, 0f), bindingUtil.imagePinAuth)
        SetFieldStyle.setEditTextBackground(ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), bindingUtil.fieldEmailAuth, bindingUtil.fieldPassAuth)
        SetBtnStyle.setStyle(ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 0f, 3f, 20f), bindingUtil.btnRegAuth)
        SetBtnStyle.setStyle(ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), bindingUtil.btnSignAuth)
    }

    private fun initListeners() {
        bindingUtil.itemForgotPass.setOnClickListener { Utils.messageOutput(this, "In process developing") }
        bindingUtil.btnRegAuth.setOnClickListener { startActivity(Intent(this@ActivityAuthorization, ActivityRegistration::class.java)) }
        bindingUtil.btnSignAuth.setOnClickListener {
            if (!Utils.fieldIsEmpty(applicationContext, bindingUtil.fieldEmailAuth, bindingUtil.fieldPassAuth)) {
                bindingUtil.progressBarAuth.visibility = View.VISIBLE
                api!!.authorization(this,
                        bindingUtil.fieldEmailAuth.text.toString().trim { it <= ' ' },
                        bindingUtil.fieldPassAuth.text.toString().trim { it <= ' ' })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<CodeHandler> {
                            override fun onSubscribe(d: Disposable) {
                                disposable.add(d)
                            }

                            override fun onSuccess(code: CodeHandler) {
                                Timber.tag("Authorize").d(code.name)

                                when (code) {
                                    SUCCESS -> {
                                        bindingUtil.progressBarAuth.visibility = View.GONE
                                        onBackPressed()
                                        finish()
                                    }
                                    WRONG_EMAIL_LOGIN -> {
                                        bindingUtil.progressBarAuth.visibility = View.GONE
                                        bindingUtil.fieldEmailAuth.error = getString(R.string.error_user_not_found)
                                    }
                                    WRONG_PASSWORD -> {
                                        bindingUtil.progressBarAuth.visibility = View.GONE
                                        bindingUtil.progressBarAuth.visibility = View.GONE
                                        Utils.messageOutput(this@ActivityAuthorization, getString(R.string.error_check_internet_connect))
                                    }
                                    else -> {
                                        bindingUtil.progressBarAuth.visibility = View.GONE
                                        Utils.messageOutput(this@ActivityAuthorization, getString(R.string.unknown_error))
                                    }
                                }
                            }

                            override fun onError(e: Throwable) {
                                Timber.e(e)
                                bindingUtil.progressBarAuth.visibility = View.GONE
                                if (e is SocketTimeoutException || e is ConnectException) {
                                    Utils.messageOutput(this@ActivityAuthorization, getString(R.string.error_check_internet_connect))
                                }
                            }
                        })
            }
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }
}