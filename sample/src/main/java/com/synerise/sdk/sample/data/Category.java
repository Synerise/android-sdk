package com.synerise.sdk.sample.data;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public enum Category {

    // ELECTRONICS
    // ****************************************************************************************************************************************

    HD_TV(R.color.persian,
            R.drawable.banner_tv,
            R.string.hd_tv,
            createProducts(Product.LG_65UJ670V, Product.SONY_KD55A1, Product.SAMSUNG_UE75MU6172, Product.PHILIPS_49PUS6432)),

    LAPTOPS_TABLETS(R.color.endeavour,
            R.drawable.banner_laptop,
            R.string.laptops_tablets,
            createProducts(Product.ASUS_TRANSFORMER, Product.MICROSOFT_SURFACE, Product.MACBOOK_PRO_13, Product.IPAD_PRO_10)),

    PHONES(R.color.pacific,
            R.drawable.banner_phone,
            R.string.phones,
            createProducts(Product.IPHONE_X, Product.SAMSUNG_S9, Product.XIAOMI_MI_MIX_2, Product.GOOGLE_PIXEL_2)),

    HOUSEHOLD_GOODS(R.color.turquoise,
            R.drawable.banner_agd,
            R.string.houeshold_goods,
            createProducts(Product.ELECTROLUX_OVEN, Product.ELECTROLUX_VACUUM, Product.COFFEE_MACHINE, Product.MICROWAVE_OVEN)),

    // FOOD
    // ****************************************************************************************************************************************

    VEGETABLES(R.color.christi,
            R.drawable.banner_vegetables,
            R.string.vegetables,
            createProducts(Product.TOMATOES, Product.CARROTS, Product.CELERY_BUNCH, Product.FRESH_SPINACH)),
    FRUITS(R.color.pistachio,
            R.drawable.banner_fruits,
            R.string.fruits,
            createProducts(Product.STRAWBERRIES, Product.BANANAS, Product.ORANGES, Product.GALA_APPLE)),
    LIQUIDS(R.color.buddha,
            R.drawable.banner_liquids,
            R.string.liquids,
            createProducts(Product.COCA_COLA, Product.ORANGE_JUICE, Product.SEVEN_UP)),
    DAIRY_PRODUCTS(R.color.corn,
            R.drawable.banner_dairy,
            R.string.dairy,
            createProducts(Product.MILK, Product.BUTTER, Product.BUTTER_MILK)),

    // FASHION
    // ****************************************************************************************************************************************

    SHOES(R.color.cinnabar,
            R.drawable.banner_shoes,
            R.string.shoes,
            createProducts(Product.NIKE_SHOES, Product.ADIDAS_SHOES)),

    TROUSERS(R.color.fuzzy,
            R.drawable.banner_trousers,
            R.string.trousers,
            createProducts(Product.NIKE_TROUSERS, Product.ADIDAS_TROUSERS)),

    RUNNING_ACCESSORIES(R.color.vivid,
            R.drawable.banner_acc,
            R.string.running_accessories,
            createProducts(Product.NIKE_BACKPACK, Product.ADIDAS_SUNGLASSES, Product.ADIDAS_HAT)),
    JACKETS(R.color.royal,
            R.drawable.banner_jacket,
            R.string.jacket,
            createProducts(Product.NIKE_JACKET, Product.ADIDAS_JACKET)),

    // CULTURE
    // ****************************************************************************************************************************************

    MUSIC(R.color.amaranth,
            R.drawable.banner_music,
            R.string.music,
            createProducts(Product.SABBATH_BLOODY_SABBATH, Product.KANSAS_LEFTOVERTURE, Product.ABOVE_AND_BEYOND_COMMON_GROUND, Product.ELLIE_GOULDING_HALCYON)),
    BOOKS(R.color.lipstick,
            R.drawable.banner_books,
            R.string.books,
            createProducts(Product.STEENA_HOLMES_THE_FORGOTTEN_ONES_A_NOVEL, Product.CATHERINE_MCKENZIE_THE_GOOD_LIAR, Product.CATHERINE_MCKENZIE_HIDDEN, Product.JRR_TOLKIEN_THE_SILMARILLION)),
    MOVIES(R.color.cherry,
            R.drawable.banner_movies,
            R.string.movies,
            createProducts(Product.BLADE_RUNNER, Product.LADY_BIRD, Product.JUSTICE_LEAGUE, Product.ALIEN_COVENANT)),
    GAMES(R.color.loulou,
            R.drawable.banner_games,
            R.string.games,
            createProducts(Product.HALO_5, Product.OVERWATCH, Product.TITANFALL_2, Product.THE_WITCHER_3));

    // ****************************************************************************************************************************************

    private static ArrayList<Product> createProducts(Product... products) {
        return new ArrayList<>(Arrays.asList(products));
    }

    // ****************************************************************************************************************************************

    @ColorRes
    private final int background;
    @DrawableRes
    private final int image;
    @StringRes
    private final int text;
    private final List<Product> products;

    Category(int background, int image, int text, List<Product> products) {
        this.background = background;
        this.image = image;
        this.text = text;
        this.products = products;
    }

    @ColorRes
    public int getBackground() {
        return background;
    }

    public int getImage() {
        return image;
    }

    public int getText() {
        return text;
    }

    public List<Product> getProducts() {
        return products;
    }
}
