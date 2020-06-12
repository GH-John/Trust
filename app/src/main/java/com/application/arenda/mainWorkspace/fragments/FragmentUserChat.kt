package com.application.arenda.mainWorkspace.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.arenda.BuildConfig
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserChatBinding
import com.application.arenda.entities.chat.MessageAdapter
import com.application.arenda.entities.models.ModelMessage
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.recyclerView.RVOnScrollListener
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.OnApiListener
import com.application.arenda.entities.serverApi.chat.*
import com.application.arenda.entities.serverApi.chat.TypeMessage.CHAT_PARTNER
import com.application.arenda.entities.serverApi.client.ApiClient
import com.application.arenda.entities.serverApi.client.CodeHandler
import com.application.arenda.entities.serverApi.client.CodeHandler.*
import com.application.arenda.entities.serverApi.user.ApiUser
import com.application.arenda.entities.utils.Utils
import com.application.arenda.entities.utils.glide.GlideUtils
import com.application.arenda.ui.widgets.actionBar.AdapterActionBar
import com.application.arenda.ui.widgets.containerFragments.ContainerFragments
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import timber.log.Timber

class FragmentUserChat : Fragment(), AdapterActionBar {
    private var bind: FragmentUserChatBinding? = null
    private var socket: Socket? = null
    private var rvAdapter: MessageAdapter? = null

    private var itemBtnBack: ImageButton? = null
    private var userChatAvatar: ImageView? = null
    private var userChatLogin: TextView? = null
    private var userChatStatus: TextView? = null
    private var itemMore: ImageButton? = null

    private var singleLoaderWithRewriteChat: SingleObserver<List<ModelMessage>>? = null
    private var singleLoaderWithoutRewriteChat: SingleObserver<List<ModelMessage>>? = null

    private var listenerLoadChat: OnApiListener? = null
    private var rvOnScrollListener: RVOnScrollListener? = null

    private var containerFragments: ContainerFragments? = null
    private var consumerUserProfile: Consumer<List<ModelUser>>? = null
    private var cacheManager: LocalCacheManager? = null
    private var sharedViewModels: SharedViewModels? = null
    private var userToken: String? = null
    private var idUser_To: Long? = null

    private var originalInputMode: Int? = null

    private var response: ResponseConnect? = null
    private var disposable = CompositeDisposable()

    private var api: ApiChat? = null
    private var apiUser: ApiUser? = null

    private var isTyping = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserChatBinding.inflate(inflater)

        init()
        initInterfaces()
        initAdapters()
        listeners()

        return bind!!.root
    }

    private fun saveInputMode() {
        originalInputMode = activity?.window?.attributes?.softInputMode
    }

    private fun setNeedInputMode(mode: Int) {
        activity?.window?.setSoftInputMode(mode)
    }

    private fun init() {
        api = ApiChat.getInstance(context)
        apiUser = ApiUser.getInstance(context)
        containerFragments = ContainerFragments.getInstance(context)
        rvAdapter = MessageAdapter()

        bind!!.rvUserChat.adapter = rvAdapter
//        socket = ApiChat.getSocket()
        socket = IO.socket(BuildConfig.URL_CHAT_SOCKET)
        cacheManager = LocalCacheManager.getInstance(context)
        sharedViewModels = ViewModelProvider(requireActivity()).get(SharedViewModels::class.java)
    }

    @SuppressLint("CheckResult")
    private fun initInterfaces() {
        consumerUserProfile = Consumer { modelUsers: List<ModelUser> ->
            if (modelUsers.isNotEmpty()) {
                userToken = modelUsers[0].token

                sharedViewModels!!.selectedUser.observe(viewLifecycleOwner, Observer { idUser ->
                    idUser_To = idUser

                    idUser_To?.let { it -> addMessagesToChat(it, 0, 0, true) }

                    initSocket(userToken!!, idUser_To!!)
                })
            } else {
                userToken = null

                Utils.messageOutput(context, getString(R.string.warning_login_required))
            }
        }

        disposable.add(cacheManager!!.users()
                .activeUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserProfile))

        listenerLoadChat = object : OnApiListener {
            override fun onComplete(code: CodeHandler) {
                when (code) {
                    NONE_REZULT -> {
                        rvAdapter?.isLoading = false

                        bind!!.progressBarChat.visibility = GONE
                    }
                    RECIPIENT_NOT_FOUND -> Utils.messageOutput(context, getString(R.string.error_recipient_not_found))
                    CHAT_NOT_FOUND -> Utils.messageOutput(context, getString(R.string.error_chat_connect))
                    USER_NOT_FOUND -> Utils.messageOutput(context, resources.getString(R.string.warning_login_required))
                    UNKNOW_ERROR, UNSUCCESS, NOT_CONNECT_TO_DB, HTTP_NOT_FOUND, NETWORK_ERROR -> {
                        Utils.messageOutput(context, resources.getString(R.string.error_check_internet_connect))
                    }
                }
            }

            override fun onFailure(t: Throwable) {
                Timber.e(t)
            }
        }

        singleLoaderWithRewriteChat = object : SingleObserver<List<ModelMessage>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelMessage>) {
                rvAdapter?.rewriteCollection(collection)
                bind!!.progressBarChat.visibility = GONE
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter?.isLoading = false
                bind!!.progressBarChat.visibility = GONE
            }
        }

        singleLoaderWithoutRewriteChat = object : SingleObserver<List<ModelMessage>> {
            override fun onSubscribe(d: Disposable) {
                disposable.add(d)
            }

            override fun onSuccess(collection: List<ModelMessage>) {
                rvAdapter?.addToCollection(collection)
                bind!!.progressBarChat.visibility = GONE
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                rvAdapter?.isLoading = false
                bind!!.progressBarChat.visibility = GONE
            }
        }
    }


    @SuppressLint("CheckResult")
    private fun initAdapters() {
        bind!!.rvUserChat.setItemViewCacheSize(50)
        bind!!.rvUserChat.setHasFixedSize(true)

        rvOnScrollListener = RVOnScrollListener(bind!!.rvUserChat.layoutManager as LinearLayoutManager?)
        bind!!.rvUserChat.addOnScrollListener(rvOnScrollListener!!)
        rvAdapter = MessageAdapter()
        rvOnScrollListener?.setRVAdapter(rvAdapter)
        bind!!.rvUserChat.adapter = rvAdapter
    }

    private fun setLoadMoreForChat() {
        rvOnScrollListener!!.setOnLoadMoreData { lastID: Long -> idUser_To?.let { addMessagesToChat(it, lastID, 0, false) } }
    }

    @Synchronized
    private fun addMessagesToChat(idUser_To: Long, idMessageAfter: Long, idMessageBefore: Long, rewrite: Boolean) {
        if (!rvAdapter!!.isLoading) {
            rvAdapter?.isLoading = true
            bind!!.progressBarChat.visibility = VISIBLE
            api!!.loadChat(userToken, idUser_To, idMessageAfter, idMessageBefore, 10, listenerLoadChat!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(if (rewrite) singleLoaderWithRewriteChat!! else singleLoaderWithoutRewriteChat!!)
        } else {
            bind!!.progressBarChat.visibility = GONE
        }
    }

    @SuppressLint("CheckResult")
    private fun listeners() {
        setLoadMoreForChat()

        bind!!.btnSendMessage.setOnClickListener { sendMessage() }

        bind!!.chatMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length!! > 0) {
                    bind!!.btnSendMessage.visibility = VISIBLE
                } else {
                    bind!!.btnSendMessage.visibility = GONE
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun initSocket(userToken: String, idUser_To: Long) {
        try {
            Timber.d("Socket - %s", socket?.id())

        } catch (e: Exception) {
            Timber.e(e)
        }

        socket?.connect()
        socket?.on(Socket.EVENT_CONNECT, getConnectListener(userToken, idUser_To))
        socket?.on("connected", getOnConnectListener())
        socket?.on("onError", onErrorChat)

        socket?.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
    }

    private fun getConnectListener(userToken: String, idUser_To: Long): Emitter.Listener {
        return Emitter.Listener {
            val jsonData = ApiClient.getGson().toJson(ConnectToRoom(userToken, idUser_To))
            socket?.emit("connectToRoom", jsonData)
        }
    }

    private fun getOnConnectListener(): Emitter.Listener {
        return Emitter.Listener {
            response = ApiClient.getGson().fromJson(it[0].toString(), ResponseConnect::class.java)
            Timber.d("Success connected to room %s", response?.idChat)

            socket?.on("updateChat", onUpdateChat)
            socket?.on("sendMessageResponse", onSendMessageResponse)
//            socket?.on("userLeftChatRoom", onUserLeft)
        }
    }

    private var onUpdateChat = Emitter.Listener {

        val modelChat: ModelMessage = ApiClient.getGson().fromJson(it[0].toString(), ModelMessage::class.java)
        modelChat.type = CHAT_PARTNER

        Single.just(modelChat)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Consumer { showMessageToUI(modelChat) })
    }

    private var onSendMessageResponse = Emitter.Listener {
        val response: SendMessageResponse = ApiClient.getGson().fromJson(it[0].toString(), SendMessageResponse::class.java)

        if (response.codeHandler == SUCCESS) {
            showMessageToUI(ModelMessage(response.idMessage, TypeMessage.CHAT_MINE, response.message))
        } else {
            Timber.e("%s - %s", response.codeHandler?.name, response.error)
            Utils.messageOutput(context, getString(R.string.error_send_message) + " " + response.codeHandler?.name + " - " + response.error)
        }
    }

    private var onErrorChat = Emitter.Listener {
        val error: ChatError = ApiClient.getGson().fromJson(it[0].toString(), ChatError::class.java)

        when (error.codeHandler) {
            FAILED_CREATE_ROOM -> Utils.messageOutput(context, getString(R.string.error_chat_connect))
            USER_NOT_FOUND -> Utils.messageOutput(context, getString(R.string.warning_login_required))
        }
        Timber.e("%s - %s", error.codeHandler?.name, error.error)
        Utils.messageOutput(context, getString(R.string.error_send_message) + " " + error.codeHandler?.name)
    }

    private var onConnectError = Emitter.Listener {
        Utils.messageOutput(context, getString(R.string.error_chat_connect))
    }

//    private var onUserLeft = Emitter.Listener {
//        val leftUserName = it[0] as String
//        Utils.messageOutput(context, "User $leftUserName left from chat")
//    }

//    private var onError = Emitter.Listener {
//        val data = initialData(userName, roomName)
//        val jsonData = gson.toJson(data)
//        socket?.emit("error", jsonData)
//
//    }

//    private var onNewUser = Emitter.Listener {
//        val name = it[0] as String //This pass the userName!
//        val chat = ModelChat(ModelChat.Type.USER_JOIN.type, "", name, "testRoom")
//        messageAdapter?.addToCollection(chat)
//    }

    private fun sendMessage() {
        if (userToken == null) {
            Utils.messageOutput(context, getString(R.string.warning_login_required))
            return
        }

        val message: String = bind!!.chatMessage.text.toString()
        val sendData = idUser_To?.let { response?.idChat?.let { it1 -> SendMessage(userToken, it, it1, message) } }
        val jsonData = ApiClient.getGson().toJson(sendData)
        socket?.emit("sendedMessage", jsonData)

        bind!!.chatMessage.setText("")
    }

    private fun showMessageToUI(model: ModelMessage) {
        rvAdapter?.addToCollection(model)

        rvAdapter?.itemCount?.let { bind!!.rvUserChat.smoothScrollToPosition(it) }
    }

    override fun onStart() {
        super.onStart()
        saveInputMode()
        setNeedInputMode(SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onPause() {
        originalInputMode?.let { setNeedInputMode(it) }
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        socket!!.disconnect()
        socket!!.emit("unsubscribe", response)
        socket!!.off()
    }

    override fun initListenersActionBar(viewGroup: ViewGroup?) {
        itemBtnBack?.setOnClickListener { requireActivity().onBackPressed() }
        itemMore?.setOnClickListener { Utils.messageOutput(context, "В разработке") }
        userChatAvatar?.setOnClickListener { FragmentViewerUserProfile.instance?.let { it1 -> containerFragments?.open(it1) } }
    }

    override fun initComponentsActionBar(viewGroup: ViewGroup?) {
        itemBtnBack = viewGroup?.findViewById(R.id.itemBtnBack)
        itemMore = viewGroup?.findViewById(R.id.itemMore)
        userChatAvatar = viewGroup?.findViewById(R.id.userChatAvatar)
        userChatLogin = viewGroup?.findViewById(R.id.userChatLogin)
        userChatStatus = viewGroup?.findViewById(R.id.userChatStatus)

        sharedViewModels!!.selectedUser.observe(viewLifecycleOwner, Observer { idUser ->
            run {
                disposable.add(apiUser!!.loadProfileToView(userToken, idUser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { data ->
                            run {
                                when (data.handler) {
                                    SUCCESS -> {
                                        GlideUtils.loadAvatar(context, data.response.avatar, userChatAvatar)
                                        userChatLogin?.text = data.response.login
                                    }
                                    USER_NOT_FOUND -> Utils.messageOutput(context, getString(R.string.error_user_not_found))
                                    UNKNOW_ERROR -> Utils.messageOutput(context, getString(R.string.unknown_error))
                                }
                            }
                        })
            }
        })
    }

    override fun getIdPatternResource(): Int {
        return R.layout.ab_pattern_user_chat
    }
}