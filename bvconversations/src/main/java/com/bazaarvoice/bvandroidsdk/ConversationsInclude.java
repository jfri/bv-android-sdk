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
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Common options for included Product and Review
 *
 * @param <ProductType> Product Type
 * @param <ReviewType> Review Type
 */
class ConversationsInclude<ProductType extends BaseProduct, ReviewType extends BaseReview> {
  @SerializedName("Products") private Map<String, ProductType> itemMap;
  @SerializedName("Answers") private Map<String, Answer> answerMap;
  @SerializedName("Questions") private Map<String, Question> questionMap;
  @SerializedName("Reviews") private Map<String, ReviewType> reviewMap;
  @SerializedName("Comments") private Map<String, Comment> commentMap;

  private transient List<ProductType> items;
  private transient List<Answer> answers;
  private transient List<Question> questions;
  private transient List<ReviewType> reviews;
  private transient List<Comment> comments;

  protected Map<String, ProductType> getItemMap() {
    return itemMap;
  }

  protected Map<String, Answer> getAnswerMap() {
    return answerMap;
  }

  protected Map<String, Question> getQuestionMap() {
    return questionMap;
  }

  protected Map<String, ReviewType> getReviewMap() {
    return reviewMap;
  }

  protected Map<String, Comment> getCommentMap() {
    return commentMap;
  }

  protected List<ReviewType> getReviewsList() {
    if (reviews == null) {
      reviews = processContent(reviewMap);
    }
    return reviews;
  }

  public List<Question> getQuestions() {
    if (questions == null) {
      questions = processContent(questionMap);
    }
    return questions;
  }

  protected List<ProductType> getItems() {
    if (items == null) {
      this.items = processContent(itemMap);
    }
    return this.items;
  }

  public List<Answer> getAnswers() {
    if (answers == null) {
      answers = processContent(answerMap);
    }
    return answers;
  }

  protected List<Comment> getComments() {
    if (comments == null) {
      comments = processContent(commentMap);
    }
    return comments;
  }

  @NonNull
  private <ContentType extends IncludeableContent> List<ContentType> processContent(@Nullable Map<String, ContentType> contents) {
    List<ContentType> contentList = new ArrayList<>();
    if (contents != null) {
      for (ContentType content : contents.values()) {
        content.setIncludedIn(this);
        contentList.add(content);
      }
    }
    return contentList;
  }
}