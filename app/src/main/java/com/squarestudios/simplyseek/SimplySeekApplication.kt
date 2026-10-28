package com.squarestudios.simplyseek

import android.app.Application
import com.google.android.gms.ads.MobileAds

/**
 * Application class initializing global SDKs including Google Mobile Ads (AdMob).
 */
class SimplySeekApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize Google Mobile Ads SDK
        MobileAds.initialize(this) {}
    }
}
