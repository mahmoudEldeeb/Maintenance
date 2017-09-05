package com.m_eldeeb.maintenance.services;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class FCMTokenRefreshListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent(this, FCMRegistrationService.class);

        startService(intent);

    }
}
