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

import java.util.List;

/**
 * Request to get {@link Review}s for a {@link Product}
 */
public class ReviewsRequest extends ConversationsDisplayRequest {
  private final String productId;
  private final int limit;
  private final int offset;
  private final List<Sort> sorts;
  private final String searchPhrase;
  private final List<ReviewIncludeType> reviewIncludeTypes;

  private ReviewsRequest(Builder builder) {
    super(builder);
    this.productId = builder.productId;
    this.limit = builder.limit;
    this.offset = builder.offset;
    this.sorts = builder.sorts;
    this.searchPhrase = builder.searchPhrase;
    this.reviewIncludeTypes = builder.reviewIncludeTypes;
  }

  String getProductId() {
    return productId;
  }

  int getLimit() {
    return limit;
  }

  int getOffset() {
    return offset;
  }

  List<Sort> getSorts() {
    return sorts;
  }

  String getSearchPhrase() {
    return searchPhrase;
  }

  List<ReviewIncludeType> getReviewIncludeTypes() {
    return reviewIncludeTypes;
  }

  @Override
  BazaarException getError() {
    if (limit < 1 || limit > 100) {
        return new BazaarException(String.format("Invalid `limit` value: Parameter 'limit' has invalid value: %d - must be between 1 and 100.", limit));
    }
    return null;
  }

  public static final class Builder extends ReviewDisplayRequestBuilder<Builder, ReviewsRequest> {
    private final String productId;
    private final int limit;
    private final int offset;

    public Builder(@NonNull String productId, int limit, int offset) {
      super(productId, limit, offset);
      this.productId = productId;
      this.limit = limit;
      this.offset = offset;
    }

    @Override
    public ReviewsRequest build() {
      return new ReviewsRequest(this);
    }

  }
}