package com.application.arenda.Entities.Authentication;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public final class Authentication {
    private static Authentication instance;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private OnAuthenticationListener registerListener;
    private OnAuthenticationListener authorizationListener;

    public Authentication() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static Authentication getInstance() {
        if (instance == null)
            instance = new Authentication();

        return instance;
    }

    @SuppressLint("ShowToast")
    public void registration(final String name,
                             final String lastName,
                             final String email,
                             final String password,
                             final String phone,
                             final String accountType) {

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        String userId = firebaseAuth.getUid();

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userId);

                        hashMap.put("name", name);
                        hashMap.put("lastName", lastName);

                        hashMap.put("userPhoto", "");

                        hashMap.put("phone_1", phone);
                        hashMap.put("phone_2", "");
                        hashMap.put("phone_3", "");

                        hashMap.put("address_1", "");
                        hashMap.put("address_2", "");
                        hashMap.put("address_3", "");

                        hashMap.put("balance", "0");
                        hashMap.put("rating", "0.0");
                        hashMap.put("accountType", accountType);

                        databaseReference.setValue(hashMap)
                                .addOnCompleteListener(task1 -> registerListener.onComplete(task1))
                                .addOnFailureListener(e -> registerListener.onFailure(e));
                    }
                }).addOnFailureListener(e -> registerListener.onFailure(e));
    }

    @SuppressLint("ShowToast")
    public void authorization(final String email, final String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> authorizationListener.onComplete(task))
                .addOnFailureListener(e -> authorizationListener.onFailure(e));
    }

    public static FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void setOnRegisterListener(OnAuthenticationListener listener) {
        registerListener = listener;
    }

    public void setOnAuthorizationListener(OnAuthenticationListener listener) {
        authorizationListener = listener;
    }
}