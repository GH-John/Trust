package com.application.arenda.mainWorkspace.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.application.arenda.databinding.FragmentUserChatBinding
import com.application.arenda.entities.chat.MessageAdapter
import com.application.arenda.entities.models.ModelMessage
import com.application.arenda.entities.models.ModelUser
import com.application.arenda.entities.models.SharedViewModels
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.serverApi.chat.ApiChat
import com.application.arenda.entities.serverApi.chat.ConnectToRoom
import com.application.arenda.entities.serverApi.chat.ResponseConnect
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.Transport
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
    private var disposable = CompositeDisposable()

    private val gson = Gson()
    private var isTyping = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        bind = FragmentUserChatBinding.inflate(inflater)

        init();

        return bind!!.root
    }

    private fun init() {
        cacheManager = LocalCacheManager.getInstance(context)
        sharedViewModels = ViewModelProvider(requireActivity()).get(SharedViewModels::class.java)

        consumerUserProfile = Consumer { modelUsers: List<ModelUser> ->
            if (modelUsers.isNotEmpty()) {
                userToken = modelUsers[0].token
            }
        }

        sharedViewModels!!.selectedUser.observe(viewLifecycleOwner, Observer { idUser ->
            idUser_To = idUser
        })

        disposable.add(cacheManager!!.users()
                .activeUser
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumerUserProfile))
    }

    private fun initSocket() {
        messageAdapter = MessageAdapter()
        bind!!.rvUserChat.adapter = messageAdapter
        socket = ApiChat.getSocket()

        socket?.on(Socket.EVENT_CONNECT, onConnect)
        socket?.on("updateChat", onUpdateChat)
        socket?.on("userLeftChatRoom", onUserLeft)
        socket?.on("connected", onConnected)
        socket?.connect()

//        socket?.on(Socket.EVENT_CONNECT_ERROR, onError)

        socket!!.io().on(Manager.EVENT_TRANSPORT) { args: Array<Any> ->
            val transport = args[0] as Transport
            transport.on(Transport.EVENT_ERROR) { args1: Array<Any?> -> Timber.e(args1[0] as Exception?) }
        }
    }

    private var onUserLeft = Emitter.Listener {
        val leftUserName = it[0] as String
        val chat: ModelMessage = ModelMessage()
        chat.type = ModelMessage.Type.USER_LEAVE.type
        chat.userName = leftUserName

        messageAdapter?.addToCollection(chat)
    }

    private var onUpdateChat = Emitter.Listener {
        val chat: ModelMessage = gson.fromJson(it[0].toString(), ModelMessage::class.java)
        chat.type = ModelMessage.Type.CHAT_PARTNER.type
        messageAdapter?.addToCollection(chat)
    }

    private var onConnect = Emitter.Listener {
        val jsonData = gson.toJson(idUser_To?.let { it1 -> ConnectToRoom(userToken, it1) })
        socket?.emit("connectToRoom", jsonData)
    }

    private var onConnected = Emitter.Listener {
        val response: ResponseConnect = gson.fromJson(it[0].toString(), ResponseConnect::class.java)
        Timber.d("Success connected to room %s", response.idRoom)
    }

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

    override fun onDestroy() {
        super.onDestroy()
        socket!!.disconnect()
    }
}