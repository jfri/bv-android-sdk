/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */

package com.bazaarvoice.bvandroidsdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bazaarvoice.bvandroidsdk.internal.Utils.mapPutSafe;

/**
 * @deprecated TODO remove after full BVPixel swap is complete
 *
 * Internal SDK Base class for forming Analytics.
 * Requires a class, type, and source to be passed in.
 * Has helper methods to safely add non-null values to the data map,
 * and to build the data map.
 */
abstract class BvAnalyticsSchema {
    protected static final String KEY_EVENT_CLASS = "cl";
    private static final String KEY_EVENT_TYPE = "type";
    private static final String KEY_SOURCE = "source";

    private String eventClass;
    private String eventType;
    private String source;

    /**
     * Enabled by default, but can be toggled to false.
     * e.g. PII event with email cannot send AdId
     */
    private boolean allowAdId = true;

    private List<BvPartialSchema> bvPartialSchemas = new ArrayList<BvPartialSchema>();
    private Map<String, Object> addedKeyVals = new HashMap<String, Object>();

    public BvAnalyticsSchema(String eventClass, String eventType, String source) {
        this.eventClass = eventClass;
        this.eventType = eventType;
        this.source = source;
    }

    protected void addKeyVal(String key, Object val) {
        mapPutSafe(addedKeyVals, key, val);
    }

    protected void addPartialSchema(BvPartialSchema bvPartialSchema) {
        if (bvPartialSchema != null) {
            bvPartialSchemas.add(bvPartialSchema);
        }
    }

    public void setAllowAdId(boolean allowAdId) {
        this.allowAdId = allowAdId;
    }

    public boolean allowAdId() {
        return allowAdId;
    }

    public Map<String, Object> getDataMap() {
        Map<String, Object> dataMap = new HashMap<>();
        mapPutSafe(dataMap, KEY_EVENT_CLASS, eventClass);
        mapPutSafe(dataMap, KEY_EVENT_TYPE, eventType);
        mapPutSafe(dataMap, KEY_SOURCE, source);

        dataMap.putAll(addedKeyVals);

        for (BvPartialSchema bvPartialSchema : bvPartialSchemas) {
            bvPartialSchema.addPartialData(dataMap);
        }

        return dataMap;
    }
}
