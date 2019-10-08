package com.synerise.sdk.sample.data;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import com.synerise.sdk.sample.R;

import java.util.ArrayList;
import java.util.List;

public enum Section {

    ELECTRONICS("1",
                R.drawable.image_electronics,
                R.drawable.background_electronics,
                R.drawable.pattern_electronics,
                R.drawable.ic_arrow_electronics,
                R.string.section_electronic,
                createElectronicsCategories(),
                R.color.persian,
                R.color.spring),

    FOOD("2",
         R.drawable.image_food,
         R.drawable.background_food,
         R.drawable.pattern_food,
         R.drawable.ic_arrow_food,
         R.string.section_food,
         createFoodCategories(),
         R.color.laurel,
         R.color.bright),

    FASHION("3",
            R.drawable.image_fashion,
            R.drawable.background_fashion,
            R.drawable.pattern_fashion,
            R.drawable.ic_arrow_fashion,
            R.string.section_fashion,
            createFashionCategories(),
            R.color.purple,
            R.color.tangerine),

    CULTURE("4",
            R.drawable.image_culture,
            R.drawable.background_culture,
            R.drawable.pattern_culture,
            R.drawable.ic_arrow_culture,
            R.string.section_culture,
            createCultureCategories(),
            R.color.pine,
            R.color.periwinkle);

    public static List<Section> getSections() {
        ArrayList<Section> sections = new ArrayList<>();
        sections.add(ELECTRONICS);
        sections.add(FOOD);
        sections.add(FASHION);
        sections.add(CULTURE);
        return sections;
    }

    private static List<Category> createElectronicsCategories() {
        ArrayList<Category> list = new ArrayList<>();
        list.add(Category.HD_TV);
        list.add(Category.LAPTOPS_TABLETS);
        list.add(Category.PHONES);
        list.add(Category.HOUSEHOLD_GOODS);
        return list;
    }

    private static List<Category> createFoodCategories() {
        ArrayList<Category> list = new ArrayList<>();
        list.add(Category.VEGETABLES);
        list.add(Category.FRUITS);
        list.add(Category.LIQUIDS);
        list.add(Category.DAIRY_PRODUCTS);
        return list;
    }

    private static List<Category> createFashionCategories() {
        ArrayList<Category> list = new ArrayList<>();
        list.add(Category.SHOES);
        list.add(Category.TROUSERS);
        list.add(Category.RUNNING_ACCESSORIES);
        list.add(Category.JACKETS);
        return list;
    }

    private static List<Category> createCultureCategories() {
        ArrayList<Category> list = new ArrayList<>();
        list.add(Category.MUSIC);
        list.add(Category.BOOKS);
        list.add(Category.MOVIES);
        list.add(Category.GAMES);
        return list;
    }

    private final String id;
    @DrawableRes private final int image;
    @DrawableRes private final int background;
    @DrawableRes private final int pattern;
    @DrawableRes private final int arrow;
    @StringRes private final int name;
    private final List<Category> categories;
    @ColorRes private final int color;
    @ColorRes private final int bottomTextColor;

    Section(String id, int image, int background, int pattern, int arrow, int name,
            List<Category> categories, int color, int bottomTextColor) {
        this.id = id;
        this.image = image;
        this.background = background;
        this.pattern = pattern;
        this.arrow = arrow;
        this.name = name;
        this.categories = categories;
        this.color = color;
        this.bottomTextColor = bottomTextColor;
    }

    public String getId() {
        return id;
    }

    @DrawableRes
    public int getImage() {
        return image;
    }

    @DrawableRes
    public int getBackground() {
        return background;
    }

    @DrawableRes
    public int getPattern() {
        return pattern;
    }

    @DrawableRes
    public int getArrow() {
        return arrow;
    }

    @StringRes
    public int getName() {
        return name;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @ColorRes
    public int getColor() {
        return color;
    }

    @ColorRes
    public int getBottomTextColor() {
        return bottomTextColor;
    }

    @Nullable
    public static Section getSection(Category category) {
        for (Section section : Section.values()) {
            for (Category sectionCategory : section.getCategories()) {
                if (sectionCategory == category)
                    return section;
            }
        }
        return null;
    }
}
