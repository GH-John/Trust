package com.application.arenda.mainWorkspace.fragments

import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.arenda.R
import com.application.arenda.databinding.FragmentUserChatBinding
import com.application.arenda.entities.chat.MessageAdapter
import com.application.arenda.entities.models.ModelMessage
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.chat.ConnectToRoom
import com.application.arenda.entities.serverApi.chat.ResponseConnect
import com.application.arenda.entities.serverApi.chat.SendMessage
import com.application.arenda.entities.serverApi.client.ApiClient
import com.application.arenda.entities.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import timber.log.Timber

class FragmentUserChat : Fragment() {
    private var bind: FragmentUserChatBinding? = null
    private var socket: Socket? = null
    private var messageAdapter: MessageAdapter? = null

    private var consumerUserProfile: Consumer<List<ModelUser>>? = null
    private var cacheManager: LocalCacheManager? = null
    private var sharedViewModels: SharedViewModels? = null
    private var userToken: String? = null
    private var idUser_To: Long? = null

    private var originalInputMode: Int? = null

    var responseConnect: ResponseConnect? = null
    private var disposable = CompositeDisposable()

    private var isTyping = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserChatBinding.inflate(inflater)

        init()
        listeners()

        return bind!!.root
    }

    private fun saveInputMode() {
        originalInputMode = activity?.window?.attributes?.softInputMode
    }

    private fun restorInputMode(){

    }

    private fun setNeedInputMode(mode: Int) {
        activity?.window?.setSoftInputMode(mode)
    }

    private fun init() {
//        var message = ModelMessage(1, CodeHandler.UNKNOW_ERROR, "message", null)
//        var json = ApiClient.getGson().toJson(message)

//        var json = "{'ID':0,'codeHandler':'-2000','message':'message','type':1}";
//
//        var model = ApiClient.getGson().fromJson(json, ModelMessage::class.java)
//
//        Timber.d("JSON convert : %s", model)


        messageAdapter = MessageAdapter()
        bind!!.rvUserChat.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        bind!!.rvUserChat.adapter = messageAdapter
//        socket = ApiChat.getSocket()
        socket = IO.socket("http://192.168.43.241:3000")
        cacheManager = LocalCacheManager.getInstance(context)
        sharedViewModels = ViewModelProvider(requireActivity()).get(SharedViewModels::class.java)

        consumerUserProfile = Consumer { modelUsers: List<ModelUser> ->
            if (modelUsers.isNotEmpty()) {
                userToken = modelUsers[0].token

                sharedViewModels!!.selectedUser.observe(viewLifecycleOwner, Observer { idUser ->
                    idUser_To = idUser

                    Utils.messageOutput(context, idUser_To.toString())

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
    }

    private fun listeners() {
        bind!!.btnSendMessage.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        if (userToken == null) {
            Utils.messageOutput(context, getString(R.string.warning_login_required))
            return
        }

        val message: String = bind!!.chatMessage.text.toString()
        val sendData = idUser_To?.let { responseConnect?.idRoom?.let { it1 -> SendMessage(userToken, it, it1, message) } }
        val jsonData = ApiClient.getGson().toJson(sendData)
        socket?.emit("sendedMessage", jsonData)

        val model = ModelMessage(ModelMessage.Type.CHAT_MINE.type, message)

        bind!!.chatMessage.setText("")

        messageAdapter?.addToCollection(model)

        messageAdapter?.itemCount?.let { bind!!.rvUserChat.smoothScrollToPosition(it) }
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
        socket?.on("USER_NOT_FOUND", getUserNotFoundListener())

//        socket?.on(Socket.EVENT_CONNECT_ERROR, onError)

//        socket!!.io().on(Manager.EVENT_TRANSPORT) { args: Array<Any> ->
//            val transport = args[0] as Transport
//            transport.on(Transport.EVENT_ERROR) { args1: Array<Any?> -> Timber.e(args1[0] as Exception?) }
//        }
    }

    private fun getConnectListener(userToken: String, idUser_To: Long): Emitter.Listener {
        return Emitter.Listener {
            val jsonData = ApiClient.getGson().toJson(ConnectToRoom(userToken, idUser_To))
            socket?.emit("connectToRoom", jsonData)
        }
    }

    private fun getOnConnectListener(): Emitter.Listener {
        return Emitter.Listener {
            responseConnect = ApiClient.getGson().fromJson(it[0].toString(), ResponseConnect::class.java)
            Timber.d("Success connected to room %s", responseConnect?.idRoom)

            socket?.on("updateChat", onUpdateChat)
            socket?.on("sendMessageError", onSendMessageError)
//            socket?.on("userLeftChatRoom", onUserLeft)
        }
    }

    private fun getUserNotFoundListener(): Emitter.Listener {
        return Emitter.Listener {
            Timber.e("User not found")
            Utils.messageOutput(context, getString(R.string.warning_login_required))
        }
    }

    private var onUpdateChat = Emitter.Listener {
        val message: ModelMessage = ApiClient.getGson().fromJson(it[0].toString(), ModelMessage::class.java)
        message.type = ModelMessage.Type.CHAT_PARTNER.type
        messageAdapter?.addToCollection(message)
    }

    private var onSendMessageError = Emitter.Listener {
        val message: ModelMessage = ApiClient.getGson().fromJson(it[0].toString(), ModelMessage::class.java)

        Timber.e("%s - %s", message.codeHandler?.name, message.error)
        Utils.messageOutput(context, getString(R.string.error_send_message) + " " + message.codeHandler?.name)
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
//        val chat = ModelMessage(ModelMessage.Type.USER_JOIN.type, "", name, "testRoom")
//        messageAdapter?.addToCollection(chat)
//    }

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
        socket!!.off()
    }
}