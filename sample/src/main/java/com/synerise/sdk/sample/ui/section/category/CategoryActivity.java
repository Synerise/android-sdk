package com.synerise.sdk.sample.ui.section.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import com.synerise.sdk.event.Tracker;
import com.synerise.sdk.event.TrackerParams;
import com.synerise.sdk.event.model.CustomEvent;
import com.synerise.sdk.event.model.interaction.VisitedScreenEvent;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Category;
import com.synerise.sdk.sample.data.Section;
import com.synerise.sdk.sample.ui.BaseActivity;
import com.synerise.sdk.sample.ui.section.category.adapter.CategoriesRecyclerAdapter;
import com.synerise.sdk.sample.ui.section.category.products.ProductsActivity;
import com.synerise.sdk.sample.util.ToolbarHelper;

public class CategoryActivity extends BaseActivity {

    private Section section;

    public static Intent createIntent(Context context, Section section) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(Args.SERIALIZABLE, section);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        section = (Section) getIntent().getSerializableExtra(Args.SERIALIZABLE);

        ToolbarHelper.setUpChildToolbar(this, section.getName(), section.getColor());

        CategoriesRecyclerAdapter recyclerAdapter = new CategoriesRecyclerAdapter(this, this::onCategorySelected,
                                                                                  section.getCategories());
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setHasFixedSize(true);
        recycler.setAdapter(recyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Tracker.send(new VisitedScreenEvent(getClass().getSimpleName(),
                                            new TrackerParams.Builder().add("category_id", section.getId()).build()));
    }

    private void onCategorySelected(Category category) {
        Tracker.send(new CustomEvent("category.view", getString(category.getText()),
                                     new TrackerParams.Builder()
                                             .add("label", getString(category.getText()))
                                             .add("parentCategory", getString(section.getName()))
                                             .build()));
        startActivity(ProductsActivity.createIntent(this, category));
    }
}
