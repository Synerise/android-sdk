package com.synerise.sdk.sample.data;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.synerise.sdk.event.model.model.UnitPrice;
import com.synerise.sdk.sample.R;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public enum Product {

    //ELECTRONICS
    // TV
    LG_65UJ670V(R.string.lg_65uj670v_brand,
                R.string.lg_65uj670v_name,
                "el-65UJ670V",
                R.string.lg_65uj670v_desc,
                "http://gate.net.pl/mobile/el-65UJ670V.png",
                1450, 3.9, 89),
    SONY_KD55A1(R.string.sony_kd55a1_brand,
                R.string.sony_kd55a1_name,
                "el-KD55A1",
                R.string.sony_kd55a1_desc,
                "http://gate.net.pl/mobile/el-KD55A1.png",
                2499, 4.8, 21),
    SAMSUNG_UE75MU6172(R.string.samsung_ue75mu6172_brand,
                       R.string.samsung_ue75mu6172_name,
                       "el-UE75MU6172",
                       R.string.samsung_ue75mu6172_desc,
                       "http://gate.net.pl/mobile/el-UE75MU6172.png",
                       1999, 4.2, 121),
    PHILIPS_49PUS6432(R.string.philips_49pus6432_brand,
                      R.string.philips_49pus6432_name,
                      "el-49PUS6432",
                      R.string.philips_49pus6432_desc,
                      "http://gate.net.pl/mobile/el-49PUS6432.png",
                      799, 4.1, 234),

    // LAPTOPS
    ASUS_TRANSFORMER(R.string.asus_transformer_brand,
                     R.string.asus_transformer_name,
                     "el-t102",
                     R.string.asus_transformer_desc,
                     "http://gate.net.pl/mobile/el-t102.png",
                     429, 3.4, 78),
    MICROSOFT_SURFACE(R.string.microsoft_surface_brand,
                      R.string.microsoft_surface_name,
                      "el-srfcpro",
                      R.string.microsoft_surface_desc,
                      "http://gate.net.pl/mobile/el-srfcpro.png",
                      1799, 4.2, 51),
    MACBOOK_PRO_13(R.string.macbook_pro_13_brand,
                   R.string.macbook_pro_13_name,
                   "el-mbppro",
                   R.string.macbook_pro_13_desc,
                   "http://gate.net.pl/mobile/el-mbppro.png",
                   1899, 4.3, 74),
    IPAD_PRO_10(R.string.ipad_pro_10_brand,
                R.string.ipad_pro_10_name,
                "el-ipadpro",
                R.string.ipad_pro_10_desc,
                "http://gate.net.pl/mobile/el-ipadpro.png",
                799, 4.7, 253),

    // PHONES
    IPHONE_X(R.string.iphone_x_brand,
             R.string.iphone_x_name,
             "el-iphx",
             R.string.iphone_x_desc,
             "http://gate.net.pl/mobile/el-iphx.png",
             899, 4.4, 75),
    SAMSUNG_S9(R.string.samsung_s9_brand,
               R.string.samsung_s9_name,
               "el-gals9",
               R.string.samsung_s9_desc,
               "http://gate.net.pl/mobile/el-gals9.png",
               849, 4.5, 143),
    XIAOMI_MI_MIX_2(R.string.xiaomi_mi_mix_2_brand,
                    R.string.xiaomi_mi_mix_2_name,
                    "el-mix2",
                    R.string.xiaomi_mi_mix_2_desc,
                    "http://gate.net.pl/mobile/el-mix2.png",
                    399, 4.3, 167),
    GOOGLE_PIXEL_2(R.string.google_pixel_2_brand,
                   R.string.google_pixel_2_name,
                   "el-pix2",
                   R.string.google_pixel_2_desc,
                   "http://gate.net.pl/mobile/el-pix2.png",
                   849, 4.2, 32),

    //HOUSEHOLD
    ELECTROLUX_OVEN(R.string.electrolux_oven_brand,
                    R.string.electrolux_oven_name,
                    "el-oven",
                    R.string.electrolux_oven_desc,
                    "http://gate.net.pl/mobile/el-oven.png",
                    449, 3.9, 32),
    ELECTROLUX_VACUUM(R.string.electrolux_vacuum_brand,
                      R.string.electrolux_vacuum_name,
                      "el-vacuum",
                      R.string.electrolux_vacuum_desc,
                      "http://gate.net.pl/mobile/el-vacuum.png",
                      99, 3.5, 12),
    COFFEE_MACHINE(R.string.coffee_machine_brand,
                   R.string.coffee_machine_name,
                   "el-coffee",
                   R.string.coffee_machine_desc,
                   "http://gate.net.pl/mobile/el-coffee.png",
                   1749, 4.5, 36),
    MICROWAVE_OVEN(R.string.microwave_oven_brand,
                   R.string.microwave_oven_name,
                   "el-micro",
                   R.string.microwave_oven_desc,
                   "http://gate.net.pl/mobile/el-micro.png",
                   1999, 4.8, 21),

    // ****************************************************************************************************************************************
    // FOOD
    // VEGETABLES
    TOMATOES(R.string.tomatoes_brand,
             R.string.tomatoes_name,
             "fd-toma",
             R.string.tomatoes_desc,
             "http://gate.net.pl/mobile/fd-toma.png",
             1.29f, 4.8, 379),
    CARROTS(R.string.carrots_brand,
            R.string.carrots_name,
            "fd-carr",
            R.string.carrots_desc,
            "http://gate.net.pl/mobile/fd-carr.png",
            0.99f, 4.5, 430),
    CELERY_BUNCH(R.string.celery_bunch_brand,
                 R.string.celery_bunch_name,
                 "fd-cele",
                 R.string.celery_bunch_desc,
                 "http://gate.net.pl/mobile/fd-cele.png",
                 0.49f, 4.2, 173),
    FRESH_SPINACH(R.string.fresh_spinach_brand,
                  R.string.fresh_spinach_name,
                  "fd-spin",
                  R.string.fresh_spinach_desc,
                  "http://gate.net.pl/mobile/fd-spin.png",
                  0.69f, 3.7, 148),

    // FRUITS
    STRAWBERRIES(R.string.strawberries_brand,
                 R.string.strawberries_name,
                 "fd-straw",
                 R.string.strawberries_desc,
                 "http://gate.net.pl/mobile/fd-straw.png",
                 1.19f, 4.4, 293),
    BANANAS(R.string.bananas_brand,
            R.string.bananas_name,
            "fd-bana",
            R.string.bananas_desc,
            "http://gate.net.pl/mobile/fd-bana.png",
            0.99f, 4.0, 392),
    ORANGES(R.string.oranges_brand,
            R.string.oranges_name,
            "fd-oran",
            R.string.oranges_desc,
            "http://gate.net.pl/mobile/fd-oran.png",
            0.89f, 4.3, 462),
    GALA_APPLE(R.string.gala_apples_brand,
               R.string.gala_apples_name,
               "fd-appl",
               R.string.gala_apples_desc,
               "http://gate.net.pl/mobile/fd-appl.png",
               0.79f, 4.5, 321),

    // LIQUIDS
    COCA_COLA(R.string.coca_cola_brand,
              R.string.coca_cola_name,
              "fd-cola",
              R.string.coca_cola_desc,
              "http://gate.net.pl/mobile/fd-cola.png",
              0.49f, 4.6, 342),
    ORANGE_JUICE(R.string.orange_juice_brand,
                 R.string.orange_juice_name,
                 "fd-juic",
                 R.string.orange_juice_desc,
                 "http://gate.net.pl/mobile/fd-juic.png",
                 1.59f, 4.8, 99),
    SEVEN_UP(R.string.seven_up_brand,
             R.string.seven_up_name,
             "fd-7up",
             R.string.seven_up_desc,
             "http://gate.net.pl/mobile/fd-7up.png",
             0.59f, 4.3, 289),

    // DAIRY PRODUCT
    MILK(R.string.milk_brand,
         R.string.milk_name,
         "fd-milk",
         R.string.milk_desc,
         "http://gate.net.pl/mobile/fd-milk.png",
         1.19f, 4.5, 534),
    BUTTER(R.string.butter_brand,
           R.string.butter_name,
           "fd-butt",
           R.string.butter_desc,
           "http://gate.net.pl/mobile/fd-butt.png",
           1.49f, 4.2, 34),
    BUTTER_MILK(R.string.butter_milk_brand,
                R.string.butter_milk_name,
                "fd-btmilk",
                R.string.butter_milk_desc,
                "http://gate.net.pl/mobile/fd-btmilk.png",
                0.99f, 4.7, 312),

    // ****************************************************************************************************************************************
    // SPORT
    // SHOES
    NIKE_SHOES(R.string.nike_brand,
               R.string.nike_shoes_name,
               "sp-airmax",
               R.string.nike_shoes_desc,
               "http://gate.net.pl/mobile/sp-airmax.png",
               135.99f, 4.1, 63),
    ADIDAS_SHOES(R.string.adidas_brand,
                 R.string.adidas_shoes_name,
                 "sp-deer",
                 R.string.adidas_shoes_desc,
                 "http://gate.net.pl/mobile/sp-deer.png",
                 299.99f, 3.9, 57),

    // TROUSERS
    NIKE_TROUSERS(R.string.nike_brand,
                  R.string.nike_trousers_name,
                  "sp-protr",
                  R.string.nike_trousers_desc,
                  "http://gate.net.pl/mobile/sp-protr.png",
                  69.89f, 4.2, 34),
    ADIDAS_TROUSERS(R.string.adidas_brand,
                    R.string.adidas_trousers_name,
                    "sp-tirpan",
                    R.string.adidas_trousers_desc,
                    "http://gate.net.pl/mobile/sp-tirpan.png",
                    89.99f, 3.0, 43),

    // RUNNING ACCESSORIES
    NIKE_BACKPACK(R.string.nike_brand,
                  R.string.nike_backpack_name,
                  "sp-backp",
                  R.string.nike_backpack_desc,
                  "http://gate.net.pl/mobile/sp-backp.png",
                  59.99f, 4.3, 45),
    ADIDAS_SUNGLASSES(R.string.adidas_brand,
                      R.string.adidas_sunglassess_name,
                      "sp-sungl",
                      R.string.adidas_glassess_desc,
                      "http://gate.net.pl/mobile/sp-sungl.png",
                      39.79f, 4.8, 76),
    ADIDAS_HAT(R.string.adidas_brand,
               R.string.adidas_hat_name,
               "sp-hat",
               R.string.adidas_hat_desc,
               "http://gate.net.pl/mobile/sp-hat.png",
               25.00f, 4.1, 15),

    // JACKETS
    NIKE_JACKET(R.string.nike_brand,
                R.string.nike_jacket_name,
                "sp-windr",
                R.string.nike_jacket_desc,
                "http://gate.net.pl/mobile/sp-windr.png",
                79.99f, 4.0, 43),
    ADIDAS_JACKET(R.string.adidas_brand,
                  R.string.adidas_jacket_name,
                  "sp-tirjack",
                  R.string.adidas_jacket_desc,
                  "http://gate.net.pl/mobile/sp-tirjack.png",
                  49.89f, 3.9, 33),

    // ****************************************************************************************************************************************
    // CULTURE
    // MUSIC
    SABBATH_BLOODY_SABBATH(R.string.black_sabbath_brand,
                           R.string.sabbath_bloody_sabbath_name,
                           "cul-sabb",
                           R.string.sabbath_bloody_sabbath_desc,
                           "http://gate.net.pl/mobile/cul-sabb.png",
                           5.99f, 4.0, 67),
    KANSAS_LEFTOVERTURE(R.string.kansas_brand,
                        R.string.kansas_leftoverture_name,
                        "cul-kans",
                        R.string.kansas_leftoverture_desc,
                        "http://gate.net.pl/mobile/cul-kans.png",
                        4.99f, 3.8, 27),
    ABOVE_AND_BEYOND_COMMON_GROUND(R.string.above_and_beyond_brand,
                                   R.string.above_and_beyond_common_ground_name,
                                   "cul-above",
                                   R.string.above_and_beyond_common_ground_desc,
                                   "http://gate.net.pl/mobile/cul-above.png",
                                   7.99f, 4.1, 23),
    ELLIE_GOULDING_HALCYON(R.string.ellie_goulding_brand,
                           R.string.ellie_goulding_halcyon_name,
                           "cul-ellie",
                           R.string.ellie_goulding_halcyon_desc,
                           "http://gate.net.pl/mobile/cul-ellie.png",
                           9.99f, 4.4, 89),

    // BOOKS
    STEENA_HOLMES_THE_FORGOTTEN_ONES_A_NOVEL(R.string.steena_holmes_brand,
                                             R.string.steena_holmes_the_forgotten_ones_a_novel_name,
                                             "cul-forgot",
                                             R.string.steena_holmes_the_forgotten_ones_a_novel_desc,
                                             "http://gate.net.pl/mobile/cul-forgot.png",
                                             12.10f, 4.6, 74),
    CATHERINE_MCKENZIE_THE_GOOD_LIAR(R.string.catherine_mckenzie_brand,
                                     R.string.catherine_mckenzie_the_good_liar_name,
                                     "cul-goodliar",
                                     R.string.catherine_mckenzie_the_good_liar_desc,
                                     "http://gate.net.pl/mobile/cul-goodliar.png",
                                     15.21f, 4.1, 17),
    CATHERINE_MCKENZIE_HIDDEN(R.string.catherine_mckenzie_brand,
                              R.string.catherine_mckenzie_hidden_name,
                              "cul-hidd",
                              R.string.catherine_mckenzie_hidden_desc,
                              "http://gate.net.pl/mobile/cul-hidd.png",
                              16.89f, 3.9, 3),
    JRR_TOLKIEN_THE_SILMARILLION(R.string.jrr_tolkien_brand,
                                 R.string.jrr_tolkien_the_silmarillion_name,
                                 "cul-silm",
                                 R.string.jrr_tolkien_the_silmarillion_desc,
                                 "http://gate.net.pl/mobile/cul-silm.png",
                                 19.99f, 4.2, 213),

    // MOVIES
    BLADE_RUNNER(R.string.blade_runner_brand,
                 R.string.blade_runner_name,
                 "cul-blade",
                 R.string.blade_runner_desc,
                 "http://gate.net.pl/mobile/cul-blade.png",
                 14.45f, 4.1, 77),
    LADY_BIRD(R.string.lady_bird_brand,
              R.string.lady_bird_name,
              "cul-lady",
              R.string.lady_bird_desc,
              "http://gate.net.pl/mobile/cul-lady.png",
              11.99f, 4.4, 5),
    JUSTICE_LEAGUE(R.string.justice_league_brand,
                   R.string.justice_league_name,
                   "cul-justice",
                   R.string.justice_league_desc,
                   "http://gate.net.pl/mobile/cul-justice.png",
                   13.73f, 4.1, 23),
    ALIEN_COVENANT(R.string.alien_covenant_brand,
                   R.string.alien_covenant_name,
                   "cul-alien",
                   R.string.alien_covenant_desc,
                   "http://gate.net.pl/mobile/cul-alien.png",
                   12.85f, 3.6, 152),

    // GAMES
    HALO_5(R.string.halo_5_brand,
           R.string.halo_5_name,
           "cul-halo",
           R.string.halo_5_desc,
           "http://gate.net.pl/mobile/cul-halo.png",
           18.99f, 4.7, 352),
    OVERWATCH(R.string.overwatch_brand,
              R.string.overwatch_name,
              "cul-over",
              R.string.overwatch_desc,
              "http://gate.net.pl/mobile/cul-over.png",
              19.50f, 3.9, 234),
    TITANFALL_2(R.string.titanfall_2_brand,
                R.string.titanfall_2_name,
                "cul-titan",
                R.string.titanfall_2_desc,
                "http://gate.net.pl/mobile/cul-titan.png",
                15.34f, 3.8, 173),
    THE_WITCHER_3(R.string.the_witcher_3_brand,
                  R.string.the_witcher_3_name,
                  "cul-witch",
                  R.string.the_witcher_3_desc,
                  "http://gate.net.pl/mobile/cul-witch.png",
                  19.99f, 4.9, 542);

    // ****************************************************************************************************************************************

    private final String SKU;
    private final String image;
    @StringRes private int name;
    @StringRes private int brand;
    @StringRes private int description;
    private final float price;
    private final double rating;
    private final int ratingCount;

    Product(int brand, int name, String sku, int description, String image, float price, double rating, int ratingCount) {
        this.brand = brand;
        this.name = name;
        this.SKU = sku;
        this.description = description;
        this.image = image;
        this.price = price;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public String getSKU() {
        return SKU;
    }

    public String getImage() {
        return image;
    }

    public int getName() {
        return name;
    }

    public int getBrand() {
        return brand;
    }

    public int getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public double getRating() {
        return rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    // ****************************************************************************************************************************************

    public com.synerise.sdk.event.model.model.Product getEventProduct(Context context, int quantity) {
        com.synerise.sdk.event.model.model.Product product = new com.synerise.sdk.event.model.model.Product();
        product.setSku(getSKU());
        product.setFinalPrice(new UnitPrice(getPrice(), Currency.getInstance(Locale.getDefault())));
        product.setName(context.getString(getName()));
        product.setImage(getImage());
        product.setQuantity(quantity);

        List<String> categories = new ArrayList<>();
        Category category = Category.getCategory(this);
        if (category != null) {
            categories.add(context.getString(category.getText()));
            Section section = Section.getSection(category);
            if (section != null) categories.add(context.getString(section.getName()));
        }
        product.setCategories(categories);
        return product;
    }

    @Nullable
    public static Product getProduct(String sku) {
        for (Product product : Product.values()) {
            if (product.getSKU().equals(sku))
                return product;
        }
        return null;
    }
}
