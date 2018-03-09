package com.synerise.sdk.sample.view.injector;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.synerise.sdk.injector.net.model.walkthrough.mock.WalkthroughMockResponse;
import com.synerise.sdk.injector.resolver.api.WalkthroughPageMapper;
import com.synerise.sdk.injector.resolver.api.WalkthroughResponseBundle;
import com.synerise.sdk.injector.ui.walkthrough.WalkthroughActivity;
import com.synerise.sdk.injector.ui.walkthrough.pager.model.PageTransformerType;
import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.view.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Random;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

public class WalkthroughActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    // ****************************************************************************************************************************************

    private WalkthroughResponseBundle walkthroughResponseBundle;
    private boolean pageNumberMismatch = false;

    // ****************************************************************************************************************************************

    @Before
    public void setUp() throws Exception {
        walkthroughResponseBundle = new WalkthroughPageMapper().apply(WalkthroughMockResponse.getMockResponse());
        walkthroughResponseBundle.setPageTransformerType(PageTransformerType.SLIDE_RIGHT);
    }
    // ****************************************************************************************************************************************

    @Test
    public void testLoopEnabled() throws Exception {
        walkthroughResponseBundle.setLoopEnabled(true);
        startActivity();
        swipeFromBeginningToEnd(swipeLeft());
        onView(withId(R.id.pager)).perform(swipeLeft());
        testWalkthroughActivity(swipeLeft());
    }

    @Test
    public void testLoopDisabled() throws Exception {
        walkthroughResponseBundle.setLoopEnabled(false);
        startActivity();
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.pager)).perform(swipeLeft());
        testWalkthroughActivity(swipeLeft());
    }

    @Test
    public void testIndicatorsVisibility() throws Exception {
        walkthroughResponseBundle.setAreIndicatorsVisible(false);
        startActivity();
        onView(withId(R.id.pager_indicator))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testCloseButtonVisibility() throws Exception {
        walkthroughResponseBundle.setCloseButtonVisible(false);
        walkthroughResponseBundle.setCloseButtonVisibleOnLastTab(false);
        startActivity();
        swipeFromBeginningToEnd();
        onView(withId(R.id.button_close))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void testWalkthroughActivityWithFadeInAnimation() throws Exception {
        changeAnimation(PageTransformerType.FADE_IN);
        testWalkthroughActivity();
    }

    @Test
    public void testWalkthroughActivityWithPopAnimation() throws Exception {
        changeAnimation(PageTransformerType.POP);
        testWalkthroughActivity();
    }

    @Test
    public void testWalkthroughActivityWithLandAnimation() throws Exception {
        changeAnimation(PageTransformerType.LAND);
        testWalkthroughActivity();
    }

    @Test
    public void testWalkthroughActivityWithSlideLeftAnimation() throws Exception {
        changeAnimation(PageTransformerType.SLIDE_LEFT);
        testWalkthroughActivity();
    }

    @Test
    public void testWalkthroughActivityWithSlideRightAnimation() throws Exception {
        changeAnimation(PageTransformerType.SLIDE_RIGHT);
        testWalkthroughActivity();
    }

    @Test
    public void testWalkthroughActivityWithSlideUpAnimation() throws Exception {
        changeAnimation(PageTransformerType.SLIDE_UP);
        startActivity();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < walkthroughResponseBundle.getPageItems().size() - 1; i++) {
            performRandomSwipesTillSwipeSucceed(i, random);
        }
        onView(withId(R.id.button_close))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    @Test
    public void testWalkthroughActivityWithSlideDownAnimation() throws Exception {
        changeAnimation(PageTransformerType.SLIDE_DOWN);
        startActivity();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < walkthroughResponseBundle.getPageItems().size() - 1; i++) {
            performRandomSwipesTillSwipeSucceed(i, random);
        }
        onView(withId(R.id.button_close))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    // ****************************************************************************************************************************************

    private void performRandomSwipesTillSwipeSucceed(int pageNumber, Random random) {
        onView(withId(R.id.pager)).check(matches(matchSelectedPage(pageNumber)))
                                  .perform(randomSwipeUp(random.nextInt(80)))
                                  .check(matches(matchSelectedPage(pageNumber + 1)));
        if (pageNumberMismatch) {
            pageNumberMismatch = false;
            performRandomSwipesTillSwipeSucceed(pageNumber, random);
        }
    }

    private Matcher<View> matchSelectedPage(final int pageNumber) {
        return new BoundedMatcher<View, ViewPager>(ViewPager.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("Checking the matcher on received view: ");
            }

            @Override
            protected boolean matchesSafely(ViewPager viewPager) {
                if (viewPager.getCurrentItem() != pageNumber) {
                    viewPager.setCurrentItem(pageNumber);
                    pageNumberMismatch = true;
                }
                return true;
            }
        };
    }

    private ViewAction randomSwipeUp(final int offset) {
        return new GeneralSwipeAction(Swipe.FAST, new CoordinatesProvider() {
            @Override
            public float[] calculateCoordinates(View view) {
                float[] xy = GeneralLocation.BOTTOM_CENTER.calculateCoordinates(view);
                xy[1] += -(offset / 100) * view.getHeight();
                return xy;
            }
        }, GeneralLocation.TOP_CENTER, Press.FINGER);
    }

    private void startActivity() {
        mActivityRule.getActivity()
                     .startActivity(WalkthroughActivity.createIntent(mActivityRule.getActivity(), walkthroughResponseBundle));
    }

    private void testWalkthroughActivity(ViewAction swipeAction) throws Exception {
        startActivity();
        swipeFromBeginningToEnd(swipeAction);
        onView(withId(R.id.button_close))
                .check(matches(isDisplayed()))
                .perform(click());
    }

    private void testWalkthroughActivity() throws Exception {
        testWalkthroughActivity(swipeLeft());
    }

    private void changeAnimation(PageTransformerType pageTransformer) {
        walkthroughResponseBundle.setPageTransformerType(pageTransformer);
    }

    private void swipeFromBeginningToEnd() {
        swipeFromBeginningToEnd(swipeLeft());
    }

    private void swipeFromBeginningToEnd(ViewAction swipeAction) {
        for (int i = 0; i < walkthroughResponseBundle.getPageItems().size() - 1; i++) {
            onView(withId(R.id.pager)).perform(swipeAction);
        }
    }
}