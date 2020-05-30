package com.application.arenda.mainWorkspace.activities

import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.application.arenda.databinding.ActivityMainBinding
import com.application.arenda.mainWorkspace.fragments.FragmentAllAnnouncements
import com.application.arenda.ui.widgets.ComponentManager
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import com.application.arenda.ui.widgets.sideBar.ContainerDrawerLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import timber.log.Timber


@ContainerOptions(CacheImplementation.SPARSE_ARRAY)
class ActivityMain : AppCompatActivity() {
    private var containerFragments: ContainerFragments? = null
    private var containerDrawerLayout: ContainerDrawerLayout? = null
    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        init()
        getFireUserToken()
    }

    private fun init() {
        containerFragments = ContainerFragments.getInstance(this)
        containerDrawerLayout = ContainerDrawerLayout.getInstance(this)
        ComponentManager.addLink(containerFragments, bind.customBottomNavigation, bind.customActionBar, containerDrawerLayout)
        containerFragments?.open(FragmentAllAnnouncements.getInstance())
    }

    private fun getFireUserToken() {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(object : OnCompleteListener<InstanceIdResult?> {
                    override fun onComplete(@NonNull task: Task<InstanceIdResult?>) {
                        if (!task.isSuccessful) {
                            Timber.e(task.exception)
                            return
                        }

                        Timber.d("Token from activity - %s", task.result?.token)
                    }
                })
    }
}