/*
 * Copyright 2017
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.bazaarvoice.bvandroidsdk;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class for generating filter options
 */
final class Filter {

    static final String paramKey = "Filter";

    private final UGCOption option;
    private final EqualityOperator equalityOperator;
    private final List<String> filterValues;

    /**
     *
     * @param option Filter option
     * @param equalityOperator Equality type
     * @param value On value which must be true. Adding many of these is how an AND filter is created
     */
    Filter(UGCOption option, EqualityOperator equalityOperator, String value) {
        this.option = option;
        this.equalityOperator = equalityOperator;
        this.filterValues = new ArrayList<>();
        filterValues.add(value);
    }

    /**
     *
     * @param option Filter option
     * @param equalityOperator Equality type
     * @param values Many values where one must be true. This is how an OR filter is created
     */
    Filter(UGCOption option, EqualityOperator equalityOperator, @NonNull List<String> values) {
        this.option = option;
        this.equalityOperator = equalityOperator;
        this.filterValues = values;
        Collections.sort(this.filterValues);
    }

     public String toString() {
        Collections.sort(filterValues);
        return String.format("%s:%s:%s",
            option.getKey(),
            equalityOperator.getKey(),
            StringUtils.componentsSeparatedByWithEscapes(filterValues, ","));
    }

    enum Type implements UGCOption{
        Id("Id"),
        ProductId("ProductId"),
        AverageOverallRating("AverageOverallRating"),
        CategoryAncestorId("CategoryAncestorId"),
        CategoryId("CategoryId"),
        IsActive("IsActive"),
        IsDisabled("IsDisabled"),
        LastAnswerTime("LastAnswerTime"),
        LastQuestionTime("LastQuestionTime"),
        LastReviewTime("LastReviewTime"),
        LastStoryTime("LastStoryTime"),
        Name("Name"),
        RatingsOnlyReviewCount("RatingsOnlyReviewCount"),
        TotalAnswerCount("TotalAnswerCount"),
        TotalQuestionCount("TotalQuestionCount"),
        TotalReviewCount("TotalReviewCount"),
        TotalStoryCount("TotalStoryCount");

        private final String key;

        Type(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        ReviewOptions.Sort a = ReviewOptions.Sort.CampaignId;
    }
}
