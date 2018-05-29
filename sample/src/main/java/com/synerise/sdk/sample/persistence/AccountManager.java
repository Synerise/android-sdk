package com.synerise.sdk.sample.persistence;

import android.content.Context;
import android.support.annotation.NonNull;

import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.persistence.model.StoragePOJO;
import com.synerise.sdk.sample.ui.cart.CartItem;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    @NonNull private final IPrefsStorage prefsStorage;
    @NonNull private final StoragePOJO storagePOJO;

    // ****************************************************************************************************************************************

    public AccountManager(Context context, @NonNull IPrefsStorage prefsStorage) {
        this.prefsStorage = prefsStorage;
        this.storagePOJO = prefsStorage.readStoragePOJO() == null ? new StoragePOJO() : prefsStorage.readStoragePOJO();
    }

    // ****************************************************************************************************************************************

    public void signOut() {
        setUserName(null);
        setUserLastName(null);
        setUserEmail(null);
    }

    public void setUserName(String name) {
        storagePOJO.setName(name);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public void setUserLastName(String lastName) {
        storagePOJO.setLastName(lastName);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public void setUserEmail(String email) {
        storagePOJO.setEmail(email);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public String getFirstName() {
        return storagePOJO.getName();
    }

    public String getLastName() {
        return storagePOJO.getLastName();
    }

    public String getEmail() {
        return storagePOJO.getEmail();
    }

    // ****************************************************************************************************************************************

    public void addCartItem(Product product) {
        List<CartItem> cartItems = storagePOJO.getCartItems();
        CartItem cartItem = isItemInCart(cartItems, product);
        if (cartItem == null) {
            cartItems.add(new CartItem(product));
        } else {
            cartItem.incrementQuantity();
        }
        storagePOJO.setCartItems(cartItems);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    private CartItem isItemInCart(List<CartItem> cartItems, Product product) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().getSKU().equals(product.getSKU())) {
                return cartItem;
            }
        }
        return null;
    }

    @NonNull
    public List<CartItem> getCartItems() {
        return storagePOJO.getCartItems();
    }

    public void clearCartItems() {
        storagePOJO.setCartItems(new ArrayList<>());
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public void removeCartItem(CartItem cartItemToRemove) {
        List<CartItem> cartItems = storagePOJO.getCartItems();
        cartItems.remove(cartItemToRemove);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    // ****************************************************************************************************************************************

    public String getBusinessProfileApiKey() {
        return storagePOJO.getBusinessProfileApiKey();
    }

    public void setBusinessProfileApiKey(String businessProfileApiKey) {
        storagePOJO.setBusinessProfileApiKey(businessProfileApiKey);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public String getClientProfileApiKey() {
        return storagePOJO.getClientApiKey();
    }

    public void setClientApiKey(String clientApiKey) {
        storagePOJO.setClientApiKey(clientApiKey);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public void setUserPhone(String phone) {
        storagePOJO.setPhone(phone);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public String getPhoneNumber() {
        return storagePOJO.getPhoneNumber();
    }
}