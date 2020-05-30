package com.application.arenda.mainWorkspace.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.application.arenda.R
import com.application.arenda.databinding.ActivityAuthorizationBinding
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.CodeHandler.*
import com.application.arenda.entities.serverApi.user.ApiUser
import com.application.arenda.entities.utils.Utils
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException

class ActivityAuthorization : AppCompatActivity() {
    private var api: ApiUser? = null
    private val disposable = CompositeDisposable()

    private lateinit var bind: ActivityAuthorizationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(bind.root)

        api = ApiUser.getInstance(this)

        initListeners()
    }

    private fun initListeners() {
        bind.itemForgotPass.setOnClickListener { Utils.messageOutput(this, "In process developing") }
        bind.btnRegAuth.setOnClickListener { startActivity(Intent(this@ActivityAuthorization, ActivityRegistration::class.java)) }
        bind.btnSignAuth.setOnClickListener {
            if (!Utils.fieldIsEmpty(applicationContext, bind.fieldEmailAuth, bind.fieldPassAuth)) {
                bind.progressBarAuth.visibility = View.VISIBLE
                api!!.authorization(this,
                        bind.fieldEmailAuth.text.toString().trim { it <= ' ' },
                        bind.fieldPassAuth.text.toString().trim { it <= ' ' })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<CodeHandler> {
                            override fun onSubscribe(d: Disposable) {
                                disposable.add(d)
                            }

                            override fun onSuccess(code: CodeHandler) {
                                when (code) {
                                    SUCCESS -> {
                                        bind.progressBarAuth.visibility = View.GONE
                                        onBackPressed()
                                        finish()
                                    }
                                    WRONG_EMAIL_LOGIN -> {
                                        bind.progressBarAuth.visibility = View.GONE
                                        bind.fieldEmailAuth.error = getString(R.string.error_user_not_found)
                                    }
                                    WRONG_PASSWORD -> {
                                        bind.progressBarAuth.visibility = View.GONE
                                        bind.fieldPassAuth.error = getString(R.string.error_wrong_password)
                                    }
                                    else -> {
                                        bind.progressBarAuth.visibility = View.GONE
                                        Utils.messageOutput(this@ActivityAuthorization, getString(R.string.unknown_error))
                                    }
                                }
                            }

                            override fun onError(e: Throwable) {
                                Timber.e(e)
                                bind.progressBarAuth.visibility = View.GONE
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