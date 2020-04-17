package com.application.arenda.MainWorkspace.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.application.arenda.BuildConfig;
import com.application.arenda.Entities.Authentication.ApiAuthentication;
import com.application.arenda.Entities.Authentication.Authentication;
import com.application.arenda.Entities.Authentication.OnAuthenticationListener;
import com.application.arenda.Entities.User.AccountType;
import com.application.arenda.Entities.Utils.Utils;
import com.application.arenda.R;
import com.application.arenda.UI.ComponentBackground;
import com.application.arenda.UI.SetDrawableImageViews;
import com.application.arenda.UI.Style.SetBtnStyle;
import com.application.arenda.UI.Style.SetFieldStyle;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class ActivityRegistration extends AppCompatActivity {
    private static final short REQUEST_CODE_CHOOSE = 901;
    private static final short CAMERA_REQUEST = 902;

    private ProgressBar progressBarReg;
    private RadioGroup radioGroup;
    private CircleImageView itemSelectUserLogo;
    private ImageView imagePinReg;
    private EditText
            fieldNameReg,
            fieldLastNameReg,
            fieldLoginReg,
            fieldEmailReg,
            fieldPassReg,
            fieldConfirmPassReg,
            fieldPhoneReg;

    private Button btnReg;

    private Uri currentUriFromCamera;

    private Authentication authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initComponents();
        initStyles();
        initListeners();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void initComponents() {

        radioGroup = findViewById(R.id.radioGroup);

        imagePinReg = findViewById(R.id.imagePinReg);

        itemSelectUserLogo = findViewById(R.id.itemSelectUserLogo);

        progressBarReg = findViewById(R.id.progressBarReg);

        fieldNameReg = findViewById(R.id.fieldNameReg);
        fieldLastNameReg = findViewById(R.id.fieldLastNameReg);

        fieldLoginReg = findViewById(R.id.fieldLoginReg);
        fieldEmailReg = findViewById(R.id.fieldEmailReg);

        fieldPassReg = findViewById(R.id.fieldPassReg);
        fieldConfirmPassReg = findViewById(R.id.fieldConfirmPassReg);

        fieldPhoneReg = findViewById(R.id.fieldPhoneReg);

        btnReg = findViewById(R.id.btnReg);

        authentication = Authentication.getInstance();

        Utils.setPhoneMask(getResources().getString(R.string.hint_phone), fieldPhoneReg);
    }

    private void initStyles() {
        SetDrawableImageViews.setStyle(new ComponentBackground(this, R.color.colorWhite,
                R.color.shadowColor, 6f, 3f, 3f, 0f, 20f, 20f, 0f), imagePinReg);

        SetFieldStyle.setEditTextBackground(new ComponentBackground(this, R.color.colorWhite,
                        R.color.shadowColor, 6f, 0f, 3f, 20f), fieldNameReg,
                fieldLastNameReg,
                fieldLoginReg,
                fieldEmailReg,
                fieldPassReg,
                fieldConfirmPassReg,
                fieldPhoneReg);

        SetBtnStyle.setStyle(new ComponentBackground(this, R.color.colorAccent,
                R.color.shadowColor, 6f, 0f, 3f, 20f), btnReg);
    }

    private void initListeners() {
        authentication.setOnAuthenticationListener(new OnAuthenticationListener() {
            @Override
            public void onComplete(@NonNull ApiAuthentication.AuthenticationCodes code) {
                switch (code) {
                    case USER_WITH_LOGIN_EXISTS:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        fieldLoginReg.setError(getString(R.string.error_user_login_exists));
                        break;
                    case USER_EXISTS:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        fieldEmailReg.setError(getString(R.string.error_user_exists));
                        break;
                    case USER_SUCCESS_REGISTERED:
                        progressBarReg.setVisibility(View.INVISIBLE);

                        onBackPressed();
                        finish();
                        break;

                    case USER_UNSUCCESS_REGISTERED:
                    case UNKNOW_ERROR:
                    case NETWORK_ERROR:
                    case NOT_CONNECT_TO_DB:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        Utils.messageOutput(ActivityRegistration.this, getString(R.string.error_check_internet_connect));
                        break;

                    default:
                        progressBarReg.setVisibility(View.INVISIBLE);
                        Utils.messageOutput(ActivityRegistration.this, getString(R.string.unknown_error));
                        break;
                }
            }

            @Override
            public void onFailure(@NonNull Throwable t) {
                Timber.e(t);
                progressBarReg.setVisibility(View.INVISIBLE);
                if (t instanceof SocketTimeoutException || t instanceof ConnectException) {
                    Utils.messageOutput(ActivityRegistration.this, getString(R.string.error_check_internet_connect));
                }
            }
        });

        itemSelectUserLogo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            String[] array = {getResources().getString(R.string.text_camera), getResources().getString(R.string.text_galery)};

            builder
                    .setTitle(R.string.text_select_picture)
                    .setItems(array, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                choosePicture(true);
                                break;

                            case 1:
                                choosePicture(false);
                        }
                    })
                    .create()
                    .show();
        });

        btnReg.setOnClickListener(v -> {
            if (!Utils.fieldIsEmpty(getApplicationContext(),
                    fieldNameReg,
                    fieldLastNameReg,
                    fieldLoginReg,
                    fieldEmailReg,
                    fieldPhoneReg,
                    fieldPassReg) &&

                    Utils.isEmail(getApplicationContext(), fieldEmailReg) &&

                    Utils.textIsAlphabet(getApplicationContext(),
                            fieldNameReg,
                            fieldLastNameReg) &&

                    !Utils.isWeakPassword(getApplicationContext(), fieldPassReg) &&

                    Utils.isConfirmPassword(getApplicationContext(), fieldPassReg, fieldConfirmPassReg)) {
                progressBarReg.setVisibility(View.VISIBLE);

                authentication.registration(this,
                        fieldNameReg.getText().toString().trim(),
                        fieldLastNameReg.getText().toString().trim(),
                        fieldLoginReg.getText().toString().trim(),
                        fieldEmailReg.getText().toString().trim(),
                        fieldPassReg.getText().toString().trim(),
                        fieldPhoneReg.getText().toString().trim(),
                        getAccountType());
            }
        });
    }

    private void choosePicture(boolean isCamera) {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {

                            if (isCamera) {
                                Intent pictureIntent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (pictureIntent.resolveActivity(getPackageManager()) != null) {

                                    File photoFile = null;
                                    try {
                                        photoFile = createImageFile();
                                    } catch (IOException ex) {
                                        Timber.e(ex);
                                    }

                                    if (photoFile != null) {
                                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
                                        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                                        startActivityForResult(pictureIntent, CAMERA_REQUEST);
                                    }
                                }
                            } else {
                                Matisse.from(ActivityRegistration.this)
                                        .choose(MimeType.ofImage())
                                        .countable(true)
                                        .showSingleMediaType(true)
                                        .autoHideToolbarOnSingleTap(true)
                                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                        .thumbnailScale(0.85f)
                                        .imageEngine(new GlideEngine())
                                        .showPreview(false) // Default is `true`
                                        .forResult(REQUEST_CODE_CHOOSE);
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        if (permissionToken != null)
                            permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());

        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        currentUriFromCamera = Uri.fromFile(image);
        return image;
    }

    private AccountType getAccountType() {
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioBtnPrivatePerson:
                return AccountType.PRIVATE_PERSON;

            case R.id.radioBtnBusiness:
                return AccountType.BUSINESS_PERSON;
        }

        return AccountType.PRIVATE_PERSON;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {

            CropImage.activity(Matisse.obtainResult(data).get(0))
                    .start(this);

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                CropImage.activity(currentUriFromCamera)
                        .start(this);
            } catch (Throwable e) {
                Timber.e(e);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                itemSelectUserLogo.setImageURI(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Timber.e(result.getError());
            }
        }
    }
}