package com.synerise.sdk.sample.view.client;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.synerise.sdk.sample.R;
import com.synerise.sdk.sample.test.EspressoTestingIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

public class ClientAccountActivityTest {

    @Rule
    public ActivityTestRule<ClientAccountActivity> mActivityRule = new ActivityTestRule<>(ClientAccountActivity.class);
    private ClientAccountActivity activity;

    // ****************************************************************************************************************************************

    @Before
    public void registerIdlingResource() {
        activity = mActivityRule.getActivity();
        IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());
    }

    // ****************************************************************************************************************************************

    @Test
    public void testGetAccountMethod() throws InterruptedException {
        onView(withId(R.id.client_get_account)).check(matches(isDisplayed()))
                                               .perform(click());
        checkMessageDisplayed();
    }

    @Test
    public void testUpdateAccountMethod() throws InterruptedException {
        onView(withId(R.id.client_update_account)).check(matches(isDisplayed()))
                                                  .perform(click());
        checkMessageDisplayed();
    }

    @Test
    public void testGetTokenMethod() throws InterruptedException {
        onView(withId(R.id.client_get_token)).check(matches(isDisplayed()))
                                             .perform(click());
        checkMessageDisplayed();
    }

    // ****************************************************************************************************************************************

    private void checkMessageDisplayed() {
        onView(withText(containsString(activity.getString(R.string.message_failure)))).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}