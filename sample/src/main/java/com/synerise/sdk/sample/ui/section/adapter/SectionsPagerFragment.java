package com.synerise.sdk.sample.ui.section.adapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Section;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.section.category.CategoryActivity;

public class SectionsPagerFragment extends BaseFragment {

    public static SectionsPagerFragment newInstance(Section section) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Args.CONTENT, section);
        SectionsPagerFragment fragment = new SectionsPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // ****************************************************************************************************************************************

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager_sections, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Section section = (Section) arguments.getSerializable(Args.CONTENT);
            if (section != null) {
                setUI(view, section);
            }
        }
    }

    // ****************************************************************************************************************************************

    private void setUI(View view, Section section) {

        ((ImageView) view.findViewById(R.id.page_image)).setImageResource(section.getImage());

        SimpleDraweeView backgroundImage = view.findViewById(R.id.page_bg);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(section.getPattern()).build();
        backgroundImage.setImageURI(imageRequest.getSourceUri());
        new Handler().post(() -> {
            Drawable drawable = ContextCompat.getDrawable(getActivity(), section.getBackground());
            backgroundImage.post(() -> backgroundImage.getHierarchy().setBackgroundImage(drawable));
        });

        ((TextView) view.findViewById(R.id.section_name)).setText(section.getName());

        view.findViewById(R.id.page_text_content)
            .setOnClickListener(v -> startActivity(CategoryActivity.createIntent(getActivity(), section)));

        TextView viewProducts = view.findViewById(R.id.view_products_text);
        viewProducts.setTextColor(ContextCompat.getColor(getActivity(), section.getBottomTextColor()));
        viewProducts.setCompoundDrawablesWithIntrinsicBounds(0, 0, section.getArrow(), 0);
    }
}