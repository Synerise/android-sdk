package com.synerise.sdk.sample.persistence.model;

import com.google.gson.annotations.SerializedName;

public class StoragePOJO {

    @SerializedName("user_name") private String name;
    @SerializedName("user_last_name") private String lastName;
    @SerializedName("user_email") private String email;

    @SerializedName("is_signed_in") private boolean isSignedIn;

    // ****************************************************************************************************************************************

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSignedIn() {
        return isSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        isSignedIn = signedIn;
    }
}