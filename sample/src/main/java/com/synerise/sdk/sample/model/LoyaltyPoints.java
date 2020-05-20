package com.synerise.sdk.sample.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoyaltyPoints {

    @SerializedName("content")
    @Expose
    private LoyaltyPointsContent pointsContent;
    @SerializedName("schema")
    @Expose
    private String schema;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("uuid")
    @Expose
    private String uuid;

    public LoyaltyPointsContent getPointsContent() {
        return pointsContent;
    }

    public void setPointsContent(LoyaltyPointsContent pointsContent) {
        this.pointsContent = pointsContent;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
