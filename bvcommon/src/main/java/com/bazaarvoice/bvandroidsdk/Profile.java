/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Shopper Marketing Profile
 */
public class Profile {

    private static final String TARGETING_KEY_INTERESTS = "interests";
    private static final String TARGETING_KEY_BRANDS = "brands";

    private Map<String, Interest> interests;

    private Map<String, Interest> brands;

    private List<String> recommendations;

    private RecommendationStats recommendationStats;

    private Map<String, BVProduct> products;

    public Map<String, String> getTargetingKeywords() {
        Map<String, String> targetingKeywords = new HashMap<>();

        if (shouldFlattenKeywords(getInterests())) {
            targetingKeywords.put(TARGETING_KEY_INTERESTS, getFlattenedValue(getInterests()));
        }

        if (shouldFlattenKeywords(getBrands())) {
            targetingKeywords.put(TARGETING_KEY_BRANDS, getFlattenedValue(getBrands()));
        }

        return targetingKeywords;
    }

    private boolean shouldFlattenKeywords(Map<String, Interest> keywords) {
        return keywords != null && keywords.size() > 0;
    }

    private String getFlattenedValue(Map<String, Interest> input) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, Interest>> inputEntrySet = input.entrySet();
        Iterator<Map.Entry<String, Interest>> iterator = inputEntrySet.iterator();

        int numEntries = input.size();
        int currentIndex = 0;
        for (Map.Entry<String, Interest> entry : input.entrySet()) {
            String key = entry.getKey();
            Interest interest = entry.getValue();
            if (key == null || key.isEmpty() || interest == null || interest.getValue() == null || interest.getValue().isEmpty()) {
                continue;
            }
            String interestStr = interest.getValue();
            stringBuilder.append(key);
            stringBuilder.append("_");
            stringBuilder.append(interestStr);
            if (currentIndex++ != numEntries - 1) {
                stringBuilder.append(" ");
            }
        }

        return stringBuilder.toString();
    }

    Map<String, Interest> getInterests() {
        return interests;
    }

    Map<String, Interest> getBrands() {
        return brands;
    }

    public List<BVProduct> getRecommendedProducts() {
        List<BVProduct> bvProducts = new ArrayList<BVProduct>();

        if (!canGetRecommendedProducts()) {
            return bvProducts;
        }

        for (Map.Entry<String, BVProduct> entry : products.entrySet()) {
            String productId = entry.getKey();
            if (recommendations.contains(productId)) {
                BVProduct bvProduct = entry.getValue();
                bvProduct.mergeRecommendationStats(recommendationStats);
                bvProducts.add(entry.getValue());
            }
        }

        return bvProducts;
    }

    private boolean canGetRecommendedProducts() {
        return recommendations != null && products != null && !products.isEmpty();
    }

    @Override
    public String toString() {
        return "Profile{" +
                "interests=" + interests +
                ", brands=" + brands +
                ", recommendations=" + recommendations +
                ", recommendationStats=" + recommendationStats +
                ", products=" + products +
                '}';
    }
}
