/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvsdkdemoandroid.detail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bazaarvoice.bvandroidsdk.BVProduct;
import com.bazaarvoice.bvandroidsdk.CurationsFeedItem;
import com.bazaarvoice.bvandroidsdk.CurationsRecyclerView;
import com.bazaarvoice.bvandroidsdk.Product;
import com.bazaarvoice.bvandroidsdk.RecommendationsRecyclerView;
import com.bazaarvoice.bvsdkdemoandroid.R;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.DemoProductContract;
import com.bazaarvoice.bvsdkdemoandroid.conversations.browseproducts.DemoProductPresenter;
import com.bazaarvoice.bvsdkdemoandroid.conversations.questions.DemoQuestionsActivity;
import com.bazaarvoice.bvsdkdemoandroid.conversations.reviews.DemoReviewsActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.DemoCurationsPostActivity;
import com.bazaarvoice.bvsdkdemoandroid.curations.detail.DemoCurationsDetailActivity;
import com.bazaarvoice.bvsdkdemoandroid.recommendations.DemoProductsCache;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoConfigUtils;
import com.bazaarvoice.bvsdkdemoandroid.utils.DemoDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DemoFancyProductDetailActivity extends AppCompatActivity implements DemoProductRecContract.View, DemoProductCurationsRowContract.View, DemoProductDetailRecAdapter.ProductTapListener, DemoProductDetailCurationsAdapter.CurationFeedItemTapListener, DemoProductContract.View {

    private static final String EXTRA_PRODUCT_ID = "extra_product_id";
    private static final String EXTRA_PRODUCT_NAME = "extra_product_name";
    private static final String EXTRA_PRODUCT_IMAGE_URL = "extra_product_image_url";

    String productId;
    String productName;
    String productImageUrl;
    String productPrice;
    float productAverageRating;

    @BindView(R.id.product_image) ImageView prodImage;
    @BindView(R.id.product_name) TextView prodName;
    @BindView(R.id.product_price) TextView prodPrice;
    @BindView(R.id.product_rating) RatingBar prodRating;

    @BindView(R.id.product_row_rec_recycler_view) RecommendationsRecyclerView recommendationsRecyclerView;
    DemoProductDetailRecAdapter recAdapter;
    @BindView(R.id.no_recs_found) TextView noRecsFoundTextView;
    @BindView(R.id.get_recs_progress) ProgressBar getRecsProgressBar;

    @BindView(R.id.product_row_curations_recycler_view) CurationsRecyclerView curationsRecyclerView;
    DemoProductDetailCurationsAdapter curationsAdapter;
    @BindView(R.id.no_curations_found) TextView noCurationsFoundTextView;
    @BindView(R.id.get_curations_progress) ProgressBar getCurationsProgressBar;
    @BindView(R.id.curations_submit_container) ViewGroup curationsSubmitViewGroup;
    @BindView(R.id.curations_submit_image) TextView fontAwesomeCameraIcon;

    @BindView(R.id.conv_reviews) TextView convReviews;
    @BindView(R.id.conv_questions_image) TextView fontAwesomeQuestionIcon;
    @BindView(R.id.conv_reviews_loading) ProgressBar convReviewsProgressBar;
    @BindView(R.id.conv_questions) TextView convQuestions;
    @BindView(R.id.conv_reviews_image) TextView fontAwesomeReviewsIcon;
    @BindView(R.id.conv_questions_loading) ProgressBar convQuestionsProgressBar;

    private DemoProductRecContract.UserActionsListener recRowActionListener;
    private DemoProductCurationsRowPresenter curationsRowActionListener;
    private DemoProductContract.UserActionsListener productDataActionListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fancy_product_detail);
        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_PRODUCT_ID)) {
            productId = savedInstanceState.getString(EXTRA_PRODUCT_ID);
            productName = savedInstanceState.getString(EXTRA_PRODUCT_NAME);
            productImageUrl = savedInstanceState.getString(EXTRA_PRODUCT_IMAGE_URL);
        } else {
            productId = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
            if (getIntent().hasExtra(EXTRA_PRODUCT_NAME)) {
                productName = getIntent().getStringExtra(EXTRA_PRODUCT_NAME);
                productImageUrl = getIntent().getStringExtra(EXTRA_PRODUCT_IMAGE_URL);
            } else {
                BVProduct bvProduct = DemoProductsCache.getInstance().getDataItem(productId);
                productName = bvProduct.getProductName();
                productImageUrl = bvProduct.getImageUrl();
                productPrice = bvProduct.getPrice();
                productAverageRating = bvProduct.getAverageRating();
            }
        }

        setupToolbar();
        setupFab();
        setupHeader();
        setupConversationsRow();
        setupRecommendationsRow();
        setupCurationsRow();

        DemoConfigUtils demoConfigUtils = DemoConfigUtils.getInstance(this);
        DemoDataUtil demoDataUtil = DemoDataUtil.getInstance(this);
        recRowActionListener = new DemoProductRecPresenter(this, demoConfigUtils, demoDataUtil, false, recommendationsRecyclerView);
        curationsRowActionListener = new DemoProductCurationsRowPresenter(this, demoConfigUtils, demoDataUtil, productId);
        productDataActionListener = new DemoProductPresenter(demoConfigUtils, demoDataUtil, this, productId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        recRowActionListener.loadRecommendationsWithProductId(false, productId);
        curationsRowActionListener.loadCurationsFeedItems(false);
        productDataActionListener.loadProduct(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_PRODUCT_ID, productId);
        outState.putString(EXTRA_PRODUCT_NAME, productName);
        outState.putString(EXTRA_PRODUCT_IMAGE_URL, productImageUrl);
    }

    private void setupToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(productName);
    }

    private void setupFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReviewDialog();
            }
        });
    }

    private void setupHeader() {
        Picasso.with(this)
                .load(productImageUrl)
                .into(prodImage);

        prodName.setText(productName);
        prodPrice.setText(productPrice);
        boolean hasPrice = !TextUtils.isEmpty(productPrice);
        prodPrice.setVisibility(hasPrice ? View.VISIBLE : View.GONE);
        prodRating.setRating((int)productAverageRating);
    }

    private void setupConversationsRow() {
        ViewGroup convQuestionsViewGroup = (ViewGroup) findViewById(R.id.conv_questions_container);
        convQuestionsViewGroup.setOnClickListener(convQuestionsRowClickListener);

        // Add the Font-Awesome icon
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        fontAwesomeReviewsIcon.setTypeface(fontAwesomeFont);

        ViewGroup convReviewsViewGroup = (ViewGroup) findViewById(R.id.conv_reviews_container);
        convReviewsViewGroup.setOnClickListener(convReviewsRowClickListener);
        // Add the Font-Awesome icon
        fontAwesomeQuestionIcon.setTypeface(fontAwesomeFont);
    }

    private View.OnClickListener convQuestionsRowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            productDataActionListener.onQandATapped();
        }
    };

    private View.OnClickListener convReviewsRowClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            productDataActionListener.onReviewsTapped();
        }
    };

    private View.OnClickListener curationsSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DemoFancyProductDetailActivity.this, DemoCurationsPostActivity.class);
            startActivity(intent);
        }
    };

    private void setupRecommendationsRow() {
        recommendationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recAdapter = new DemoProductDetailRecAdapter();
        recAdapter.setProductTapListener(this);
        recommendationsRecyclerView.setAdapter(recAdapter);
        recommendationsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupCurationsRow() {
        curationsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        curationsAdapter = new DemoProductDetailCurationsAdapter();
        curationsAdapter.setCurationFeedItemTapListener(this);
        curationsRecyclerView.setAdapter(curationsAdapter);
        curationsRecyclerView.setNestedScrollingEnabled(false);
        curationsSubmitViewGroup.setOnClickListener(curationsSubmitClickListener);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        fontAwesomeCameraIcon.setTypeface(fontAwesomeFont);

    }

    private void showReviewDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DemoSubmitDialogFragment.newInstance(getString(R.string.review_body_hint));
        dialog.show(getSupportFragmentManager(), "SubmitReviewDialogFragment");
    }

    @Override
    public void showRecommendations(List<BVProduct> bvProducts) {
        recAdapter.refreshRecs(bvProducts);
        noRecsFoundTextView.setVisibility(View.GONE);
        getRecsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingRecs(boolean show) {
        getRecsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoRecommendations() {
        noRecsFoundTextView.setVisibility(View.VISIBLE);
        getRecsProgressBar.setVisibility(View.GONE);
        recAdapter.refreshRecs(Collections.<BVProduct>emptyList());
    }

    @Override
    public void showError() {

    }

    @Override
    public void showRecMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCurations(List<CurationsFeedItem> feedItems) {
        curationsAdapter.refreshFeedItems(feedItems);
        noCurationsFoundTextView.setVisibility(View.GONE);
        getCurationsProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingCurations(boolean show) {
        getCurationsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showNoCurations() {
        noCurationsFoundTextView.setVisibility(View.VISIBLE);
        getCurationsProgressBar.setVisibility(View.GONE);
        curationsAdapter.refreshFeedItems(Collections.<CurationsFeedItem>emptyList());
    }

    @Override
    public void showCurationsMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void transitionToCurationsFeedItem(int index, List<CurationsFeedItem> curationsFeedItems) {
        Intent intent = new Intent(this, DemoCurationsDetailActivity.class);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<CurationsFeedItem>>() {
        }.getType();
        String curationsFeedItemsJsonStr = gson.toJson(curationsFeedItems, listType);
        intent.putExtra(DemoCurationsDetailActivity.CURRATIONS_UPDATE_KEY, curationsFeedItemsJsonStr);
        intent.putExtra(DemoCurationsDetailActivity.CURRATIONS_UPDATE_IDX_KEY, index);
        startActivity(intent);
    }

    /**
     * Use this to start the activity when the Product is cached, and is accessible with the product id
     * @param fromActivity
     * @param productId
     */
    public static void transitionTo(Activity fromActivity, String productId) {
        Intent intent = new Intent(fromActivity, DemoFancyProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        fromActivity.startActivity(intent);
    }

    /**
     * User this to start the activity when the Product is not cached, and is not accessible with the product id
     * @param fromActivity
     * @param productId
     * @param productName
     * @param productImageUrl
     */
    public static void transitionTo(Activity fromActivity, String productId, String productName, String productImageUrl) {
        Intent intent = new Intent(fromActivity, DemoFancyProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT_ID, productId);
        intent.putExtra(EXTRA_PRODUCT_NAME, productName);
        intent.putExtra(EXTRA_PRODUCT_IMAGE_URL, productImageUrl);
        fromActivity.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductTapListener(BVProduct bvProduct) {
        DemoFancyProductDetailActivity.transitionTo(this, bvProduct.getProductId());
    }

    @Override
    public void onCurationFeedItemTapped(CurationsFeedItem curationsFeedItem) {
        curationsRowActionListener.onCurationsFeedItemTapped(curationsFeedItem);
    }

    @Override
    public void transitionToReviews() {
        DemoReviewsActivity.transitionTo(DemoFancyProductDetailActivity.this, productId);
    }

    @Override
    public void showProduct(Product product) {
        int numReviews = (product.getReviewStatistics().getTotalReviewCount() != null) ? product.getReviewStatistics().getTotalReviewCount() : 0;
        int numQuestions = (product.getQaStatistics().getTotalQuestionCount() != null) ? product.getQaStatistics().getTotalQuestionCount() : 0;
        int numAnswers = (product.getQaStatistics().getTotalAnswerCount() != null) ? product.getQaStatistics().getTotalAnswerCount() : 0;

        if (numReviews > 0) {
            convReviews.setText(String.format(getString(R.string.conv_reviews_photos), numReviews));
        } else {
            convReviews.setText("0 Reviews, be the first!");
        }

        if (numQuestions > 0) {
            convQuestions.setText(String.format(getString(R.string.conv_questions), numQuestions, numAnswers));
        } else {
            convQuestions.setText("0 Questions, be the first!");
        }
    }

    @Override
    public void showNoProduct() {
        convReviews.setText("0 Reviews, be the first!");
        convQuestions.setText("0 Questions, be the first!");
    }

    @Override
    public void showLoadingProduct(boolean show) {
        convReviewsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        convQuestionsProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showSubmitReviewDialog() {
        showReviewDialog();
    }

    @Override
    public void showAskQuestionDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = DemoSubmitDialogFragment.newInstance(getString(R.string.question_body_hint));
        dialog.show(getSupportFragmentManager(), "SubmitQuestionDialogFragment");
    }

    @Override
    public void transitionToQandA() {
        DemoQuestionsActivity.transitionTo(this, productId);
    }

}
