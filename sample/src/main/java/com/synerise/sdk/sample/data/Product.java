package com.synerise.sdk.sample.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.StringRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Product implements Parcelable {

    @SerializedName("sku") private final String SKU;
    @SerializedName("image") private final String image;
    @SerializedName("name") private String name;
    @SerializedName("description") private String description;
    @SerializedName("price") private final double price;

    @Expose(serialize = false, deserialize = false) @StringRes private int hardcodedName;
    @Expose(serialize = false, deserialize = false) @StringRes private int hardcodedDescription;

    // ****************************************************************************************************************************************

    Product(String SKU, String image, int hardcodedName, int hardcodedDescription, double price) {
        this.SKU = SKU;
        this.image = image;
        this.hardcodedName = hardcodedName;
        this.hardcodedDescription = hardcodedDescription;
        this.price = price;
    }

    // ****************************************************************************************************************************************

    public String getSKU() {
        return SKU;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getHardcodedName() {
        return hardcodedName;
    }

    public int getHardcodedDescription() {
        return hardcodedDescription;
    }

    public String getPrice() {
        return String.format(Locale.US, "%.2f", price);
    }

    // ****************************************************************************************************************************************

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SKU);
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeDouble(this.price);
        dest.writeInt(this.hardcodedName);
        dest.writeInt(this.hardcodedDescription);
    }

    protected Product(Parcel in) {
        this.SKU = in.readString();
        this.image = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.price = in.readDouble();
        this.hardcodedName = in.readInt();
        this.hardcodedDescription = in.readInt();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {return new Product(source);}

        @Override
        public Product[] newArray(int size) {return new Product[size];}
    };
}
