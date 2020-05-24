package com.application.arenda.mainWorkspace.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.application.arenda.BuildConfig
import com.application.arenda.R
import com.application.arenda.databinding.ActivityRegistrationBinding
import com.application.arenda.entities.serverApi.auth.ApiAuthentication
import com.application.arenda.entities.room.LocalCacheManager
import com.application.arenda.entities.user.AccountType
import com.application.arenda.entities.utils.Utils
import com.application.arenda.entities.utils.retrofit.CodeHandler
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*

class ActivityRegistration : AppCompatActivity() {
    private var currentUriUserLogo: Uri? = null
    private var api: ApiAuthentication? = null
    private var cacheManager: LocalCacheManager? = null
    private val disposable = CompositeDisposable()
    private lateinit var bindingUtil: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUtil = DataBindingUtil.setContentView(this, R.layout.activity_registration)

        init()
        initListeners()
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private fun init() {
        cacheManager = LocalCacheManager.getInstance(this)
        api = ApiAuthentication.getInstance(this)
        Utils.setPhoneMask(resources.getString(R.string.hint_phone), bindingUtil.fieldPhoneReg)
    }

    private fun initListeners() {
        bindingUtil.layoutSelectUserLogo.setOnClickListener { openAlertDialogChoosePicture() }

        bindingUtil.btnReg.setOnClickListener {
            if (!Utils.fieldIsEmpty(applicationContext,
                            bindingUtil.fieldNameReg,
                            bindingUtil.fieldLastNameReg,
                            bindingUtil.fieldLoginReg,
                            bindingUtil.fieldEmailReg,
                            bindingUtil.fieldPhoneReg,
                            bindingUtil.fieldPassReg) &&
                    Utils.isEmail(applicationContext, bindingUtil.fieldEmailReg) &&
                    Utils.textIsAlphabet(applicationContext,
                            bindingUtil.fieldNameReg,
                            bindingUtil.fieldLastNameReg) &&
                    !Utils.isWeakPassword(applicationContext, bindingUtil.fieldPassReg) &&
                    Utils.isConfirmPassword(applicationContext, bindingUtil.fieldPassReg, bindingUtil.fieldConfirmPassReg)) {
                bindingUtil.progressBarReg.visibility = View.VISIBLE
                api!!.registration(this,
                        currentUriUserLogo,
                        bindingUtil.fieldNameReg.text.toString().trim { it <= ' ' },
                        bindingUtil.fieldLastNameReg.text.toString().trim { it <= ' ' },
                        bindingUtil.fieldLoginReg.text.toString().trim { it <= ' ' },
                        bindingUtil.fieldEmailReg.text.toString().trim { it <= ' ' },
                        bindingUtil.fieldPassReg.text.toString().trim { it <= ' ' },
                        bindingUtil.fieldPhoneReg.text.toString().trim { it <= ' ' },
                        accountType)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(object : SingleObserver<CodeHandler> {
                            override fun onSubscribe(d: Disposable) {
                                disposable.add(d)
                            }

                            override fun onSuccess(code: CodeHandler) {
                                when (code) {
                                    CodeHandler.SUCCESS -> {
                                        bindingUtil.progressBarReg.visibility = View.INVISIBLE
                                        onBackPressed()
                                        finish()
                                    }
                                    CodeHandler.USER_WITH_LOGIN_EXISTS -> {
                                        bindingUtil.progressBarReg.visibility = View.INVISIBLE
                                        bindingUtil.fieldLoginReg.error = getString(R.string.error_user_login_exists)
                                    }
                                    CodeHandler.USER_EXISTS -> {
                                        bindingUtil.progressBarReg.visibility = View.INVISIBLE
                                        bindingUtil.fieldEmailReg.error = getString(R.string.error_user_exists)
                                    }
                                    CodeHandler.UNSUCCESS, CodeHandler.UNKNOW_ERROR, CodeHandler.NOT_CONNECT_TO_DB -> {
                                        bindingUtil.progressBarReg.visibility = View.INVISIBLE
                                        Utils.messageOutput(this@ActivityRegistration, getString(R.string.error_check_internet_connect))
                                    }
                                    else -> {
                                        bindingUtil.progressBarReg.visibility = View.INVISIBLE
                                        Utils.messageOutput(this@ActivityRegistration, getString(R.string.unknown_error))
                                    }
                                }
                            }

                            override fun onError(e: Throwable) {
                                Timber.e(e)
                                bindingUtil.progressBarReg.visibility = View.INVISIBLE
                                if (e is SocketTimeoutException || e is ConnectException) {
                                    Utils.messageOutput(this@ActivityRegistration, getString(R.string.error_check_internet_connect))
                                }
                            }
                        })
            }
        }
    }

    private fun openAlertDialogChoosePicture() {
        val builder = AlertDialog.Builder(this)
        val array = arrayOf(resources.getString(R.string.text_camera), resources.getString(R.string.text_galery))
        builder
                .setTitle(R.string.text_select_picture)
                .setItems(array) { _: DialogInterface?, which: Int ->
                    when (which) {
                        0 -> choosePicture(true)
                        1 -> choosePicture(false)
                    }
                }
                .create()
                .show()
    }

    private fun choosePicture(isCamera: Boolean) {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            if (isCamera) {
                                val pictureIntent = Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE)
                                if (pictureIntent.resolveActivity(packageManager) != null) {
                                    var photoFile: File? = null
                                    try {
                                        photoFile = createImageFile()
                                    } catch (ex: IOException) {
                                        Timber.e(ex)
                                    }
                                    if (photoFile != null) {
                                        val photoURI = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID + ".provider", photoFile)
                                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                        startActivityForResult(pictureIntent, CAMERA_REQUEST.toInt())
                                    }
                                }
                            } else {
                                Matisse.from(this@ActivityRegistration)
                                        .choose(MimeType.ofImage())
                                        .countable(true)
                                        .showSingleMediaType(true)
                                        .autoHideToolbarOnSingleTap(true)
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(GlideEngine())
                                        .showPreview(false) // Default is `true`
                                        .forResult(REQUEST_CODE_CHOOSE.toInt())
                            }
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(list: List<PermissionRequest>, permissionToken: PermissionToken) {
                        permissionToken.continuePermissionRequest()
                    }
                })
                .check()
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(Date())
        val imageFileName = "IMG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        currentUriUserLogo = Uri.fromFile(image)
        Timber.tag("CURRENT_URI").d(currentUriUserLogo.toString())
        return image
    }

    private val accountType: AccountType
        get() {
            when (bindingUtil.radioGroupReg.checkedRadioButtonId) {
                R.id.radioBtnPrivatePerson -> return AccountType.PRIVATE_PERSON
                R.id.radioBtnBusiness -> return AccountType.BUSINESS_PERSON
            }
            return AccountType.PRIVATE_PERSON
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE.toInt() && resultCode == Activity.RESULT_OK) {
            CropImage.activity(Matisse.obtainResult(data)[0])
                    .start(this)
        } else if (requestCode == CAMERA_REQUEST.toInt() && resultCode == Activity.RESULT_OK) {
            try {
                CropImage.activity(currentUriUserLogo)
                        .start(this)
            } catch (e: Throwable) {
                Timber.e(e)
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                currentUriUserLogo = result.uri
                bindingUtil.itemSelectUserLogo.setImageURI(result.uri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Timber.e(result.error)
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_CHOOSE: Short = 901
        private const val CAMERA_REQUEST: Short = 902
    }
}