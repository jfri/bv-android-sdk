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

import com.bazaarvoice.bvandroidsdk.types.FeedbackContentType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackType;
import com.bazaarvoice.bvandroidsdk.types.FeedbackVoteType;

/**
 * Request class for submitting feedback on {@link Review}s, {@link Question}s,
 * and {@link Answer}s.
 */
public class FeedbackSubmissionRequest extends ConversationsSubmissionRequest {
    private final String contentId;
    private final String contentType;
    private final String feedbackType;
    private final String userId;
    private final String feedbackVote;
    private final String reasonFlaggedText;

    public FeedbackSubmissionRequest(Builder builder) {
        super(builder);
        this.contentId = builder.contentId;
        this.contentType = builder.contentType;
        this.feedbackType = builder.feedbackType;
        this.userId = builder.userId;
        this.feedbackVote = builder.feedbackVote;
        this.reasonFlaggedText = builder.reasonFlaggedText;
    }

    @Override
    BazaarException getError() {
        return null;
    }

    String getContentId() {
        return contentId;
    }

    String getContentType() {
        return contentType;
    }

    String getFeedbackType() {
        return feedbackType;
    }

    String getUserId() {
        return userId;
    }

    String getFeedbackVote() {
        return feedbackVote;
    }

    String getReasonFlaggedText() {
        return reasonFlaggedText;
    }

    public static final class Builder extends ConversationsSubmissionRequest.Builder<Builder> {

        private final String contentId;
        private String contentType;
        private String feedbackType;
        private String userId;
        private String feedbackVote;
        private String reasonFlaggedText;

        public Builder(String contentId) {
            super(Action.Preview);  // Feedback does not support action, so forcing preview will make a proper submission.
            this.contentId = contentId;
        }

        public Builder feedbackContentType(FeedbackContentType feedbackContentType) {
            this.contentType = feedbackContentType.getTypeString();
            return this;
        }

        public Builder feedbackType(FeedbackType feedbackType) {
            this.feedbackType = feedbackType.getTypeString();
            return this;
        }

        public Builder feedbackVote(FeedbackVoteType feedbackVote){
            this.feedbackVote = feedbackVote.getTypeString();
            return this;
        }

        public Builder userId(String userId){
            this.userId = userId;
            return this;
        }

        public Builder reasonFlaggedText(String reasonFlaggedText){
            this.reasonFlaggedText = reasonFlaggedText;
            return this;
        }

        public FeedbackSubmissionRequest build() {
            return new FeedbackSubmissionRequest(this);
        }

        @Override
        PhotoUpload.ContentType getPhotoContentType() {
            // TODO: Return unsupported?
            return PhotoUpload.ContentType.QUESTION;
        }
    }

}
