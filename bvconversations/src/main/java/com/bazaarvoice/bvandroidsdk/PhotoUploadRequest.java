package com.bazaarvoice.bvandroidsdk;

/**
 * Internal class for uploading photos associate with CGC submission
 */
class PhotoUploadRequest extends ConversationsRequest {
  private final PhotoUpload photoUpload;

  private PhotoUploadRequest(Builder builder) {
    photoUpload = builder.photoUpload;
  }

  @Override
  BazaarException getError() {
    return null;
  }

  public PhotoUpload getPhotoUpload() {
    return photoUpload;
  }

  public static class Builder {
    private final PhotoUpload photoUpload;

    public Builder(PhotoUpload photoUpload) {
      this.photoUpload = photoUpload;
    }

    public PhotoUploadRequest build() {
      return new PhotoUploadRequest(this);
    }
  }
}
