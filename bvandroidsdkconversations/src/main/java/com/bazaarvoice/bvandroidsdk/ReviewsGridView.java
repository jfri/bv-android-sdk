/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk;

import android.content.Context;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

    public final class ReviewsGridView extends BVGridView implements BVConversationsClient.DisplayLoader<ReviewsRequest, ReviewResponse> {
    private WeakReference<ConversationsCallback<ReviewResponse>> delegateCbWeakRef;
    private String productId;
    private boolean onScreen = false;

    public ReviewsGridView(Context context) {
        super(context);
    }

    public ReviewsGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewsGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ReviewsGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public ReviewResponse loadSync(LoadCallDisplay<ReviewsRequest, ReviewResponse> call) throws BazaarException {
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
        ReviewResponse reviewResponse = call.loadSync();
        trySendUsedFeatureInViewEvent();
        return reviewResponse;
    }

    @Override
    public void loadAsync(LoadCallDisplay<ReviewsRequest, ReviewResponse> call, ConversationsCallback<ReviewResponse> callback) {
        final ReviewsRequest reviewsRequest = call.getRequest();
        productId = reviewsRequest.getProductId();
        delegateCbWeakRef = new WeakReference<ConversationsCallback<ReviewResponse>>(callback);
        call.loadAsync(receiverCb);
        trySendUsedFeatureInViewEvent();
    }

    private ConversationsCallback<ReviewResponse> receiverCb = new ConversationsCallback<ReviewResponse>() {
        @Override
        public void onSuccess(ReviewResponse response) {
            ConversationsCallback<ReviewResponse> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onSuccess(response);
        }

        @Override
        public void onFailure(BazaarException exception) {
            ConversationsCallback<ReviewResponse> delegateCb = delegateCbWeakRef.get();
            if (delegateCb == null) {
                return;
            }
            delegateCbWeakRef.clear();
            delegateCb.onFailure(exception);
        }
    };

    @Override
    public void onFirstTimeOnScreen() {
        this.onScreen = true;
        trySendUsedFeatureInViewEvent();
    }

    @Override
    public void onViewGroupInteractedWith() {
        ConversationsAnalyticsManager.sendUsedFeatureScrolledEvent(productId, MagpieBvProduct.RATINGS_AND_REVIEWS);
    }

    private void trySendUsedFeatureInViewEvent() {
        if (onScreen && productId != null) {
            ConversationsAnalyticsManager.sendUsedFeatureInViewEvent(productId, MagpieBvProduct.RATINGS_AND_REVIEWS);
        }
    }

    @Override
    public String getProductId() {
        return productId;
    }
}
