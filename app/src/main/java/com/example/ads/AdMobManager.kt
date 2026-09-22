package com.example.ads

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import java.util.concurrent.atomic.AtomicBoolean

object AdMobManager {
    private const val TAG = "AdMobManager"

    private val isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var interstitialAd: InterstitialAd? = null
    private var isInterstitialLoading = false
    private var lastInterstitialShownTime = 0L
    private const val INTERSTITIAL_COOLDOWN_MS = 180_000L // 3 minutes cooldown

    val bannerAdUnitId: String
        get() = BuildConfig.ADMOB_BANNER_ID

    val interstitialAdUnitId: String
        get() = BuildConfig.ADMOB_INTERSTITIAL_ID

    fun initializeWithConsent(activity: Activity, onReady: () -> Unit = {}) {
        try {
            val params = ConsentRequestParameters.Builder().build()
            val consentInformation = UserMessagingPlatform.getConsentInformation(activity)

            consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                {
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity) { formError ->
                        if (formError != null) {
                            Log.w(TAG, "Consent form error: ${formError.errorCode} - ${formError.message}")
                        }
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAds(activity)
                        }
                        onReady()
                    }
                },
                { requestConsentError ->
                    Log.w(TAG, "Consent info update error: ${requestConsentError.errorCode} - ${requestConsentError.message}")
                    if (consentInformation.canRequestAds()) {
                        initializeMobileAds(activity)
                    }
                    onReady()
                }
            )

            // If consent was previously gathered or available, initialize MobileAds
            if (consentInformation.canRequestAds()) {
                initializeMobileAds(activity)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Consent flow initialization error: ${e.message}")
            try {
                initializeMobileAds(activity)
            } catch (ignored: Exception) {}
            onReady()
        }
    }

    private fun initializeMobileAds(context: Context) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }
        try {
            MobileAds.initialize(context) { status ->
                Log.d(TAG, "MobileAds initialized: $status")
                preloadInterstitial(context)
            }
        } catch (e: Exception) {
            Log.w(TAG, "MobileAds initialize exception: ${e.message}")
        }
    }

    fun preloadInterstitial(context: Context) {
        if (interstitialAd != null || isInterstitialLoading) return
        isInterstitialLoading = true

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            context,
            interstitialAdUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isInterstitialLoading = false
                    Log.d(TAG, "Interstitial ad loaded successfully")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    interstitialAd = null
                    isInterstitialLoading = false
                    Log.w(TAG, "Failed to load interstitial ad: ${loadAdError.message}")
                }
            }
        )
    }

    fun showInterstitialIfAllowed(activity: Activity, onDismissed: () -> Unit) {
        val now = System.currentTimeMillis()
        if (now - lastInterstitialShownTime < INTERSTITIAL_COOLDOWN_MS) {
            onDismissed()
            return
        }

        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    lastInterstitialShownTime = System.currentTimeMillis()
                    preloadInterstitial(activity)
                    onDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    preloadInterstitial(activity)
                    onDismissed()
                }
            }
            ad.show(activity)
        } else {
            preloadInterstitial(activity)
            onDismissed()
        }
    }

    fun showPrivacyOptionsForm(activity: Activity, onComplete: () -> Unit = {}) {
        UserMessagingPlatform.showPrivacyOptionsForm(activity) { formError ->
            if (formError != null) {
                Log.w(TAG, "Error showing privacy options form: ${formError.message}")
            }
            onComplete()
        }
    }
}
