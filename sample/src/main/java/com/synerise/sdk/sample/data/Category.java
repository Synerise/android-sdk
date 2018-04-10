package com.synerise.sdk.sample.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.synerise.sdk.sample.R;

import java.util.ArrayList;
import java.util.List;

public class Category implements Parcelable {

    private final String id;
    private final String image;
    @StringRes private final int name;

    // ****************************************************************************************************************************************
    private List<Product> products = new ArrayList<>();

    // ****************************************************************************************************************************************

    private Category(String id, int name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    // ****************************************************************************************************************************************

    public int getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public List<Product> getProducts() {
        return products;
    }

    private void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    // ****************************************************************************************************************************************

    public static List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(createServices());
        categories.add(createElectronics());
        categories.add(createFood());
        return categories;
    }

    private static Category createServices() {
        Category services = new Category("1", R.string.default_services, "https://www.ylconsulting.com/wp-content/uploads/2017/04/shutterstock_350999087.jpg");
        List<Product> products = new ArrayList<>();
        products.add(new Product("1", ImageLinks.Services.card, R.string.default_card, R.string.default_card_desc, 3.49));
        products.add(new Product("2", ImageLinks.Services.support, R.string.default_support, R.string.default_support_desc, 4.99));
        products.add(new Product("3", ImageLinks.Services.insurance, R.string.default_insurance, R.string.default_insurance_desc, 3.89));
        services.setProducts(products);
        return services;
    }

    private static Category createElectronics() {
        Category electronics = new Category("2", R.string.default_electronics, "https://fpimages.withfloats.com/actual/59684a2fc27c121850f2f400.jpg");
        List<Product> products = new ArrayList<>();
        products.add(new Product("4", ImageLinks.Electronics.tv, R.string.default_tv, R.string.default_tv_desc, 4.99));
        products.add(new Product("5", ImageLinks.Electronics.mobile, R.string.default_mobile, R.string.default_mobile_desc, 4.39));
        products.add(new Product("6", ImageLinks.Electronics.laptop, R.string.default_laptop, R.string.default_laptop_desc, 6.99));
        electronics.setProducts(products);
        return electronics;
    }

    private static Category createFood() {
        Category food = new Category("3", R.string.default_food, "https://www.pkt.pl/wiz/SZC/4521803/1/background_file/1_SZC_4521803_1_.jpg");
        List<Product> products = new ArrayList<>();
        products.add(new Product("7", ImageLinks.Food.strawberry, R.string.default_strawberry, R.string.default_strawberry_desc, 19.99));
        products.add(new Product("8", ImageLinks.Food.potato, R.string.default_potato, R.string.default_potato_desc, 29.99));
        products.add(new Product("9", ImageLinks.Food.ribs, R.string.default_ribs, R.string.default_ribs_desc, 23.99));
        food.setProducts(products);
        return food;
    }

    // ****************************************************************************************************************************************

    private interface ImageLinks {
        interface Services {
            String card = "https://i.imgur.com/r2fPt6u.png";
            String support = "https://wpengine.com/wp-content/themes/genesis-sleek-wpengine/images/hero/support.jpg";
            String insurance = "http://nfda.org.za/wp-content/uploads/2017/02/insurance-title-image_tcm7-198694.jpg";
        }

        interface Electronics {
            String tv = "http://images.samsung.com/is/image/samsung/my-full-hd-k6300-ua49k6300akxxm-001-front-black?$PD_GALLERY_L_JPG$";
            String mobile = "https://cdn.x-kom.pl/i/setup/images/prod/big/product-large,,2018/1/pr_2018_1_22_10_17_48_712_06.jpg";
            String laptop = "https://souqcms.s3.amazonaws.com/spring/images/2017/Apple/apple-macbook-pro-touch-bar-touch-id/4-apple-macbook-pro-touch-bar-touch-id-core-i7-15-inch-space-gray.jpg";
        }

        interface Food {
            String strawberry = "http://liofi.pl/wp-content/uploads/2017/11/truskawki2.jpg";
            String potato = "https://www.almanac.com/sites/default/files/styles/primary_image_in_article/public/image_nodes/potatoes.jpg";
            String ribs = "http://s3.amazonaws.com/yummy_uploads2/blog/8030.jpg";
        }
    }

    // ****************************************************************************************************************************************

    protected Category(Parcel in) {
        name = in.readInt();
        image = in.readString();
        id = in.readString();
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(name);
        dest.writeString(image);
        dest.writeString(id);
        dest.writeTypedList(products);
    }
}
