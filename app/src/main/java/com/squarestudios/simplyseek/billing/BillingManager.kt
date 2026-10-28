package com.squarestudios.simplyseek.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Product ID for the one-time "Remove Ads" in-app purchase.
 */
const val PRODUCT_REMOVE_ADS = "premium_remove_ads"

/**
 * Helper class managing Google Play Billing Library connection, product queries,
 * purchase flow, and entitlement state updates for "Remove Ads".
 */
class BillingManager(
    context: Context,
    private val externalScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : PurchasesUpdatedListener {

    private val _isRemoveAdsPurchased = MutableStateFlow(false)
    val isRemoveAdsPurchased: StateFlow<Boolean> = _isRemoveAdsPurchased.asStateFlow()

    private val _removeAdsProductDetails = MutableStateFlow<ProductDetails?>(null)
    val removeAdsProductDetails: StateFlow<ProductDetails?> = _removeAdsProductDetails.asStateFlow()

    private val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
        .enableOneTimeProducts()
        .build()

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(pendingPurchasesParams)
        .build()

    /**
     * Establishes connection to Google Play Billing Service.
     */
    fun startConnection() {
        if (billingClient.isReady) {
            queryExistingPurchases()
            queryProductDetails()
            return
        }

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryExistingPurchases()
                    queryProductDetails()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Connection disconnected; will reconnect on demand
            }
        })
    }

    /**
     * Queries existing in-app purchases to check if user owns "Remove Ads".
     */
    fun queryExistingPurchases() {
        if (!billingClient.isReady) return

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases)
            }
        }
    }

    /**
     * Queries details (title, price, description) for "premium_remove_ads".
     */
    fun queryProductDetails() {
        if (!billingClient.isReady) return

        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(PRODUCT_REMOVE_ADS)
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val details = productDetailsList.firstOrNull { it.productId == PRODUCT_REMOVE_ADS }
                _removeAdsProductDetails.value = details
            }
        }
    }

    /**
     * Launches the Google Play purchase flow for "Remove Ads".
     *
     * @return `true` if purchase flow launched successfully.
     */
    fun launchPurchaseFlow(activity: Activity): Boolean {
        val details = _removeAdsProductDetails.value ?: return false
        if (!billingClient.isReady) return false

        val productDetailsParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .build()

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(listOf(productDetailsParams))
            .build()

        val result = billingClient.launchBillingFlow(activity, flowParams)
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            handlePurchases(purchases)
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        for (purchase in purchases) {
            if (purchase.products.contains(PRODUCT_REMOVE_ADS) &&
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            ) {
                _isRemoveAdsPurchased.value = true

                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        externalScope.launch {
            billingClient.acknowledgePurchase(params) { /* Purchase acknowledged */ }
        }
    }

    /**
     * Ends the billing client connection when no longer needed.
     */
    fun endConnection() {
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }
}
