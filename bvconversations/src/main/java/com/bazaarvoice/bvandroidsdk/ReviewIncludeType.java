package com.bazaarvoice.bvandroidsdk;

public enum ReviewIncludeType implements IncludeType {
  PRODUCTS("products"), COMMENTS("comments");

  private String value;

  ReviewIncludeType(String value) {
    this.value = value;
  }

  @Override
  public String toString() {
    return value;
  }
}
