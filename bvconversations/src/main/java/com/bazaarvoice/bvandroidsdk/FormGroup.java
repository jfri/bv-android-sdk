package com.bazaarvoice.bvandroidsdk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * An individual field for a submission form
 */
public class FormGroup {
  @SerializedName("SubElements")
  private List<FormSubElement> subElements;

  @SerializedName("Required")
  private boolean isRequired;

  @SerializedName("Id")
  private String id;

  @SerializedName("Label")
  private String label;

  @SerializedName("Type")
  private FormGroupType type;


  public List<FormSubElement> getSubElements() {
    return subElements;
  }

  public boolean isRequired() {
    return isRequired;
  }

  public String getId() {
    return id;
  }

  public String getLabel() {
    return label;
  }

  public FormGroupType getType() {
    return type;
  }
}
