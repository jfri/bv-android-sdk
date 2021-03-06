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

package com.bazaarvoice.bvandroidsdk.types;

/**
 * An enum used for defining the vote type for feedback to be submitted.
 */
public enum FeedbackVoteType {
	/**
	 * Leave positive feedback.
	 */
	POSITIVE("positive"),
	/**
	 * Leave negative feedback.
	 */
	NEGATIVE("negative");
	
	private String feedbackVoteType;
	
	FeedbackVoteType(String feedbackType){
		this.feedbackVoteType = feedbackType;
	}
	public String getTypeString() {
		return this.feedbackVoteType;
	}
}
