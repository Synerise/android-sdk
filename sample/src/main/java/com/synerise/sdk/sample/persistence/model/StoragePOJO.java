package com.synerise.sdk.sample.persistence.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.ui.cart.CartItem;

import java.util.ArrayList;
import java.util.List;

public class StoragePOJO {

    @SerializedName("user_name") private String name;
    @SerializedName("user_last_name") private String lastName;
    @SerializedName("user_email") private String email;
    @SerializedName("user_phone") private String phone;
    @SerializedName("is_signed_in") private boolean isSignedIn;
    @SerializedName("cart_items") private List<CartItem> cartItems;
    @SerializedName("business_profile_api_key") private String businessProfileApiKey;
    @SerializedName("client_api_key") private String clientApiKey;
    @SerializedName("favourite_products") private List<Product> favouriteProducts;

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

    @NonNull
    public List<CartItem> getCartItems() {
        return cartItems == null ? new ArrayList<>() : cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getBusinessProfileApiKey() {
        return businessProfileApiKey == null ? "" : businessProfileApiKey;
    }

    public void setBusinessProfileApiKey(String businessProfileApiKey) {
        this.businessProfileApiKey = businessProfileApiKey;
    }

    public String getClientApiKey() {
        return clientApiKey == null ? "" : clientApiKey;
    }

    public void setClientApiKey(String clientProfileApiKey) {
        this.clientApiKey = clientProfileApiKey;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public List<Product> getFavouriteProducts() {
        if(favouriteProducts == null) return new ArrayList<>();
        return favouriteProducts;
    }

    public void setFavouriteProducts(List<Product> favouriteProducts) {
        this.favouriteProducts = favouriteProducts;
    }
}