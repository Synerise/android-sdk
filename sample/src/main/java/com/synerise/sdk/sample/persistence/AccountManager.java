package com.synerise.sdk.sample.persistence;

import androidx.annotation.NonNull;

import com.synerise.sdk.promotions.model.promotion.Promotion;
import com.synerise.sdk.sample.data.Product;
import com.synerise.sdk.sample.persistence.model.StoragePOJO;
import com.synerise.sdk.sample.ui.cart.adapter.item.CartItem;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    @NonNull private final IPrefsStorage prefsStorage;
    @NonNull private final StoragePOJO storagePOJO;

    public AccountManager(@NonNull IPrefsStorage prefsStorage) {
        this.prefsStorage = prefsStorage;
        this.storagePOJO = prefsStorage.readStoragePOJO() == null ? new StoragePOJO() : prefsStorage.readStoragePOJO();
    }

    public void signOut() {
        setUserName(null);
        setUserLastName(null);
        setUserEmail(null);
        setUserPoints(null);
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

    public void setUserPoints(String points) {
        storagePOJO.setPoints(points);
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

    public String getUserPoints() {
        return storagePOJO.getPoints();
    }

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
            if (cartItem.getProduct().getSKU().equals(product.getSKU()))
                return cartItem;
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

    public void addProductAsFavourite(Product product) {
        if (!isProductFavourite(product)) {
            ArrayList<Product> favouriteProductsList = new ArrayList<>(getFavouriteProducts());
            favouriteProductsList.add(product);
            storagePOJO.setFavouriteProducts(favouriteProductsList);
            prefsStorage.saveStoragePOJO(storagePOJO);
        }
    }

    public void removeProductFromFavourite(Product product) {
        if (isProductFavourite(product)) {
            ArrayList<Product> favouriteProductsList = new ArrayList<>(getFavouriteProducts());
            favouriteProductsList.remove(product);
            storagePOJO.setFavouriteProducts(favouriteProductsList);
            prefsStorage.saveStoragePOJO(storagePOJO);
        }
    }

    @NonNull
    public List<Product> getFavouriteProducts() {
        return storagePOJO.getFavouriteProducts();
    }

    public boolean isProductFavourite(Product product) {
        List<Product> favouriteProducts = getFavouriteProducts();
        return favouriteProducts.contains(product);
    }

    @NonNull
    public List<Promotion> getPromotions() {
        return storagePOJO.getPromotions();
    }

    public void updatePromotions(List<Promotion> promotions) {
        storagePOJO.setPromotions(promotions);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }

    public String getClientProfileApiKey() {
        return storagePOJO.getClientApiKey();
    }

    public void setClientApiKey(String clientApiKey) {
        storagePOJO.setClientApiKey(clientApiKey);
        prefsStorage.saveStoragePOJO(storagePOJO);
    }
}