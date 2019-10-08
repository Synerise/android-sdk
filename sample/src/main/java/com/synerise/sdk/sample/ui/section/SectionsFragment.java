package com.synerise.sdk.sample.ui.section;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.synerise.sdk.injector.ui.walkthrough.InfiniteLoopViewPager;
import com.synerise.sdk.injector.ui.walkthrough.indicators.IndicatorsLayout;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.data.Section;
import com.synerise.sdk.sample.ui.BaseFragment;
import com.synerise.sdk.sample.ui.section.adapter.SectionsPagerAdapter;
import com.synerise.sdk.sample.util.ToolbarHelper;

import java.util.ArrayList;
import java.util.List;

public class SectionsFragment extends BaseFragment {

    private static final float PAGE_IMAGE_CONTENT_MULTIPLIER = 0.5f;
    private static final float PAGE_TEXT_CONTENT_MULTIPLIER = 1.0f;
    private static final float VIEW_PRODUCTS_MULTIPLIER = 1.5f;
    private UpdateStatusBarColorInterface updateStatusBarColorListener;

    public static SectionsFragment newInstance() {
        return new SectionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sections, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Section> sections = Section.getSections();

        // adapter
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), new ArrayList<>(sections));
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        View root = view.findViewById(R.id.root_layout);

        // view pager
        InfiniteLoopViewPager viewPager = view.findViewById(R.id.sections_view_pager);
        viewPager.setIsLoopEnabled(true);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int evaluatedColor = getEvaluatedColor(position, positionOffset);
                root.setBackgroundColor(evaluatedColor);
                updateStatusBarColorListener.updateStatusBarColor(evaluatedColor);
                ToolbarHelper.updateToolbarColor((AppCompatActivity) getActivity(), evaluatedColor);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ToolbarHelper.updateToolbarTitle((AppCompatActivity) getActivity(), sections.get(position).getName());
            }

            private int getEvaluatedColor(int position, float positionOffset) {
                int startColor = getColor(sections.get(position));
                int nextColor = position != (sections.size() - 1) ?
                        getColor(sections.get(position + 1)) : getColor(sections.get(0));
                return (int) argbEvaluator.evaluate(positionOffset, startColor, nextColor);
            }

            private int getColor(Section section) {
                return ContextCompat.getColor(getActivity(), section.getColor());
            }
        });

        ToolbarHelper.updateToolbarTitle((AppCompatActivity) getActivity(), sections.get(0).getName());

        viewPager.setAdapter(sectionsPagerAdapter);
        viewPager.setPageTransformer(true, (page, position) -> {
            View viewProductsText = page.findViewById(R.id.view_products_text);
            View pageImageContent = page.findViewById(R.id.page_image_content);
            View pageTextContent = page.findViewById(R.id.page_text_content);
            viewProductsText.setTranslationX(viewProductsText.getWidth() * position * VIEW_PRODUCTS_MULTIPLIER);
            pageImageContent.setTranslationX(pageImageContent.getWidth() * position * PAGE_IMAGE_CONTENT_MULTIPLIER);
            pageTextContent.setTranslationX(pageTextContent.getWidth() * position * PAGE_TEXT_CONTENT_MULTIPLIER);
        });
        viewPager.setOffscreenPageLimit(sectionsPagerAdapter.getCount());

        IndicatorsLayout indicatorsLayout = view.findViewById(R.id.indicators);
        indicatorsLayout.init(viewPager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateStatusBarColorInterface) {
            updateStatusBarColorListener = (UpdateStatusBarColorInterface) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        updateStatusBarColorListener = UpdateStatusBarColorInterface.NULL;
    }
}