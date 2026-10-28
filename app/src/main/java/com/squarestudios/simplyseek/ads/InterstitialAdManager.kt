package com.squarestudios.simplyseek.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

// Official Google AdMob Test Interstitial Ad Unit ID
private const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

/**
 * Helper class to preload and show AdMob Interstitial Ads when transitioning between game states.
 */
class InterstitialAdManager(
    private val adUnitId: String = TEST_INTERSTITIAL_AD_UNIT_ID,
) {

    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false

    /**
     * Preloads an Interstitial Ad in advance.
     */
    fun preloadAd(context: Context) {
        if (interstitialAd != null || isLoading) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            },
        )
    }

    /**
     * Shows the preloaded Interstitial Ad if ready, invoking [onAdDismissed] when done or failed.
     *
     * @param activity The host Activity required to present full screen ad content.
     * @param onAdDismissed Callback invoked when the ad is closed or fails to show.
     */
    fun showAd(activity: Activity, onAdDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    preloadAd(activity) // Preload next ad
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    onAdDismissed()
                }
            }
            ad.show(activity)
        } else {
            // Ad not loaded yet, proceed immediately
            onAdDismissed()
        }
    }
}
