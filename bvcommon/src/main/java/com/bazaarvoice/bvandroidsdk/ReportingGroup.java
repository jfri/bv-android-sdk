package com.bazaarvoice.bvandroidsdk;

public enum ReportingGroup {
    LISTVIEW("listView"), GRIDVIEW("gridView"), RECYCLERVIEW("recyclerView"), CUSTOM("custom");

    private String value;

    ReportingGroup(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}